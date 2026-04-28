package com.scholarship.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applications",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "scholarship_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Scholarship scholarship;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    // Personal Info
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    private String phone;
    private String address;
    private String city;
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    // Academic Info
    private String institution;
    private String major;
    private Double gpa;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    @Column(name = "current_year")
    private String currentYear;

    @Column(columnDefinition = "TEXT")
    private String statement;

    @Column(name = "financial_need", columnDefinition = "TEXT")
    private String financialNeed;

    private String achievements;

    // Admin notes
    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Document> documents = new ArrayList<>();

    public enum ApplicationStatus {
        SUBMITTED, UNDER_REVIEW, DOCUMENTS_REQUIRED, APPROVED, REJECTED, WITHDRAWN
    }
}
