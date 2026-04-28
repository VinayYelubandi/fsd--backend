package com.scholarship.platform.dto;

import com.scholarship.platform.entity.Scholarship;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipDto {

    private Long id;
    private String title;
    private String provider;
    private String category;
    private BigDecimal amount;
    private LocalDate deadline;
    private String description;
    private String eligibility;
    private Double gpaRequirement;
    private Integer seats;
    private String website;
    private Boolean renewable;
    private String renewalCriteria;
    private Scholarship.ScholarshipStatus status;
    private Integer applicantCount;
    private Boolean saved;
    private LocalDateTime createdAt;

    // ─── Request DTO ───────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScholarshipRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Provider is required")
        private String provider;

        private String category;
        private BigDecimal amount;

        @NotNull(message = "Deadline is required")
        private LocalDate deadline;

        private String description;
        private String eligibility;
        private Double gpaRequirement;
        private Integer seats;
        private String website;
        private Boolean renewable;
        private String renewalCriteria;
        private Scholarship.ScholarshipStatus status;
    }

    // ─── Static factory ─────────────────────────────────────
    public static ScholarshipDto from(Scholarship s, boolean saved) {
        return ScholarshipDto.builder()
            .id(s.getId())
            .title(s.getTitle())
            .provider(s.getProvider())
            .category(s.getCategory())
            .amount(s.getAmount())
            .deadline(s.getDeadline())
            .description(s.getDescription())
            .eligibility(s.getEligibility())
            .gpaRequirement(s.getGpaRequirement())
            .seats(s.getSeats())
            .website(s.getWebsite())
            .renewable(s.getRenewable())
            .renewalCriteria(s.getRenewalCriteria())
            .status(s.getStatus())
            .applicantCount(s.getApplications().size())
            .saved(saved)
            .createdAt(s.getCreatedAt())
            .build();
    }
}
