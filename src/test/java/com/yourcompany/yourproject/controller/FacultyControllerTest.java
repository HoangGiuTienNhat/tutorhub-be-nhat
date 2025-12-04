package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.FacultyStudentDto;
import com.yourcompany.yourproject.service.FacultyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    private List<FacultyStudentDto> mockStudents;

    @BeforeEach
    public void setUp() {
        mockStudents = Arrays.asList(
            FacultyStudentDto.builder()
                .studentId(123123L)
                .studentName("Nguyễn Trọng Nhân")
                .email("nhan.nguyen04@hmcut.edu.vn")
                .groupId(5L)
                .groupName("Advanced Java Programming")
                .score(9)
                .feedback("Học tốt")
                .attendance(true)
                .build(),
            FacultyStudentDto.builder()
                .studentId(123124L)
                .studentName("Trần Văn B")
                .email("b.tran@hmcut.edu.vn")
                .groupId(5L)
                .groupName("Advanced Java Programming")
                .score(8)
                .feedback("Tham gia tích cực")
                .attendance(true)
                .build()
        );
    }

    @Test
    public void testGetFacultyStudents_Success() throws Exception {
        when(facultyService.getFacultyStudents(1L, null, null))
            .thenReturn(mockStudents);

        mockMvc.perform(get("/faculties/1/students")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].studentId", is(123123)))
            .andExpect(jsonPath("$[0].studentName", is("Nguyễn Trọng Nhân")))
            .andExpect(jsonPath("$[0].score", is(9)))
            .andExpect(jsonPath("$[1].studentId", is(123124)))
            .andExpect(jsonPath("$[1].score", is(8)));
    }

    @Test
    public void testGetFacultyStudents_WithGroupFilter() throws Exception {
        List<FacultyStudentDto> filteredStudents = mockStudents.subList(0, 1);
        when(facultyService.getFacultyStudents(1L, 5L, null))
            .thenReturn(filteredStudents);

        mockMvc.perform(get("/faculties/1/students")
                .param("groupId", "5")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].groupId", is(5)));
    }

    @Test
    public void testGetFacultyStudents_WithTutorFilter() throws Exception {
        when(facultyService.getFacultyStudents(1L, null, 10L))
            .thenReturn(mockStudents);

        mockMvc.perform(get("/faculties/1/students")
                .param("tutorId", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetFacultyStudents_WithBothFilters() throws Exception {
        when(facultyService.getFacultyStudents(1L, 5L, 10L))
            .thenReturn(mockStudents);

        mockMvc.perform(get("/faculties/1/students")
                .param("groupId", "5")
                .param("tutorId", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetFacultyStudents_EmptyResult() throws Exception {
        when(facultyService.getFacultyStudents(2L, null, null))
            .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/faculties/2/students")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}

