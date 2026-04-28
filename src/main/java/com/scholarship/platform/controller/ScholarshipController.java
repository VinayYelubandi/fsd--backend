package com.scholarship.platform.controller;

import com.scholarship.platform.dto.ScholarshipDto;
import com.scholarship.platform.service.ScholarshipService;
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
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;

    @GetMapping
    public ResponseEntity<Page<ScholarshipDto>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(scholarshipService.searchScholarships(query, category, page, size, email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipDto> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(scholarshipService.getById(id, email));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScholarshipDto> create(
            @Valid @RequestBody ScholarshipDto.ScholarshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scholarshipService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScholarshipDto> update(
            @PathVariable Long id,
            @RequestBody ScholarshipDto.ScholarshipRequest request) {
        return ResponseEntity.ok(scholarshipService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        scholarshipService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Scholarship deleted successfully"));
    }

    @PostMapping("/{id}/save")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> save(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        scholarshipService.saveScholarship(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Scholarship saved"));
    }

    @DeleteMapping("/{id}/save")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, String>> unsave(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        scholarshipService.unsaveScholarship(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Scholarship removed from saved"));
    }

    @GetMapping("/saved")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ScholarshipDto>> getSaved(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(scholarshipService.getSavedScholarships(userDetails.getUsername()));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(scholarshipService.getCategories());
    }
}
