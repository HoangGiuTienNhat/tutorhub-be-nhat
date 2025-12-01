package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByGroupId(Long groupId);
}

