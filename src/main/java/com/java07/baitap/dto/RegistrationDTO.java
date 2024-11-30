package com.java07.baitap.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RegistrationDTO {

    private int registrationId;
    private int studentId;
    private int courseId;
    private Date registrationDate;

}