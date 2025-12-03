package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.faculty.id = :facultyId AND u.role = 'STUDENT'")
    List<User> findStudentsByFaculty(@Param("facultyId") Long facultyId);

    long countByFaculty_IdAndRole(Long facultyId, String role);

    @Query("SELECT COUNT(DISTINCT u.uid) FROM User u JOIN u.joinedGroups g JOIN g.topics t WHERE t.id = :topicId AND u.role = 'STUDENT'")
    long countStudentsByTopic(@Param("topicId") Long topicId);
}
