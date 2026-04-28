package com.scholarship.platform.controller;

import com.scholarship.platform.dto.ApplicationDto;
import com.scholarship.platform.entity.Application;
import com.scholarship.platform.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/{scholarshipId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApplicationDto> apply(
            @PathVariable Long scholarshipId,
            @RequestBody ApplicationDto.ApplicationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(applicationService.apply(scholarshipId, request, userDetails.getUsername()));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ApplicationDto>> getMyApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(applicationService.getMyApplications(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApplicationDto> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(applicationService.getById(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> withdraw(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        applicationService.withdraw(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Application withdrawn successfully"));
    }
}
