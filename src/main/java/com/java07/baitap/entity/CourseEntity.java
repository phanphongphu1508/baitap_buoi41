package com.java07.baitap.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.naming.Name;
import java.util.List;


@Data
@Entity(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "duration")
    private int duration;

    @OneToMany(mappedBy = "course")
    private List<RegistrationEntity> registrations;
}
