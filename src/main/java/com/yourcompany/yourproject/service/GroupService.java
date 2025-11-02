package com.yourcompany.yourproject.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourcompany.yourproject.dto.FacultyResponseDto;
import com.yourcompany.yourproject.dto.GroupRequestDto;
import com.yourcompany.yourproject.dto.GroupResponseDto;
import com.yourcompany.yourproject.dto.TopicResponseDto;
import com.yourcompany.yourproject.dto.UserDto;
import com.yourcompany.yourproject.entity.Faculty;
import com.yourcompany.yourproject.entity.Group;
import com.yourcompany.yourproject.entity.Topic;
import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.repository.FacultyRepository;
import com.yourcompany.yourproject.repository.GroupRepository;
import com.yourcompany.yourproject.repository.TopicRepository;
import com.yourcompany.yourproject.repository.UserRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Transactional
    public GroupResponseDto createGroup(GroupRequestDto groupRequestDto) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        User tutor = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

        Set<Topic> topics = new HashSet<>(topicRepository.findAllById(groupRequestDto.getTopicIds()));
        if (topics.size() != groupRequestDto.getTopicIds().size()) {
            throw new ResourceNotFoundException("One or more topics not found.");
        }

        Faculty faculty = facultyRepository.findById(groupRequestDto.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Faculty not found with id: " + groupRequestDto.getFacultyId()));

        Group group = new Group();
        group.setGroupName(groupRequestDto.getGroupName());
        group.setDescription(groupRequestDto.getDescription());
        group.setStudentLimit(groupRequestDto.getStudentLimit());
        group.setStatus(groupRequestDto.getStatus());
        group.setStartDate(groupRequestDto.getStartDate());
        group.setEndDate(groupRequestDto.getEndDate());
        group.setTutor(tutor);
        group.setTopics(topics);
        group.setFaculty(faculty);

        Group savedGroup = groupRepository.save(group);
        return convertToDto(savedGroup);
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupResponseDto getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));
        return convertToDto(group);
    }

    @Transactional
    public GroupResponseDto updateGroup(Long id, GroupRequestDto groupRequestDto) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));

        Faculty faculty = facultyRepository.findById(groupRequestDto.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Faculty not found with id: " + groupRequestDto.getFacultyId()));

        Set<Topic> topics = new HashSet<>(topicRepository.findAllById(groupRequestDto.getTopicIds()));
        if (topics.size() != groupRequestDto.getTopicIds().size()) {
            throw new ResourceNotFoundException("One or more topics not found.");
        }

        // Update fields
        group.setGroupName(groupRequestDto.getGroupName());
        group.setDescription(groupRequestDto.getDescription());
        group.setStudentLimit(groupRequestDto.getStudentLimit());
        group.setStatus(groupRequestDto.getStatus());
        group.setStartDate(groupRequestDto.getStartDate());
        group.setEndDate(groupRequestDto.getEndDate());
        group.setFaculty(faculty);
        group.setTopics(topics);

        Group updatedGroup = groupRepository.save(group);
        return convertToDto(updatedGroup);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));
        groupRepository.delete(group);
    }

    private GroupResponseDto convertToDto(Group group) {
        GroupResponseDto groupResponseDto = new GroupResponseDto();
        groupResponseDto.setId(group.getId());
        groupResponseDto.setGroupName(group.getGroupName());
        groupResponseDto.setDescription(group.getDescription());
        groupResponseDto.setStudentLimit(group.getStudentLimit());
        groupResponseDto.setStatus(group.getStatus());
        groupResponseDto.setStartDate(group.getStartDate());
        groupResponseDto.setEndDate(group.getEndDate());

        // Convert associated entities to DTOs
        User tutor = group.getTutor();
        groupResponseDto.setTutor(new UserDto(tutor.getUid(), tutor.getUserName(), tutor.getEmail(), tutor.getRole(),
                tutor.getPersonalEmail(), tutor.getPhoneNumber(), tutor.getAddress()));

        Faculty faculty = group.getFaculty();
        groupResponseDto.setFaculty(new FacultyResponseDto(faculty.getId(), faculty.getName()));

        Set<TopicResponseDto> topicDtos = group.getTopics().stream()
                .map(topic -> new TopicResponseDto(topic.getId(), topic.getName()))
                .collect(Collectors.toSet());
        groupResponseDto.setTopics(topicDtos);

        return groupResponseDto;
    }
}
