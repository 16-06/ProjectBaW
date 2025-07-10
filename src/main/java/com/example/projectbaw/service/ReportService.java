package com.example.projectbaw.service;


import com.example.projectbaw.enums.ResolutionStatus;
import com.example.projectbaw.mapper.ReportMapper;
import com.example.projectbaw.model.Report;
import com.example.projectbaw.model.User;
import com.example.projectbaw.payload.ReportDto;
import com.example.projectbaw.repository.ReportRepository;
import com.example.projectbaw.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;

    public List<ReportDto.ResponseDto> findAllReports() {

        List<Report> reports = reportRepository.findAll();

        return reports.stream()
                .map(reportMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ReportDto.ResponseDto findReportById(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if(report.getReporter().getId() != user.getId()) {
            throw new RuntimeException("You do not have permission to view this report");
        }

        return reportMapper.toResponseDto(report);

    }

    public ReportDto.ResponseDto createReport(ReportDto.RequestDto request, Long userId) {

        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = reportMapper.toEntity(request);
        report.setReporter(reporter);
        reportRepository.save(report);

        return reportMapper.toResponseDto(report);
    }

    public ReportDto.ResponseDto changeReportStatus(Long id, ResolutionStatus status) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(status);
        reportRepository.save(report);

        return reportMapper.toResponseDto(report);

    }
}
