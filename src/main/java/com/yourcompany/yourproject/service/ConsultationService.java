package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.ConsultationRequestDto;
import com.yourcompany.yourproject.dto.ConsultationResponseDto;
import com.yourcompany.yourproject.entity.Consultation;
import com.yourcompany.yourproject.entity.Group;
import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.exception.UnauthorizedException;
import com.yourcompany.yourproject.repository.ConsultationRepository;
import com.yourcompany.yourproject.repository.GroupRepository;
import com.yourcompany.yourproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConsultationService {

        @Autowired
        private ConsultationRepository consultationRepository;

        @Autowired
        private GroupRepository groupRepository;

        @Autowired
        private UserRepository userRepository;

        @Transactional
        public ConsultationResponseDto createConsultation(ConsultationRequestDto consultationRequestDto) {
                log.info("Creating consultation for group: {}", consultationRequestDto.getGroupId());

                Group group = groupRepository.findById(consultationRequestDto.getGroupId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Group not found with id: " + consultationRequestDto.getGroupId()));

                Consultation consultation = new Consultation();
                consultation.setTopic(consultationRequestDto.getTopic());
                consultation.setDescription(consultationRequestDto.getDescription());
                consultation.setConsultationDate(consultationRequestDto.getConsultationDate());
                consultation.setConsultationTime(consultationRequestDto.getConsultationTime());
                consultation.setType(consultationRequestDto.getType());
                consultation.setLocationLink(consultationRequestDto.getLocationLink());
                consultation.setStatus(consultationRequestDto.getStatus() != null ? consultationRequestDto.getStatus()
                                : "SCHEDULED");
                consultation.setGroup(group);

                Consultation savedConsultation = consultationRepository.save(consultation);
                log.info("Consultation created successfully with id: {}", savedConsultation.getId());
                return convertToDto(savedConsultation);
        }

        @Transactional(readOnly = true)
        public ConsultationResponseDto getConsultationById(Long id) {
                Consultation consultation = consultationRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + id));
                return convertToDto(consultation);
        }

        @Transactional(readOnly = true)
        public List<ConsultationResponseDto> getConsultationsByGroupId(Long groupId) {
                log.info("Fetching consultations for group: {}", groupId);
                return consultationRepository.findByGroupId(groupId).stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ConsultationResponseDto> getAllConsultations() {
                return consultationRepository.findAll().stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
        }

        @Transactional
        public ConsultationResponseDto updateConsultation(Long id, ConsultationRequestDto consultationRequestDto) {
                log.info("Updating consultation with id: {}", id);

                Consultation consultation = consultationRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + id));

                Group group = groupRepository.findById(consultationRequestDto.getGroupId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Group not found with id: " + consultationRequestDto.getGroupId()));

                consultation.setTopic(consultationRequestDto.getTopic());
                consultation.setDescription(consultationRequestDto.getDescription());
                consultation.setConsultationDate(consultationRequestDto.getConsultationDate());
                consultation.setConsultationTime(consultationRequestDto.getConsultationTime());
                consultation.setType(consultationRequestDto.getType());
                consultation.setLocationLink(consultationRequestDto.getLocationLink());
                consultation.setStatus(consultationRequestDto.getStatus() != null ? consultationRequestDto.getStatus()
                                : consultation.getStatus());
                consultation.setGroup(group);

                Consultation updatedConsultation = consultationRepository.save(consultation);
                log.info("Consultation updated successfully with id: {}", updatedConsultation.getId());
                return convertToDto(updatedConsultation);
        }

        @Transactional
        public void deleteConsultation(Long id) {
                log.info("Deleting consultation with id: {}", id);

                Consultation consultation = consultationRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + id));

                consultationRepository.delete(consultation);
                log.info("Consultation deleted successfully with id: {}", id);
        }

        @Transactional
        public void registerForConsultation(Long consultationId) {
                User user = getCurrentUser();
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + consultationId));

                // Business rule: User must be part of the group to register for its
                // consultations
                if (!user.getJoinedGroups().contains(consultation.getGroup())) {
                        throw new UnauthorizedException(
                                        "User must be a member of the group to register for this consultation.");
                }

                consultation.getRegisteredUsers().add(user);
                user.getRegisteredConsultations().add(consultation);

                consultationRepository.save(consultation);
        }

        @Transactional
        public void unregisterFromConsultation(Long consultationId) {
                User user = getCurrentUser();
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Consultation not found with id: " + consultationId));

                consultation.getRegisteredUsers().remove(user);
                user.getRegisteredConsultations().remove(consultation);

                consultationRepository.save(consultation);
        }

        private User getCurrentUser() {
                String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .getUsername();
                return userRepository.findByEmail(username)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with email: " + username));
        }

        private ConsultationResponseDto convertToDto(Consultation consultation) {
                return new ConsultationResponseDto(
                                consultation.getId(),
                                consultation.getTopic(),
                                consultation.getDescription(),
                                consultation.getConsultationDate(),
                                consultation.getConsultationTime(),
                                consultation.getType(),
                                consultation.getLocationLink(),
                                consultation.getStatus(),
                                consultation.getGroup().getId(),
                                consultation.getGroup().getGroupName());
        }
}
