package com.scholarship.platform.controller;

import com.scholarship.platform.dto.ApplicationDto;
import com.scholarship.platform.dto.DashboardStatsDto;
import com.scholarship.platform.dto.FinancialAidDto;
import com.scholarship.platform.dto.UserDto;
import com.scholarship.platform.entity.Application;
import com.scholarship.platform.entity.User;
import com.scholarship.platform.service.AdminService;
import com.scholarship.platform.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private ApplicationService applicationService;

    // ─── Dashboard ──────────────────────────────────────────────

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // ─── Applications ───────────────────────────────────────────

    @GetMapping("/applications")
    public ResponseEntity<Page<ApplicationDto>> getAllApplications(
            @RequestParam(required = false) Application.ApplicationStatus status,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(applicationService.getAllApplications(status, query, page, size));
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody ApplicationDto.StatusUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(applicationService.updateStatus(id, request, userDetails.getUsername()));
    }

    // ─── User Management ────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<UserDto> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        User.UserStatus status = User.UserStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(adminService.updateUserStatus(id, status));
    }

    // ─── Financial Aid ──────────────────────────────────────────

    @GetMapping("/financial-aids")
    public ResponseEntity<List<FinancialAidDto>> getFinancialAids() {
        return ResponseEntity.ok(adminService.getAllFinancialAids());
    }

    @PostMapping("/financial-aids")
    public ResponseEntity<FinancialAidDto> createFinancialAid(
            @Valid @RequestBody FinancialAidDto.FinancialAidRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createFinancialAid(request));
    }

    @PutMapping("/financial-aids/{id}")
    public ResponseEntity<FinancialAidDto> updateFinancialAid(
            @PathVariable Long id,
            @RequestBody FinancialAidDto.FinancialAidRequest request) {
        return ResponseEntity.ok(adminService.updateFinancialAid(id, request));
    }

    @DeleteMapping("/financial-aids/{id}")
    public ResponseEntity<Map<String, String>> deleteFinancialAid(@PathVariable Long id) {
        adminService.deleteFinancialAid(id);
        return ResponseEntity.ok(Map.of("message", "Financial aid deleted successfully"));
    }
}
