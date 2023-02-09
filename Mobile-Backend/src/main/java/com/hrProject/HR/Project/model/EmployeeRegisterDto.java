package com.hrProject.HR.Project.model;

import com.hrProject.HR.Project.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeRegisterDto {
    String firstName;
    String lastName;
    Gender gender;
    Date birthDate;
    String email;
    Integer departmentId;
    Integer shiftId;
    String secret;
}