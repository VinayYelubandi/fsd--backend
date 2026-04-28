package com.scholarship.platform.repository;

import com.scholarship.platform.entity.SavedScholarship;
import com.scholarship.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedScholarshipRepository extends JpaRepository<SavedScholarship, Long> {

    List<SavedScholarship> findByUser(User user);

    Optional<SavedScholarship> findByUserAndScholarshipId(User user, Long scholarshipId);

    boolean existsByUserAndScholarshipId(User user, Long scholarshipId);

    void deleteByUserAndScholarshipId(User user, Long scholarshipId);
}
