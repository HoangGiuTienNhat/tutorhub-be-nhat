package com.yourcompany.yourproject.controller;

import com.yourcompany.yourproject.dto.TopicRequestDto;
import com.yourcompany.yourproject.dto.TopicResponseDto;
import com.yourcompany.yourproject.entity.Topic;
import com.yourcompany.yourproject.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    // Helper method to convert Entity to DTO
    private TopicResponseDto convertToDto(Topic topic) {
        return new TopicResponseDto(topic.getId(), topic.getName());
    }

    @PostMapping
    public ResponseEntity<TopicResponseDto> createTopic(@Valid @RequestBody TopicRequestDto topicRequestDto) {
        Topic createdTopic = topicService.createTopic(topicRequestDto);
        return new ResponseEntity<>(convertToDto(createdTopic), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> getAllTopics() {
        List<TopicResponseDto> topics = topicService.getAllTopics().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDto> getTopicById(@PathVariable Long id) {
        Topic topic = topicService.getTopicById(id);
        return ResponseEntity.ok(convertToDto(topic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponseDto> updateTopic(@PathVariable Long id,
            @Valid @RequestBody TopicRequestDto topicRequestDto) {
        Topic updatedTopic = topicService.updateTopic(id, topicRequestDto);
        return ResponseEntity.ok(convertToDto(updatedTopic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
