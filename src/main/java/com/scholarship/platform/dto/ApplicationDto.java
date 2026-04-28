package com.scholarship.platform.dto;

import com.scholarship.platform.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Long id;
    private Long scholarshipId;
    private String scholarshipTitle;
    private String scholarshipProvider;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Application.ApplicationStatus status;

    // Personal
    private String fullName;
    private String dateOfBirth;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;

    // Academic
    private String institution;
    private String major;
    private Double gpa;
    private Integer graduationYear;
    private String currentYear;
    private String statement;
    private String financialNeed;
    private String achievements;

    private String adminNotes;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    // ─── Request DTO ─────────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationRequest {
        private String fullName;
        private String dateOfBirth;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String institution;
        private String major;
        private Double gpa;
        private Integer graduationYear;
        private String currentYear;
        private String statement;
        private String financialNeed;
        private String achievements;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        private Application.ApplicationStatus status;
        private String adminNotes;
    }

    // ─── Static factory ──────────────────────────────────────
    public static ApplicationDto from(Application a) {
        return ApplicationDto.builder()
            .id(a.getId())
            .scholarshipId(a.getScholarship().getId())
            .scholarshipTitle(a.getScholarship().getTitle())
            .scholarshipProvider(a.getScholarship().getProvider())
            .studentId(a.getStudent().getId())
            .studentName(a.getStudent().getFirstName() + " " + a.getStudent().getLastName())
            .studentEmail(a.getStudent().getEmail())
            .status(a.getStatus())
            .fullName(a.getFullName())
            .dateOfBirth(a.getDateOfBirth())
            .phone(a.getPhone())
            .address(a.getAddress())
            .city(a.getCity())
            .state(a.getState())
            .zipCode(a.getZipCode())
            .institution(a.getInstitution())
            .major(a.getMajor())
            .gpa(a.getGpa())
            .graduationYear(a.getGraduationYear())
            .currentYear(a.getCurrentYear())
            .statement(a.getStatement())
            .financialNeed(a.getFinancialNeed())
            .achievements(a.getAchievements())
            .adminNotes(a.getAdminNotes())
            .appliedAt(a.getAppliedAt())
            .updatedAt(a.getUpdatedAt())
            .build();
    }
}
