package com.example.hellodoctor;

public class Doctor {
    private String id;
    private String name;
    private String specialty;
    private String phone;
    private String email;

    // Default constructor required for Firebase
    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    }

    // Parameterized constructor
    public Doctor(String id, String name, String specialty, String phone, String email) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
        this.email = email;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}