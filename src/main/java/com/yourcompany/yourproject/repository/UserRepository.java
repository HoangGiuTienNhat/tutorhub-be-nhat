package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.dto.UserStatisticDto;
import com.yourcompany.yourproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

        @Query("SELECT new com.yourcompany.yourproject.dto.UserStatisticDto(" +
                        "CAST(s.uid AS string), s.userName, COALESCE(f.name, 'N/A'), " +
                        "COUNT(DISTINCT g.id), " +
                        "COUNT(DISTINCT CASE WHEN (:startDate IS NULL OR c.consultationDate >= :startDate) " +
                        "AND (:endDate IS NULL OR c.consultationDate <= :endDate) THEN c.id END)) " +
                        "FROM User s " +
                        "LEFT JOIN s.faculty f " +
                        "LEFT JOIN s.joinedGroups g " +
                        "LEFT JOIN g.consultations c "
                        +
                        "WHERE s.role = 'STUDENT' " +
                        "AND (:facultyId IS NULL OR s.faculty.id = :facultyId) " +
                        "AND (:topicId IS NULL OR :topicId IN (SELECT t.id FROM g.topics t)) " +
                        "AND (:tutorId IS NULL OR g.tutor.uid = :tutorId) " +
                        "GROUP BY s.uid, s.userName, f.name")
        List<UserStatisticDto> getStudentStatistics(@Param("facultyId") Long facultyId, @Param("topicId") Long topicId,
                        @Param("tutorId") Long tutorId, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT new com.yourcompany.yourproject.dto.UserStatisticDto(" +
                        "CAST(t.uid AS string), t.userName, COALESCE(f.name, 'N/A'), " +
                        "COUNT(DISTINCT g.id), " +
                        "COUNT(DISTINCT CASE WHEN (:startDate IS NULL OR c.consultationDate >= :startDate) " +
                        "AND (:endDate IS NULL OR c.consultationDate <= :endDate) THEN c.id END)) " +
                        "FROM User t " +
                        "LEFT JOIN t.faculty f " +
                        "LEFT JOIN Group g ON g.tutor = t " +
                        "LEFT JOIN g.consultations c "
                        +
                        "WHERE t.role = 'TUTOR' " +
                        "AND (:facultyId IS NULL OR t.faculty.id = :facultyId) " +
                        "AND (:topicId IS NULL OR :topicId IN (SELECT top.id FROM g.topics top)) " +
                        "AND (:studentId IS NULL OR :studentId IN (SELECT u.uid FROM g.joinedUsers u WHERE u.role = 'STUDENT')) "
                        +
                        "GROUP BY t.uid, t.userName, f.name")
        List<UserStatisticDto> getTutorStatistics(@Param("facultyId") Long facultyId, @Param("topicId") Long topicId,
                        @Param("studentId") Long studentId, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

}
