package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.StatisticDto;
import com.yourcompany.yourproject.dto.StatisticFilterDto;
import com.yourcompany.yourproject.dto.UserStatisticDto;
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

    /**
     * Get student statistics with optional filtering
     *
     * @param facultyId Faculty ID (optional)
     * @param topicId   Topic ID (optional)
     * @param tutorId   Tutor ID (optional)
     * @param startDate Start date (optional)
     * @param endDate   End date (optional)
     * @return List of student statistics
     */
    @GetMapping("/by-student")
    public ResponseEntity<List<UserStatisticDto>> getStudentStatistics(
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) Long tutorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        StatisticFilterDto filter = new StatisticFilterDto();
        filter.setFacultyId(facultyId);
        filter.setTopicId(topicId);
        filter.setTutorId(tutorId);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        List<UserStatisticDto> statistics = statisticService.getStudentStatistics(filter);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get tutor statistics with optional filtering
     *
     * @param facultyId Faculty ID (optional)
     * @param topicId   Topic ID (optional)
     * @param studentId Student ID (optional)
     * @param startDate Start date (optional)
     * @param endDate   End date (optional)
     * @return List of tutor statistics
     */
    @GetMapping("/by-tutor")
    public ResponseEntity<List<UserStatisticDto>> getTutorStatistics(
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        StatisticFilterDto filter = new StatisticFilterDto();
        filter.setFacultyId(facultyId);
        filter.setTopicId(topicId);
        filter.setStudentId(studentId);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        List<UserStatisticDto> statistics = statisticService.getTutorStatistics(filter);
        return ResponseEntity.ok(statistics);
    }
}
