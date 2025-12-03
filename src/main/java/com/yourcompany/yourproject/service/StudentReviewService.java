package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.*;
import com.yourcompany.yourproject.entity.Consultation;
import com.yourcompany.yourproject.entity.StudentReview;
import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.exception.UnauthorizedException;
import com.yourcompany.yourproject.repository.ConsultationRepository;
import com.yourcompany.yourproject.repository.StudentReviewRepository;
import com.yourcompany.yourproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentReviewService {

        private final StudentReviewRepository studentReviewRepository;
        private final ConsultationRepository consultationRepository;
        private final UserRepository userRepository;

        @Transactional
        public List<StudentReviewResponseDto> createOrUpdateReviews(Long consultationId,
                        StudentReviewListRequestDto requestDto) {
                User tutor = getCurrentUser();
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + consultationId));

                // Authorization: Check if the current user is the tutor of the group
                if (!consultation.getGroup().getTutor().getUid().equals(tutor.getUid())) {
                        throw new UnauthorizedException("You are not the tutor for this consultation's group.");
                }

                Set<Long> registeredStudentIds = consultation.getRegisteredUsers().stream().map(User::getUid)
                                .collect(Collectors.toSet());
                List<StudentReview> reviewsToSave = new ArrayList<>();

                for (StudentReviewItemDto item : requestDto.getReviews()) {
                        if (!registeredStudentIds.contains(item.getStudentId())) {
                                throw new UnauthorizedException(
                                                "Student with id " + item.getStudentId()
                                                                + " is not registered for this consultation.");
                        }

                        User student = userRepository.findById(item.getStudentId())
                                        .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                        "Student not found with id: "
                                                                                        + item.getStudentId()));

                        StudentReview review = studentReviewRepository
                                        .findByConsultationIdAndStudentUid(consultationId, item.getStudentId())
                                        .orElse(new StudentReview());

                        review.setConsultation(consultation);
                        review.setTutor(tutor);
                        review.setStudent(student);
                        review.setAttendance(item.getAttendance());
                        review.setScore(item.getScore());
                        review.setFeedback(item.getFeedback());

                        reviewsToSave.add(review);
                }

                List<StudentReview> savedReviews = studentReviewRepository.saveAll(reviewsToSave);
                return savedReviews.stream().map(this::convertToDto).collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ConsultationMemberReviewDto> getReviewableStudents(Long consultationId) {
                log.info("[StudentReviewService] getReviewableStudents(consultationId={})", consultationId);
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + consultationId));

                // Get all existing reviews for this consultation and map them by student ID for
                // quick lookup
                List<StudentReview> reviewList = studentReviewRepository.findByConsultationId(consultationId);
                log.info("Found {} existing reviews for this consultation", reviewList.size());
                Map<Long, StudentReview> reviewsMap = reviewList.stream()
                                .filter(r -> {
                                        boolean ok = r.getStudent() != null && r.getStudent().getUid() != null;
                                        if (!ok) {
                                                log.warn("Ignoring review id={} because student or student.uid is null",
                                                                r.getId());
                                        }
                                        return ok;
                                })
                                .collect(Collectors.toMap(review -> review.getStudent().getUid(), Function.identity(),
                                                (existing, replacement) -> existing));

                // Get all registered students for the consultation
                Set<User> registeredStudents = consultation.getRegisteredUsers();
                log.info("Found {} registered students", registeredStudents.size());

                // Build the response DTO
                List<ConsultationMemberReviewDto> result = registeredStudents.stream().map(student -> {
                        StudentReview existingReview = reviewsMap.get(student.getUid());
                        return ConsultationMemberReviewDto.builder()
                                        .studentId(student.getUid())
                                        .studentName(student.getUserName())
                                        .studentEmail(student.getEmail())
                                        .attendance(existingReview != null ? existingReview.isAttendance() : null)
                                        .score(existingReview != null ? existingReview.getScore() : null)
                                        .feedback(existingReview != null ? existingReview.getFeedback() : null)
                                        .build();
                }).collect(Collectors.toList());
                log.info("Returning {} reviewable students", result.size());
                return result;
        }

        @Transactional(readOnly = true)
        public List<StudentReviewResponseDto> getReviewsForConsultation(Long consultationId) {
                if (!consultationRepository.existsById(consultationId)) {
                        throw new ResourceNotFoundException("Consultation not found with id: " + consultationId);
                }
                return studentReviewRepository.findByConsultationId(consultationId).stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
        }

        private User getCurrentUser() {
                String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .getUsername();
                return userRepository.findByEmail(username)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with email: " + username));
        }

        private StudentReviewResponseDto convertToDto(StudentReview review) {
                User student = review.getStudent();
                UserDto studentDto = new UserDto(
                                student.getUid(),
                                student.getUserName(),
                                student.getEmail(),
                                student.getRole(),
                                student.getPersonalEmail(),
                                student.getPhoneNumber(),
                                student.getAddress(),
                                student.getFaculty() != null ? new com.yourcompany.yourproject.dto.FacultyResponseDto(
                                                student.getFaculty().getId(), student.getFaculty().getName()) : null);

                return StudentReviewResponseDto.builder()
                                .id(review.getId())
                                .consultationId(review.getConsultation().getId())
                                .tutorId(review.getTutor().getUid())
                                .student(studentDto)
                                .attendance(review.isAttendance())
                                .score(review.getScore())
                                .feedback(review.getFeedback())
                                .lastUpdatedAt(review.getLastUpdatedAt())
                                .build();
        }
}
