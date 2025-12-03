package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByGroupId(Long groupId);

    @Query("SELECT COUNT(c.id) FROM Consultation c JOIN c.group g WHERE g.faculty.id = :facultyId AND (:startDate IS NULL OR c.consultationDate >= :startDate) AND (:endDate IS NULL OR c.consultationDate <= :endDate)")
    long countByFaculty(@Param("facultyId") Long facultyId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(c.id) FROM Consultation c JOIN c.group g JOIN g.topics t WHERE t.id = :topicId AND (:startDate IS NULL OR c.consultationDate >= :startDate) AND (:endDate IS NULL OR c.consultationDate <= :endDate)")
    long countByTopic(@Param("topicId") Long topicId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
