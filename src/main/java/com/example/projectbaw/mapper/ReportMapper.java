package com.example.projectbaw.mapper;

import com.example.projectbaw.model.Report;
import com.example.projectbaw.payload.ReportDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "contentId", target = "contentId")
    @Mapping(source = "reason", target = "reason")
    @Mapping(source = "contentType", target = "contentType")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    ReportDto.ResponseDto toResponseDto(Report report);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Report toEntity(ReportDto.RequestDto dto);
}

