package com.example.projectbaw.controller;

import com.example.projectbaw.enums.ResolutionStatus;
import com.example.projectbaw.payload.ReportDto;
import com.example.projectbaw.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/user/create")
    public ResponseEntity<?> createReport(ReportDto.RequestDto request, Long userId) {

        ReportDto.ResponseDto response = reportService.createReport(request, userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    @GetMapping("/moderator/reports")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(reportService.findAllReports());
    }

    @GetMapping("/user/checkStatus")
    public ResponseEntity<?> checkReportStatus(Long reportId) {

        ReportDto.ResponseDto response = reportService.findReportById(reportId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    @PostMapping("/moderator/changeStatus")
    public ResponseEntity<?> changeReportStatus(Long reportId, ResolutionStatus status) {

        return ResponseEntity.ok(reportService.changeReportStatus(reportId, status));
    }
}
