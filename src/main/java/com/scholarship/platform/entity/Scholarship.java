package com.scholarship.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scholarships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String provider;

    @Column(length = 50)
    private String category;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String eligibility;

    @Column(name = "gpa_requirement")
    private Double gpaRequirement;

    private Integer seats;

    private String website;

    @Column(nullable = false)
    @Builder.Default
    private Boolean renewable = false;

    @Column(name = "renewal_criteria")
    private String renewalCriteria;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ScholarshipStatus status = ScholarshipStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<SavedScholarship> savedByUsers = new ArrayList<>();

    public enum ScholarshipStatus {
        ACTIVE, INACTIVE, DRAFT, EXPIRED
    }
}
