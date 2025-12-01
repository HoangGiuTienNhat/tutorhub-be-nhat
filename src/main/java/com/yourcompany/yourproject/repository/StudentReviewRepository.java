package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.entity.StudentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentReviewRepository extends JpaRepository<StudentReview, Long> {

    List<StudentReview> findByConsultationId(Long consultationId);

    Optional<StudentReview> findByConsultationIdAndStudentUid(Long consultationId, Long studentId);
}
