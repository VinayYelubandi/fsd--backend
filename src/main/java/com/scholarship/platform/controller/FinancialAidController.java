package com.scholarship.platform.controller;

import com.scholarship.platform.dto.FinancialAidDto;
import com.scholarship.platform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-aids")
public class FinancialAidController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<FinancialAidDto>> getAll() {
        return ResponseEntity.ok(adminService.getAllFinancialAids());
    }
}
