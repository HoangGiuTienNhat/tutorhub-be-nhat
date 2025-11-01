package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.FacultyRequestDto;
import com.yourcompany.yourproject.entity.Faculty;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

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
        // Optional: Add logic here to check if the faculty is associated with any groups before deleting.
        facultyRepository.delete(faculty);
    }
}

