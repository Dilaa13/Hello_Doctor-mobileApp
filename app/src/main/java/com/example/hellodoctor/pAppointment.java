package com.example.hellodoctor;

public class pAppointment {
    private String id;
    private String doctor_id;
    private String name; // Doctor's name
    private String date;
    private String start_time;
    private String end_time;
    private boolean isAvailable;

    // Default constructor required for Firebase
    public pAppointment() {
    }

    // Constructor with parameters
    public pAppointment(String id, String doctor_id, String name, String date, String start_time, String end_time, boolean isAvailable) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.name = name;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.isAvailable = isAvailable;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}