package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.StatisticDto;
import com.yourcompany.yourproject.dto.StatisticFilterDto;
import com.yourcompany.yourproject.dto.UserStatisticDto;
import com.yourcompany.yourproject.entity.Faculty;
import com.yourcompany.yourproject.entity.Topic;
import com.yourcompany.yourproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticService {

    private final FacultyRepository facultyRepository;
    private final TopicRepository topicRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ConsultationRepository consultationRepository;

    public List<StatisticDto> getFacultyStatistics(LocalDate startDate, LocalDate endDate) {
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream().map(faculty -> {
            long facultyId = faculty.getId();
            long tutorCount = groupRepository.countDistinctTutorsByFaculty(facultyId);
            long studentCount = userRepository.countByFaculty_IdAndRole(facultyId, "STUDENT");
            long groupCount = groupRepository.countByFaculty_Id(facultyId);
            long consultationCount = consultationRepository.countByFaculty(facultyId, startDate, endDate);
            return new StatisticDto(facultyId, faculty.getName(), tutorCount, studentCount, groupCount,
                    consultationCount);
        }).collect(Collectors.toList());
    }

    public List<StatisticDto> getTopicStatistics(LocalDate startDate, LocalDate endDate) {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(topic -> {
            long topicId = topic.getId();
            long tutorCount = groupRepository.countDistinctTutorsByTopic(topicId);
            long studentCount = userRepository.countStudentsByTopic(topicId);
            long groupCount = groupRepository.countByTopics_Id(topicId);
            long consultationCount = consultationRepository.countByTopic(topicId, startDate, endDate);
            return new StatisticDto(topicId, topic.getName(), tutorCount, studentCount, groupCount, consultationCount);
        }).collect(Collectors.toList());
    }

    public List<UserStatisticDto> getStudentStatistics(StatisticFilterDto filter) {
        return userRepository.getStudentStatistics(
                filter.getFacultyId(),
                filter.getTopicId(),
                filter.getTutorId(),
                filter.getStartDate(),
                filter.getEndDate());
    }

    public List<UserStatisticDto> getTutorStatistics(StatisticFilterDto filter) {
        return userRepository.getTutorStatistics(
                filter.getFacultyId(),
                filter.getTopicId(),
                filter.getStudentId(),
                filter.getStartDate(),
                filter.getEndDate());
    }
}
