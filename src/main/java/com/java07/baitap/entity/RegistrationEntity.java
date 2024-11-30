package com.java07.baitap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity(name = "registrations")
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Column(name = "registration_date")
    private Date registrationDate;
}
