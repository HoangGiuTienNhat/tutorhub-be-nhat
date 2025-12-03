package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.StatisticDto;
import com.yourcompany.yourproject.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/by-faculty")
    public ResponseEntity<List<StatisticDto>> getFacultyStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StatisticDto> statistics = statisticService.getFacultyStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/by-topic")
    public ResponseEntity<List<StatisticDto>> getTopicStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StatisticDto> statistics = statisticService.getTopicStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
}

