package com.scholarship.platform.repository;

import com.scholarship.platform.entity.Scholarship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    List<Scholarship> findByStatus(Scholarship.ScholarshipStatus status);

    long countByStatus(Scholarship.ScholarshipStatus status);

    @Query("""
        SELECT s FROM Scholarship s
        WHERE s.status = 'ACTIVE'
        AND (:query IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(s.provider) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%')))
        AND (:category IS NULL OR s.category = :category)
        ORDER BY s.deadline ASC
        """)
    Page<Scholarship> searchScholarships(
        @Param("query") String query,
        @Param("category") String category,
        Pageable pageable
    );

    @Query("SELECT DISTINCT s.category FROM Scholarship s WHERE s.category IS NOT NULL ORDER BY s.category")
    List<String> findAllCategories();
}
