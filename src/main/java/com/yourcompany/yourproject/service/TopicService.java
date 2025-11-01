package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.TopicRequestDto;
import com.yourcompany.yourproject.entity.Topic;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public Topic createTopic(TopicRequestDto topicRequestDto) {
        Topic topic = new Topic();
        topic.setName(topicRequestDto.getName());
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
    }

    public Topic updateTopic(Long id, TopicRequestDto topicRequestDto) {
        Topic topic = getTopicById(id);
        topic.setName(topicRequestDto.getName());
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        Topic topic = getTopicById(id);
        // Optional: Add logic here to check if the topic is associated with any groups before deleting.
        topicRepository.delete(topic);
    }
}

