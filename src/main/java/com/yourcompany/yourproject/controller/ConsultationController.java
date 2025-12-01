package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.ConsultationRequestDto;
import com.yourcompany.yourproject.dto.ConsultationResponseDto;
import com.yourcompany.yourproject.dto.ConsultationMemberReviewDto;
import com.yourcompany.yourproject.dto.StudentReviewListRequestDto;
import com.yourcompany.yourproject.dto.StudentReviewResponseDto;
import com.yourcompany.yourproject.service.ConsultationService;
import com.yourcompany.yourproject.service.StudentReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private StudentReviewService studentReviewService;

    @PostMapping
    public ResponseEntity<ConsultationResponseDto> createConsultation(
            @Valid @RequestBody ConsultationRequestDto consultationRequestDto) {
        log.info("ConsultationController.createConsultation called with: {}", consultationRequestDto);
        ConsultationResponseDto createdConsultation = consultationService.createConsultation(consultationRequestDto);
        log.info("Consultation created successfully: {}", createdConsultation);
        return new ResponseEntity<>(createdConsultation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseDto> getConsultationById(@PathVariable Long id) {
        log.info("ConsultationController.getConsultationById called with id: {}", id);
        ConsultationResponseDto consultation = consultationService.getConsultationById(id);
        return ResponseEntity.ok(consultation);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ConsultationResponseDto>> getConsultationsByGroupId(@PathVariable Long groupId) {
        log.info("ConsultationController.getConsultationsByGroupId called with groupId: {}", groupId);
        List<ConsultationResponseDto> consultations = consultationService.getConsultationsByGroupId(groupId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping
    public ResponseEntity<List<ConsultationResponseDto>> getAllConsultations() {
        log.info("ConsultationController.getAllConsultations called");
        List<ConsultationResponseDto> consultations = consultationService.getAllConsultations();
        return ResponseEntity.ok(consultations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponseDto> updateConsultation(@PathVariable Long id,
            @Valid @RequestBody ConsultationRequestDto consultationRequestDto) {
        log.info("ConsultationController.updateConsultation called with id: {} and data: {}", id,
                consultationRequestDto);
        ConsultationResponseDto updatedConsultation = consultationService.updateConsultation(id,
                consultationRequestDto);
        return ResponseEntity.ok(updatedConsultation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        log.info("ConsultationController.deleteConsultation called with id: {}", id);
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Void> registerForConsultation(@PathVariable Long id) {
        log.info("Registering for consultation with id: {}", id);
        consultationService.registerForConsultation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unregister")
    public ResponseEntity<Void> unregisterFromConsultation(@PathVariable Long id) {
        log.info("Unregistering from consultation with id: {}", id);
        consultationService.unregisterFromConsultation(id);
        return ResponseEntity.ok().build();
    }

    // Student Review Endpoints

    @PostMapping("/{consultationId}/reviews")
    public ResponseEntity<List<StudentReviewResponseDto>> createOrUpdateReviews(
            @PathVariable Long consultationId,
            @Valid @RequestBody StudentReviewListRequestDto requestDto) {
        List<StudentReviewResponseDto> reviews = studentReviewService.createOrUpdateReviews(consultationId, requestDto);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{consultationId}/reviews")
    public ResponseEntity<List<StudentReviewResponseDto>> getReviewsForConsultation(@PathVariable Long consultationId) {
        List<StudentReviewResponseDto> reviews = studentReviewService.getReviewsForConsultation(consultationId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{consultationId}/reviewable-students")
    public ResponseEntity<List<ConsultationMemberReviewDto>> getReviewableStudents(@PathVariable Long consultationId) {
        List<ConsultationMemberReviewDto> students = studentReviewService.getReviewableStudents(consultationId);
        return ResponseEntity.ok(students);
    }
}
