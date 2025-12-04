package com.yourcompany.yourproject.service;

import com.yourcompany.yourproject.dto.FacultyResponseDto;
import com.yourcompany.yourproject.dto.RegisterRequest;
import com.yourcompany.yourproject.dto.UpdateProfileRequest;
import com.yourcompany.yourproject.dto.UserDto;
import com.yourcompany.yourproject.entity.Faculty;
import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.exception.ResourceNotFoundException;
import com.yourcompany.yourproject.repository.FacultyRepository;
import com.yourcompany.yourproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDto(user);
    }

    public UserDto createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto register(RegisterRequest request) {
        // Since UID is mandatory, we check for its existence directly.
        if (userRepository.existsById(request.getUid())) {
            throw new IllegalArgumentException("Error: User ID is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = new User();
        user.setUid(request.getUid()); // UID is now mandatory and set directly.
        user.setEmail(request.getEmail());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "STUDENT");

        if (request.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(request.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Faculty not found with id: " + request.getFacultyId()));
            user.setFaculty(faculty);
        }

        User saved = userRepository.save(user);
        return convertToDto(saved);
    }

    public UserDto updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setUserName(userDetails.getUserName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setFaculty(userDetails.getFaculty());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private UserDto convertToDto(User user) {
        FacultyResponseDto facultyDto = null;
        if (user.getFaculty() != null) {
            facultyDto = new FacultyResponseDto(user.getFaculty().getId(), user.getFaculty().getName());
        }
        return new UserDto(
                user.getUid(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getPersonalEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                facultyDto);
    }

    public UserDto updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Update only the allowed fields
        if (request.getPersonalEmail() != null && !request.getPersonalEmail().isEmpty()) {
            user.setPersonalEmail(request.getPersonalEmail());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            user.setAddress(request.getAddress());
        }

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }
}
