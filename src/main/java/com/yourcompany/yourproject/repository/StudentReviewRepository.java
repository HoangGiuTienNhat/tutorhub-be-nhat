package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.entity.StudentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentReviewRepository extends JpaRepository<StudentReview, Long> {

    List<StudentReview> findByConsultationId(Long consultationId);

    Optional<StudentReview> findByConsultationIdAndStudentUid(Long consultationId, Long studentId);

    @Query("SELECT sr FROM StudentReview sr WHERE sr.consultation.group.id = :groupId")
    List<StudentReview> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT sr FROM StudentReview sr WHERE sr.consultation.group.faculty.id = :facultyId")
    List<StudentReview> findByFacultyId(@Param("facultyId") Long facultyId);

    @Query("SELECT sr FROM StudentReview sr WHERE sr.consultation.group.faculty.id = :facultyId AND sr.tutor.uid = :tutorId")
    List<StudentReview> findByFacultyIdAndTutorId(@Param("facultyId") Long facultyId, @Param("tutorId") Long tutorId);
}
