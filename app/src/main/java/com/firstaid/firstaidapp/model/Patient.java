package com.firstaid.firstaidapp.model;

import java.io.Serializable;

public class Patient implements Serializable {

    private String name;
    private String idNumber;
    private String email;
    private String Password;
    private String bloodType;
    private String chronic;
    private String allergy;
    private String gender;
    private String age;
    private String phone;

    public Patient(String name, String idNumber, String email, String password, String bloodType, String chronic, String allergy, String gender, String age, String phone) {
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.Password = password;
        this.bloodType = bloodType;
        this.chronic = chronic;
        this.allergy = allergy;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
    }

    public Patient() {
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return Password;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getChronic() {
        return chronic;
    }

    public String getAllergy() {
        return allergy;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }
}
