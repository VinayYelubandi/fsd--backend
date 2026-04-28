package com.scholarship.platform.repository;

import com.scholarship.platform.entity.Application;
import com.scholarship.platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentOrderByAppliedAtDesc(User student);

    Optional<Application> findByStudentAndScholarshipId(User student, Long scholarshipId);

    boolean existsByStudentAndScholarshipId(User student, Long scholarshipId);

    long countByStatus(Application.ApplicationStatus status);

    Page<Application> findAllByOrderByAppliedAtDesc(Pageable pageable);

    @Query("""
        SELECT a FROM Application a
        WHERE (:status IS NULL OR a.status = :status)
        AND (:query IS NULL OR LOWER(a.student.firstName) LIKE LOWER(CONCAT('%',:query,'%'))
             OR LOWER(a.student.email) LIKE LOWER(CONCAT('%',:query,'%'))
             OR LOWER(a.scholarship.title) LIKE LOWER(CONCAT('%',:query,'%')))
        ORDER BY a.appliedAt DESC
        """)
    Page<Application> searchApplications(
        @Param("status") Application.ApplicationStatus status,
        @Param("query") String query,
        Pageable pageable
    );
}
