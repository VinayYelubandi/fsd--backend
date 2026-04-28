package com.scholarship.platform.dto;

import com.scholarship.platform.entity.FinancialAid;
import jakarta.validation.constraints.NotBlank;
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
public class FinancialAidDto {

    private Long id;
    private String title;
    private String category;
    private BigDecimal amount;
    private Integer maxApplicants;
    private LocalDate deadline;
    private String description;
    private FinancialAid.AidStatus status;
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialAidRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Category is required")
        private String category;

        private BigDecimal amount;
        private Integer maxApplicants;
        private LocalDate deadline;
        private String description;
        private FinancialAid.AidStatus status;
    }

    public static FinancialAidDto from(FinancialAid aid) {
        return FinancialAidDto.builder()
            .id(aid.getId())
            .title(aid.getTitle())
            .category(aid.getCategory())
            .amount(aid.getAmount())
            .maxApplicants(aid.getMaxApplicants())
            .deadline(aid.getDeadline())
            .description(aid.getDescription())
            .status(aid.getStatus())
            .createdAt(aid.getCreatedAt())
            .build();
    }
}
