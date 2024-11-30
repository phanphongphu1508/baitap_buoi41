package com.java07.baitap.specification;


import com.java07.baitap.entity.CourseEntity;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {
    public static Specification<CourseEntity> hasDurationGreaterThan(int duration) {
        return (root, query, builder) -> builder.greaterThan(root.get("duration"), duration);
    }
}
