package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.FeedbackRequestDto;
import com.yourcompany.yourproject.dto.FeedbackResponseDto;
import com.yourcompany.yourproject.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponseDto> createFeedback(@Valid @RequestBody FeedbackRequestDto requestDto) {
        FeedbackResponseDto createdFeedback = feedbackService.createFeedback(requestDto);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackForConsultation(@PathVariable Long consultationId) {
        List<FeedbackResponseDto> feedbacks = feedbackService.getFeedbackForConsultation(consultationId);
        return ResponseEntity.ok(feedbacks);
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<FeedbackResponseDto> updateFeedback(@PathVariable Long feedbackId, @Valid @RequestBody FeedbackRequestDto requestDto) {
        FeedbackResponseDto updatedFeedback = feedbackService.updateFeedback(feedbackId, requestDto);
        return ResponseEntity.ok(updatedFeedback);
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }
}

