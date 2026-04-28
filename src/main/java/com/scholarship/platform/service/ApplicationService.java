package com.scholarship.platform.service;

import com.scholarship.platform.dto.ApplicationDto;
import com.scholarship.platform.entity.Application;
import com.scholarship.platform.entity.Notification;
import com.scholarship.platform.entity.Scholarship;
import com.scholarship.platform.entity.User;
import com.scholarship.platform.exception.BadRequestException;
import com.scholarship.platform.exception.ResourceNotFoundException;
import com.scholarship.platform.repository.ApplicationRepository;
import com.scholarship.platform.repository.NotificationRepository;
import com.scholarship.platform.repository.ScholarshipRepository;
import com.scholarship.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService {

    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private ScholarshipRepository scholarshipRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private NotificationRepository notificationRepository;

    public ApplicationDto apply(Long scholarshipId, ApplicationDto.ApplicationRequest request, String email) {
        User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
            .orElseThrow(() -> new ResourceNotFoundException("Scholarship", scholarshipId));

        if (applicationRepository.existsByStudentAndScholarshipId(student, scholarshipId)) {
            throw new BadRequestException("You have already applied for this scholarship");
        }

        if (scholarship.getStatus() != Scholarship.ScholarshipStatus.ACTIVE) {
            throw new BadRequestException("This scholarship is not currently accepting applications");
        }

        Application application = Application.builder()
            .student(student)
            .scholarship(scholarship)
            .status(Application.ApplicationStatus.SUBMITTED)
            .fullName(request.getFullName())
            .dateOfBirth(request.getDateOfBirth())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .state(request.getState())
            .zipCode(request.getZipCode())
            .institution(request.getInstitution())
            .major(request.getMajor())
            .gpa(request.getGpa())
            .graduationYear(request.getGraduationYear())
            .currentYear(request.getCurrentYear())
            .statement(request.getStatement())
            .financialNeed(request.getFinancialNeed())
            .achievements(request.getAchievements())
            .build();

        Application saved = applicationRepository.save(application);

        // Send notification
        sendNotification(student,
            "Your application for '" + scholarship.getTitle() + "' has been submitted successfully!",
            Notification.NotificationType.SUCCESS);

        return ApplicationDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationDto> getMyApplications(String email) {
        User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return applicationRepository.findByStudentOrderByAppliedAtDesc(student)
            .stream().map(ApplicationDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApplicationDto getById(Long id, String email) {
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Application", id));

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Students can only see their own applications
        if (user.getRole() == User.Role.STUDENT &&
            !application.getStudent().getId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }
        return ApplicationDto.from(application);
    }

    public void withdraw(Long id, String email) {
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Application", id));

        User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!application.getStudent().getId().equals(student.getId())) {
            throw new BadRequestException("Access denied");
        }

        if (application.getStatus() == Application.ApplicationStatus.APPROVED) {
            throw new BadRequestException("Approved applications cannot be withdrawn");
        }

        application.setStatus(Application.ApplicationStatus.WITHDRAWN);
        applicationRepository.save(application);
    }

    // ─── Admin Methods ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ApplicationDto> getAllApplications(Application.ApplicationStatus status, String query, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return applicationRepository.searchApplications(status, query, pageable)
            .map(ApplicationDto::from);
    }

    public ApplicationDto updateStatus(Long id, ApplicationDto.StatusUpdateRequest request, String adminEmail) {
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Application", id));

        Application.ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(request.getStatus());
        application.setAdminNotes(request.getAdminNotes());
        application.setReviewedBy(adminEmail);
        application.setReviewedAt(LocalDateTime.now());

        Application saved = applicationRepository.save(application);

        // Notify student
        String msg = switch (request.getStatus()) {
            case APPROVED -> "🎉 Congratulations! Your application for '" +
                application.getScholarship().getTitle() + "' has been APPROVED!";
            case REJECTED -> "Your application for '" +
                application.getScholarship().getTitle() + "' was not selected.";
            case DOCUMENTS_REQUIRED -> "Additional documents are required for your application to '" +
                application.getScholarship().getTitle() + "'. Please check your portal.";
            case UNDER_REVIEW -> "Your application for '" +
                application.getScholarship().getTitle() + "' is now under review.";
            default -> "Your application status has been updated.";
        };

        Notification.NotificationType notifType = request.getStatus() == Application.ApplicationStatus.APPROVED
            ? Notification.NotificationType.SUCCESS
            : request.getStatus() == Application.ApplicationStatus.REJECTED
            ? Notification.NotificationType.ERROR
            : Notification.NotificationType.INFO;

        sendNotification(application.getStudent(), msg, notifType);

        return ApplicationDto.from(saved);
    }

    // ─── Helper ─────────────────────────────────────────────────
    private void sendNotification(User user, String message, Notification.NotificationType type) {
        Notification notification = Notification.builder()
            .user(user)
            .message(message)
            .type(type)
            .build();
        notificationRepository.save(notification);
    }
}
