package com.yourcompany.yourproject.repository;

import com.yourcompany.yourproject.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByTutorUid(Long tutorId);

    List<Group> findByFacultyId(Long facultyId);

    @Query("SELECT g FROM Group g WHERE g.faculty.id = :facultyId AND g.tutor.uid = :tutorId")
    List<Group> findByFacultyIdAndTutorId(@Param("facultyId") Long facultyId, @Param("tutorId") Long tutorId);

    long countByFaculty_Id(Long facultyId);

    long countByTopics_Id(Long topicId);

    @Query("SELECT COUNT(DISTINCT g.tutor.uid) FROM Group g WHERE g.faculty.id = :facultyId")
    long countDistinctTutorsByFaculty(@Param("facultyId") Long facultyId);

    @Query("SELECT COUNT(DISTINCT g.tutor.uid) FROM Group g JOIN g.topics t WHERE t.id = :topicId")
    long countDistinctTutorsByTopic(@Param("topicId") Long topicId);
}
