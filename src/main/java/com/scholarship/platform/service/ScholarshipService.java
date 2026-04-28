package com.scholarship.platform.service;

import com.scholarship.platform.dto.ScholarshipDto;
import com.scholarship.platform.entity.SavedScholarship;
import com.scholarship.platform.entity.Scholarship;
import com.scholarship.platform.entity.User;
import com.scholarship.platform.exception.BadRequestException;
import com.scholarship.platform.exception.ResourceNotFoundException;
import com.scholarship.platform.repository.SavedScholarshipRepository;
import com.scholarship.platform.repository.ScholarshipRepository;
import com.scholarship.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ScholarshipService {

    @Autowired private ScholarshipRepository scholarshipRepository;
    @Autowired private SavedScholarshipRepository savedScholarshipRepository;
    @Autowired private UserRepository userRepository;

    public Page<ScholarshipDto> searchScholarships(String query, String category, int page, int size, String email) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("deadline").ascending());
        Page<Scholarship> scholarships = scholarshipRepository.searchScholarships(
            (query == null || query.isBlank()) ? null : query,
            (category == null || category.isBlank()) ? null : category,
            pageable
        );

        Set<Long> savedIds = getSavedIds(email);
        return scholarships.map(s -> ScholarshipDto.from(s, savedIds.contains(s.getId())));
    }

    public ScholarshipDto getById(Long id, String email) {
        Scholarship scholarship = scholarshipRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Scholarship", id));
        Set<Long> savedIds = getSavedIds(email);
        return ScholarshipDto.from(scholarship, savedIds.contains(id));
    }

    @Transactional
    public ScholarshipDto create(ScholarshipDto.ScholarshipRequest request) {
        Scholarship scholarship = Scholarship.builder()
            .title(request.getTitle())
            .provider(request.getProvider())
            .category(request.getCategory())
            .amount(request.getAmount())
            .deadline(request.getDeadline())
            .description(request.getDescription())
            .eligibility(request.getEligibility())
            .gpaRequirement(request.getGpaRequirement())
            .seats(request.getSeats())
            .website(request.getWebsite())
            .renewable(request.getRenewable() != null ? request.getRenewable() : false)
            .renewalCriteria(request.getRenewalCriteria())
            .status(request.getStatus() != null ? request.getStatus() : Scholarship.ScholarshipStatus.ACTIVE)
            .build();
        return ScholarshipDto.from(scholarshipRepository.save(scholarship), false);
    }

    @Transactional
    public ScholarshipDto update(Long id, ScholarshipDto.ScholarshipRequest request) {
        Scholarship scholarship = scholarshipRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Scholarship", id));

        if (request.getTitle() != null) scholarship.setTitle(request.getTitle());
        if (request.getProvider() != null) scholarship.setProvider(request.getProvider());
        if (request.getCategory() != null) scholarship.setCategory(request.getCategory());
        if (request.getAmount() != null) scholarship.setAmount(request.getAmount());
        if (request.getDeadline() != null) scholarship.setDeadline(request.getDeadline());
        if (request.getDescription() != null) scholarship.setDescription(request.getDescription());
        if (request.getEligibility() != null) scholarship.setEligibility(request.getEligibility());
        if (request.getGpaRequirement() != null) scholarship.setGpaRequirement(request.getGpaRequirement());
        if (request.getSeats() != null) scholarship.setSeats(request.getSeats());
        if (request.getWebsite() != null) scholarship.setWebsite(request.getWebsite());
        if (request.getRenewable() != null) scholarship.setRenewable(request.getRenewable());
        if (request.getRenewalCriteria() != null) scholarship.setRenewalCriteria(request.getRenewalCriteria());
        if (request.getStatus() != null) scholarship.setStatus(request.getStatus());

        return ScholarshipDto.from(scholarshipRepository.save(scholarship), false);
    }

    @Transactional
    public void delete(Long id) {
        if (!scholarshipRepository.existsById(id)) {
            throw new ResourceNotFoundException("Scholarship", id);
        }
        scholarshipRepository.deleteById(id);
    }

    @Transactional
    public void saveScholarship(Long scholarshipId, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (savedScholarshipRepository.existsByUserAndScholarshipId(user, scholarshipId)) {
            throw new BadRequestException("Scholarship already saved");
        }
        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
            .orElseThrow(() -> new ResourceNotFoundException("Scholarship", scholarshipId));
        savedScholarshipRepository.save(
            SavedScholarship.builder().user(user).scholarship(scholarship).build()
        );
    }

    @Transactional
    public void unsaveScholarship(Long scholarshipId, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        savedScholarshipRepository.deleteByUserAndScholarshipId(user, scholarshipId);
    }

    public List<ScholarshipDto> getSavedScholarships(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return savedScholarshipRepository.findByUser(user).stream()
            .map(ss -> ScholarshipDto.from(ss.getScholarship(), true))
            .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        return scholarshipRepository.findAllCategories();
    }

    // ─── Helper ────────────────────────────────────────────────
    private Set<Long> getSavedIds(String email) {
        if (email == null) return Set.of();
        return userRepository.findByEmail(email)
            .map(user -> savedScholarshipRepository.findByUser(user).stream()
                .map(ss -> ss.getScholarship().getId())
                .collect(Collectors.toSet()))
            .orElse(Set.of());
    }
}
