package com.example.hellodoctor;

public class PatientAppointment {
    private String id;
    private int slotNumber;
    private String timeSlot;
    private String date;
    private String doctorId;
    private String doctorName;

    public PatientAppointment() {
        // Default constructor required for Firebase
    }

    public PatientAppointment(String id, int slotNumber, String timeSlot, String date, String doctorId, String doctorName ) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.timeSlot = timeSlot;
        this.date = date;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getSlotNumber() { return slotNumber; }
    public void setSlotNumber(int slotNumber) { this.slotNumber = slotNumber; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
}