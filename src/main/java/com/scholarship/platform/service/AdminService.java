package com.scholarship.platform.service;

import com.scholarship.platform.dto.DashboardStatsDto;
import com.scholarship.platform.dto.FinancialAidDto;
import com.scholarship.platform.dto.UserDto;
import com.scholarship.platform.entity.Application;
import com.scholarship.platform.entity.FinancialAid;
import com.scholarship.platform.entity.Scholarship;
import com.scholarship.platform.entity.User;
import com.scholarship.platform.exception.ResourceNotFoundException;
import com.scholarship.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private ScholarshipRepository scholarshipRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private FinancialAidRepository financialAidRepository;

    // ─── Dashboard ──────────────────────────────────────────────

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        return DashboardStatsDto.builder()
            .totalUsers(userRepository.count())
            .totalStudents(userRepository.countByRole(User.Role.STUDENT))
            .totalScholarships(scholarshipRepository.count())
            .activeScholarships(scholarshipRepository.countByStatus(Scholarship.ScholarshipStatus.ACTIVE))
            .totalApplications(applicationRepository.count())
            .pendingApplications(applicationRepository.countByStatus(Application.ApplicationStatus.UNDER_REVIEW)
                + applicationRepository.countByStatus(Application.ApplicationStatus.SUBMITTED))
            .approvedApplications(applicationRepository.countByStatus(Application.ApplicationStatus.APPROVED))
            .rejectedApplications(applicationRepository.countByStatus(Application.ApplicationStatus.REJECTED))
            .totalFinancialAids(financialAidRepository.count())
            .totalAidDisbursed("$2,800,000+")
            .build();
    }

    // ─── User Management ────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserDto::from)
            .collect(Collectors.toList());
    }

    public UserDto updateUserStatus(Long userId, User.UserStatus status) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setStatus(status);
        return UserDto.from(userRepository.save(user));
    }

    // ─── Financial Aid ──────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<FinancialAidDto> getAllFinancialAids() {
        return financialAidRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(FinancialAidDto::from)
            .collect(Collectors.toList());
    }

    public FinancialAidDto createFinancialAid(FinancialAidDto.FinancialAidRequest request) {
        FinancialAid aid = FinancialAid.builder()
            .title(request.getTitle())
            .category(request.getCategory())
            .amount(request.getAmount())
            .maxApplicants(request.getMaxApplicants())
            .deadline(request.getDeadline())
            .description(request.getDescription())
            .status(request.getStatus() != null ? request.getStatus() : FinancialAid.AidStatus.ACTIVE)
            .build();
        return FinancialAidDto.from(financialAidRepository.save(aid));
    }

    public FinancialAidDto updateFinancialAid(Long id, FinancialAidDto.FinancialAidRequest request) {
        FinancialAid aid = financialAidRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Financial Aid", id));

        if (request.getTitle() != null) aid.setTitle(request.getTitle());
        if (request.getCategory() != null) aid.setCategory(request.getCategory());
        if (request.getAmount() != null) aid.setAmount(request.getAmount());
        if (request.getMaxApplicants() != null) aid.setMaxApplicants(request.getMaxApplicants());
        if (request.getDeadline() != null) aid.setDeadline(request.getDeadline());
        if (request.getDescription() != null) aid.setDescription(request.getDescription());
        if (request.getStatus() != null) aid.setStatus(request.getStatus());

        return FinancialAidDto.from(financialAidRepository.save(aid));
    }

    public void deleteFinancialAid(Long id) {
        if (!financialAidRepository.existsById(id)) {
            throw new ResourceNotFoundException("Financial Aid", id);
        }
        financialAidRepository.deleteById(id);
    }
}
