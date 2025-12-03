package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.FacultyRequestDto;
import com.yourcompany.yourproject.dto.FacultyResponseDto;
import com.yourcompany.yourproject.dto.FacultyStudentDto;
import com.yourcompany.yourproject.entity.Faculty;
import com.yourcompany.yourproject.service.FacultyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/faculties")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    // Helper method to convert Entity to DTO
    private FacultyResponseDto convertToDto(Faculty faculty) {
        return new FacultyResponseDto(faculty.getId(), faculty.getName());
    }

    @PostMapping
    public ResponseEntity<FacultyResponseDto> createFaculty(@Valid @RequestBody FacultyRequestDto facultyRequestDto) {
        log.info("FacultyController.createFaculty called with: {}", facultyRequestDto);
        Faculty createdFaculty = facultyService.createFaculty(facultyRequestDto);
        log.info("Faculty created successfully: {}", createdFaculty);
        return new ResponseEntity<>(convertToDto(createdFaculty), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FacultyResponseDto>> getAllFaculties() {
        List<FacultyResponseDto> faculties = facultyService.getAllFaculties().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> getFacultyById(@PathVariable Long id) {
        Faculty faculty = facultyService.getFacultyById(id);
        return ResponseEntity.ok(convertToDto(faculty));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> updateFaculty(@PathVariable Long id,
            @Valid @RequestBody FacultyRequestDto facultyRequestDto) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, facultyRequestDto);
        return ResponseEntity.ok(convertToDto(updatedFaculty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get list of students in a faculty with optional filtering
     *
     * @param facultyId Faculty ID
     * @param groupId   Optional group ID for filtering
     * @param tutorId   Optional tutor ID for filtering
     * @return List of students with their review information
     */
    @GetMapping("/{id}/students")
    public ResponseEntity<List<FacultyStudentDto>> getFacultyStudents(
            @PathVariable("id") Long facultyId,
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) Long tutorId) {
        log.info("FacultyController.getFacultyStudents called with facultyId: {}, groupId: {}, tutorId: {}",
                facultyId, groupId, tutorId);
        List<FacultyStudentDto> students = facultyService.getFacultyStudents(facultyId, groupId, tutorId);
        log.info("Retrieved {} students for faculty: {}", students.size(), facultyId);
        return ResponseEntity.ok(students);
    }
}
