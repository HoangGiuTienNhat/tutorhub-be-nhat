package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.FacultyRequestDto;
import com.yourcompany.yourproject.dto.FacultyStudentDto;
import com.yourcompany.yourproject.entity.Faculty;
import com.yourcompany.yourproject.entity.Group;
import com.yourcompany.yourproject.entity.StudentReview;

import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.repository.FacultyRepository;
import com.yourcompany.yourproject.repository.StudentReviewRepository;
import com.yourcompany.yourproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentReviewRepository studentReviewRepository; // kept for other features

    @Autowired
    private UserRepository userRepository;

    public Faculty createFaculty(FacultyRequestDto facultyRequestDto) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyRequestDto.getName());
        return facultyRepository.save(faculty);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
    }

    public Faculty updateFaculty(Long id, FacultyRequestDto facultyRequestDto) {
        Faculty faculty = getFacultyById(id);
        faculty.setName(facultyRequestDto.getName());
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        Faculty faculty = getFacultyById(id);
        // Optional: Add logic here to check if the faculty is associated with any
        // groups before deleting.
        facultyRepository.delete(faculty);
    }

    /**
     * Get list of students in a faculty with optional filtering by group or tutor
     *
     * @param facultyId Faculty ID
     * @param groupId   Optional group ID for filtering
     * @param tutorId   Optional tutor ID for filtering
     * @return List of FacultyStudentDto
     */
    public List<FacultyStudentDto> getFacultyStudents(Long facultyId, Long groupId, Long tutorId) {
        // Verify faculty exists
        getFacultyById(facultyId);

        // If no group/tutor filter is provided, fetch students directly by user.faculty
        if (groupId == null && tutorId == null) {
            return userRepository.findStudentsByFaculty(facultyId).stream()
                    .map(u -> FacultyStudentDto.builder()
                            .studentId(u.getUid())
                            .studentName(u.getUserName())
                            .email(u.getEmail())
                            .groupId(null)
                            .groupName(null)
                            .score(null)
                            .feedback(null)
                            .attendance(false)
                            .build())
                    .collect(Collectors.toList());
        }

        List<StudentReview> studentReviews;

        // Get student reviews based on filter criteria (legacy path for advanced
        // filters)
        if (tutorId != null) {
            studentReviews = studentReviewRepository.findByFacultyIdAndTutorId(facultyId, tutorId);
        } else {
            studentReviews = studentReviewRepository.findByFacultyId(facultyId);
        }

        // Filter by group if specified
        if (groupId != null) {
            studentReviews = studentReviews.stream()
                    .filter(sr -> sr.getConsultation().getGroup().getId().equals(groupId))
                    .collect(Collectors.toList());
        }

        // Convert to DTO and remove duplicates (same student might have multiple
        // reviews)
        Map<Long, FacultyStudentDto> studentMap = studentReviews.stream()
                .collect(Collectors.toMap(
                        sr -> sr.getStudent().getUid(),
                        this::convertToFacultyStudentDto,
                        (existing, replacement) -> {
                            if (replacement.getScore() != null &&
                                    (existing.getScore() == null || replacement.getScore() > existing.getScore())) {
                                return replacement;
                            }
                            return existing;
                        }));

        return studentMap.values().stream().collect(Collectors.toList());
    }

    private FacultyStudentDto convertToFacultyStudentDto(StudentReview studentReview) {
        Group group = studentReview.getConsultation().getGroup();
        return FacultyStudentDto.builder()
                .studentId(studentReview.getStudent().getUid())
                .studentName(studentReview.getStudent().getUserName())
                .email(studentReview.getStudent().getEmail())
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .score(studentReview.getScore())
                .feedback(studentReview.getFeedback())
                .attendance(studentReview.isAttendance())
                .build();
    }
}
