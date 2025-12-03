package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.FeedbackRequestDto;
import com.yourcompany.yourproject.dto.FeedbackResponseDto;
import com.yourcompany.yourproject.dto.UserDto;
import com.yourcompany.yourproject.entity.Consultation;
import com.yourcompany.yourproject.entity.Feedback;
import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.exception.UnauthorizedException;
import com.yourcompany.yourproject.repository.ConsultationRepository;
import com.yourcompany.yourproject.repository.FeedbackRepository;
import com.yourcompany.yourproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ConsultationRepository consultationRepository;

    @Transactional
    public FeedbackResponseDto createFeedback(FeedbackRequestDto requestDto) {
        User user = getCurrentUser();
        Consultation consultation = consultationRepository.findById(requestDto.getConsultationId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

        // Check if user is registered for the consultation
        if (!consultation.getRegisteredUsers().contains(user)) {
            throw new UnauthorizedException("User is not registered for this consultation.");
        }

        Feedback feedback = Feedback.builder()
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .consultation(consultation)
                .user(user)
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToDto(savedFeedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponseDto> getFeedbackForConsultation(Long consultationId) {
        if (!consultationRepository.existsById(consultationId)) {
            throw new ResourceNotFoundException("Consultation not found");
        }
        List<Feedback> feedbacks = feedbackRepository.findByConsultationId(consultationId);
        return feedbacks.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public FeedbackResponseDto updateFeedback(Long feedbackId, FeedbackRequestDto requestDto) {
        User user = getCurrentUser();
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));

        if (!feedback.getUser().getUid().equals(user.getUid())) {
            throw new UnauthorizedException("User is not authorized to update this feedback.");
        }

        feedback.setRating(requestDto.getRating());
        feedback.setComment(requestDto.getComment());

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return convertToDto(updatedFeedback);
    }

    @Transactional
    public void deleteFeedback(Long feedbackId) {
        User user = getCurrentUser();
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));

        if (!feedback.getUser().getUid().equals(user.getUid())) {
            throw new UnauthorizedException("User is not authorized to delete this feedback.");
        }

        feedbackRepository.delete(feedback);
    }

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
    }

    private FeedbackResponseDto convertToDto(Feedback feedback) {
        User user = feedback.getUser();
        UserDto userDto = new UserDto(
                user.getUid(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getPersonalEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getFaculty() != null
                        ? new com.yourcompany.yourproject.dto.FacultyResponseDto(user.getFaculty().getId(),
                                user.getFaculty().getName())
                        : null);
        return new FeedbackResponseDto(
                feedback.getId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getConsultation().getId(),
                userDto,
                feedback.getCreatedAt());
    }
}
