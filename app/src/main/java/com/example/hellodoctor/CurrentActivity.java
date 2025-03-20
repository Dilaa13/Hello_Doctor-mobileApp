package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        // Retrieve doctor details from Intent
        Intent intent = getIntent();
        String doctorID = intent.getStringExtra("DOCTOR_ID");
        String doctorName = intent.getStringExtra("DOCTOR_NAME");
        String doctorSpecialty = intent.getStringExtra("DOCTOR_SPECIALTY");
        String doctorPhone = intent.getStringExtra("DOCTOR_PHONE");
        String doctorEmail = intent.getStringExtra("DOCTOR_EMAIL");

        // Check and log the retrieved doctor ID
        if (doctorID == null || doctorID.isEmpty()) {
            Log.d("CurrentActivity", "doctorID is null or empty");
        } else {
            Log.d("CurrentActivity", "doctorID: " + doctorID);
        }

        // Set the greeting text dynamically using the doctor ID
        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        if (doctorID != null) {
            welcomeTextView.setText("Hello Doctor " + doctorID);
        } else {
            welcomeTextView.setText("Hello Doctor");
        }

        // Set the doctor details in the corresponding TextViews
        TextView nameTextView = findViewById(R.id.textViewNameDetail);
        nameTextView.setText(doctorName != null ? doctorName : "Name");

        TextView idTextView = findViewById(R.id.textViewID);
        idTextView.setText(doctorID != null ? doctorID : "ID");

        TextView specialtyTextView = findViewById(R.id.textViewSpecialtyDetail);
        specialtyTextView.setText(doctorSpecialty != null ? doctorSpecialty : "Specialty");

        TextView phoneTextView = findViewById(R.id.textViewPhoneDetail);
        phoneTextView.setText(doctorPhone != null ? doctorPhone : "Phone");

        TextView emailTextView = findViewById(R.id.textViewEmailDetail);
        emailTextView.setText(doctorEmail != null ? doctorEmail : "Email");

        // Set up the button to navigate to CreateAppointmentDct activity
        Button btnAddTimeSlots = findViewById(R.id.btnAddTimeSlots);
        btnAddTimeSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start CreateAppointmentDct
                Intent intent = new Intent(CurrentActivity.this, DA1.class);
                // Pass the doctor's ID to the next activity
                intent.putExtra("DOCTOR_ID", doctorID);
                startActivity(intent);
            }
        });

        // Set up the button to navigate to ViewTimeSlotsActivity
        Button btnViewTimeSlots = findViewById(R.id.btnViewTimeSlots);
        btnViewTimeSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start ViewAllRecordsActivity
                Intent intent = new Intent(CurrentActivity.this, ViewAllRecordsActivity.class);
                // Pass the doctor's ID to the next activity
                intent.putExtra("DOCTOR_ID", doctorID);
                startActivity(intent);
            }
        });

    }
}