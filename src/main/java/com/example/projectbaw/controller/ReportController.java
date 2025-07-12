package com.example.projectbaw.controller;

import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.enums.ResolutionStatus;
import com.example.projectbaw.payload.ReportDto;
import com.example.projectbaw.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/user/create")
    public ResponseEntity<?> createReport(@RequestBody ReportDto.RequestDto request,@AuthenticationPrincipal CustomUserDetails userDetails) {

        ReportDto.ResponseDto response = reportService.createReport(request,userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/reports")
    public ResponseEntity<?> getUserReports(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportService.findUserReports(userDetails));
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    @GetMapping("/moderator/reports")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(reportService.findAllReports());
    }

    @GetMapping("/user/checkStatus")
    public ResponseEntity<?> checkReportStatus(@RequestBody Long reportId,@AuthenticationPrincipal CustomUserDetails userDetails) {

        ReportDto.ResponseDto response = reportService.findReportById(reportId,userDetails);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','ADMIN')")
    @PostMapping("/moderator/changeStatus")
    public ResponseEntity<?> changeReportStatus(@RequestBody ReportDto.ChangeStatusDto changeStatusDto) {

        return ResponseEntity.ok(reportService.changeReportStatus(changeStatusDto));
    }

}
