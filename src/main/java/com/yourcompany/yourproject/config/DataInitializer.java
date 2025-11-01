package com.yourcompany.yourproject.config;

import com.yourcompany.yourproject.entity.User;
import com.yourcompany.yourproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if test user already exists
        if (!userRepository.existsByEmail("test@example.com")) {
            User testUser = User.builder()
                    .email("test@example.com")
                    .userName("testuser")
                    .password(passwordEncoder.encode("password123"))
                    .role("USER")
                    .build();

            userRepository.save(testUser);
            log.info("Test user created: test@example.com with password: password123");
        } else {
            log.info("Test user already exists: test@example.com");
        }

        // Create admin user
        if (!userRepository.existsByEmail("admin@tutorhub.com")) {
            User adminUser = User.builder()
                    .email("admin@tutorhub.com")
                    .userName("Administrator")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .build();

            userRepository.save(adminUser);
            log.info("Admin user created: admin@tutorhub.com with password: admin123");
        }

        // Create student user
        if (!userRepository.existsByEmail("student@tutorhub.com")) {
            User studentUser = User.builder()
                    .email("student@tutorhub.com")
                    .userName("Nguyễn Văn A")
                    .password(passwordEncoder.encode("student123"))
                    .role("STUDENT")
                    .build();

            userRepository.save(studentUser);
            log.info("Student user created: student@tutorhub.com with password: student123");
        }

        // Create tutor user
        if (!userRepository.existsByEmail("tutor@tutorhub.com")) {
            User tutorUser = User.builder()
                    .email("tutor@tutorhub.com")
                    .userName("Trần Thị B")
                    .password(passwordEncoder.encode("tutor123"))
                    .role("TUTOR")
                    .build();

            userRepository.save(tutorUser);
            log.info("Tutor user created: tutor@tutorhub.com with password: tutor123");
        }

        // Create HCMUT student user
        if (!userRepository.existsByEmail("student@hcmut.edu.vn")) {
            User hcmutStudent = User.builder()
                    .email("student@hcmut.edu.vn")
                    .userName("HCMUT Student")
                    .password(passwordEncoder.encode("hcmut123"))
                    .role("Student")
                    .build();

            userRepository.save(hcmutStudent);
            log.info("HCMUT student user created: student@hcmut.edu.vn with password: hcmut123");
        }
    }
}
