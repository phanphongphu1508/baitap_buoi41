package com.java07.baitap.repository;


import com.java07.baitap.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {

    public List<StudentEntity> findByNameContaining(String keyword);

    Page<StudentEntity> findAll(Specification<StudentEntity> spec, Pageable pageable);
}
