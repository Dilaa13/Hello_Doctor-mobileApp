package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentDetailsActivity extends AppCompatActivity {

    private TextView slotNumberTextView, timeSlotTextView, dateTextView, doctorIdTextView, doctorNameTextView;
    private Button confirmAppointmentButton;

    private String timeSlot, date, doctorId, doctorName;
    private int slotNumber;

    private DatabaseReference patientAppointmentsRef;
    private DatabaseReference doctorsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        // Initialize UI elements
        slotNumberTextView = findViewById(R.id.slotNumberTextView);
        timeSlotTextView = findViewById(R.id.timeSlotTextView);
        dateTextView = findViewById(R.id.dateTextView);
        doctorIdTextView = findViewById(R.id.doctorIdTextView);
        doctorNameTextView = findViewById(R.id.doctorNameTextView);
        confirmAppointmentButton = findViewById(R.id.confirmAppointmentButton);

        // Initialize Firebase Database references
        patientAppointmentsRef = FirebaseDatabase.getInstance().getReference("patient_appointments");
        doctorsRef = FirebaseDatabase.getInstance().getReference("doctors");

        // Get appointment details from the Intent
        Intent intent = getIntent();
        slotNumber = intent.getIntExtra("slotNumber", -1);
        timeSlot = intent.getStringExtra("timeSlot");
        date = intent.getStringExtra("date");
        doctorId = intent.getStringExtra("doctorId");

        // Fetch doctor's name and set the appointment details
        if (!TextUtils.isEmpty(doctorId)) {
            fetchDoctorDetails();
        } else {
            Toast.makeText(this, "Doctor ID is missing", Toast.LENGTH_SHORT).show();
        }

        // Confirm Appointment button click listener
        confirmAppointmentButton.setOnClickListener(v -> confirmAppointment());
    }

    private void fetchDoctorDetails() {
        doctorsRef.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctorName = dataSnapshot.child("name").getValue(String.class);
                    doctorNameTextView.setText(doctorName != null ? doctorName : "N/A");
                } else {
                    doctorNameTextView.setText("N/A");
                }

                // Set other appointment details
                setAppointmentDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AppointmentDetailsActivity.this, "Failed to fetch doctor details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAppointmentDetails() {
        slotNumberTextView.setText(String.valueOf(slotNumber));
        timeSlotTextView.setText(timeSlot != null ? timeSlot : "N/A");
        dateTextView.setText(date != null ? date : "N/A");
        doctorIdTextView.setText(doctorId != null ? doctorId : "N/A");
    }

    private void confirmAppointment() {
        String appointmentId = patientAppointmentsRef.push().getKey();
        if (appointmentId != null) {
            PatientAppointment appointment = new PatientAppointment(
                    appointmentId, slotNumber, timeSlot, date, doctorId, doctorName // Username is removed
            );
            patientAppointmentsRef.child(appointmentId).setValue(appointment)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AppointmentDetailsActivity.this, "Appointment confirmed successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity and return to the previous screen
                        } else {
                            Toast.makeText(AppointmentDetailsActivity.this, "Failed to confirm appointment: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(AppointmentDetailsActivity.this, "Failed to generate appointment ID", Toast.LENGTH_SHORT).show();
        }
    }
}