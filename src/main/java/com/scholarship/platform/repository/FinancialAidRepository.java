package com.scholarship.platform.repository;

import com.scholarship.platform.entity.FinancialAid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialAidRepository extends JpaRepository<FinancialAid, Long> {
    List<FinancialAid> findByStatus(FinancialAid.AidStatus status);
    List<FinancialAid> findAllByOrderByCreatedAtDesc();
}
