package com.scholarship.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_aids")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialAid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "max_applicants")
    private Integer maxApplicants;

    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AidStatus status = AidStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AidStatus {
        ACTIVE, INACTIVE
    }
}
