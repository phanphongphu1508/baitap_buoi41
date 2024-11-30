package com.java07.baitap.specification;


import com.java07.baitap.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<StudentEntity> hasName(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<StudentEntity> hasAgeBetween(Integer ageFrom, Integer ageTo) {
        return (root, query, builder) -> builder.between(root.get("age"), ageFrom, ageTo);
    }

    public static Specification<StudentEntity> hasEmailEndingWith(String domain) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + domain);
    }

}
