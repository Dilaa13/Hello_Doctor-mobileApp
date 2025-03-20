package com.example.hellodoctor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Appointment_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_menu);

        // Find buttons by their IDs
        Button btnAddAppointments = findViewById(R.id.btn_add_apponitments);
        Button btnViewAppointments = findViewById(R.id.btn_view_appointments);

        // Set click listener for Add Doctors button
        btnAddAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddDoctors activity
                Intent intent = new Intent(Appointment_menu.this, CreateAppointmentDct.class);
                startActivity(intent);
            }
        });

        // Set click listener for View Doctors button
        btnViewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewDoctors activity
                Intent intent = new Intent(Appointment_menu.this, ViewAllRecordsActivity.class);
                startActivity(intent);
            }
        });
    }
}