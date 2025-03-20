package com.example.hellodoctor;

public class Appointment {
    private String doctor_id;
    private String date;
    private String start_time;
    private String end_time;
    private String time_id;
    private String num_patients;
    private String num_reports;

    // Default constructor required for calls to DataSnapshot.getValue(Appointment.class)
    public Appointment() {}

    // Parameterized constructor
    public Appointment(String doctor_id, String date, String start_time, String end_time, String time_id, String num_patients, String num_reports) {
        this.doctor_id = doctor_id;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.time_id = time_id;
        this.num_patients = num_patients;
        this.num_reports = num_reports;
    }

    // Getters
    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
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

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }

    public String getNum_patients() {
        return num_patients;
    }

    public void setNum_patients(String num_patients) {
        this.num_patients = num_patients;
    }

    public String getNum_reports() {
        return num_reports;
    }

    public void setNum_reports(String num_reports) {
        this.num_reports = num_reports;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "doctor_id='" + doctor_id + '\'' +
                ", date='" + date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", time_id='" + time_id + '\'' +
                ", num_patients='" + num_patients + '\'' +
                ", num_reports='" + num_reports + '\'' +
                '}';
    }
}