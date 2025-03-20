package com.example.hellodoctor;

public class pDoctor {
    private String id;
    private String name;
    private String specialty; // Use the correct field name
    private String phone;
    private String email;

    // Default constructor required for Firebase
    public pDoctor() {
        // Default constructor required for calls to DataSnapshot.getValue(pDoctor.class)
    }

    // Parameterized constructor
    public pDoctor(String id, String name, String specialization, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.specialty = specialization; // Use the correct field name
        this.phone = phoneNumber;
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

    public String getSpecialty() { // Ensure this method exists
        return specialty;
    }

    public void setSpecialization(String specialization) {
        this.specialty = specialization;
    }

    public String getPhone() { // Ensure this method exists
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}