package com.java07.baitap.repository;

import com.java07.baitap.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {
    List<RegistrationEntity> findByStudentId(int studentId);

    List<RegistrationEntity> findByCourseId(int courseId);

    List<RegistrationEntity> findByStudentIdAndCourseId(int studentId, int courseId);
}