package com.java07.baitap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "students")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "email")
    private String email;


    @OneToMany(mappedBy = "student")
    private List<RegistrationEntity> registrations;
}