package com.java07.baitap.repository;

import com.java07.baitap.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

    public List<CourseEntity> findByDurationGreaterThan(int duration);

    long count();
}
