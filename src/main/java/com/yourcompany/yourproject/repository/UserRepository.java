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
}
