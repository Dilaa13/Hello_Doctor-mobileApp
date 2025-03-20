package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddDoctors extends AppCompatActivity {

    private TextInputEditText editTextUserID, editTextDoctorName, editTextSpecialty, editTextPhoneNumber, editTextEmail;
    private Button buttonSubmit;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctors);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");

        // Initialize UI elements
        editTextUserID = findViewById(R.id.editTextUserID);
        editTextDoctorName = findViewById(R.id.editTextDoctorName);
        editTextSpecialty = findViewById(R.id.editTextSpecialty);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Set up button listeners
        buttonSubmit.setOnClickListener(v -> addDoctor());
    }

    private void addDoctor() {
        String userId = editTextUserID.getText().toString().trim();
        String doctorName = editTextDoctorName.getText().toString().trim();
        String specialty = editTextSpecialty.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (userId.isEmpty()) {
            editTextUserID.setError("User ID is required");
            editTextUserID.requestFocus();
            return;
        }

        if (doctorName.isEmpty()) {
            editTextDoctorName.setError("Doctor Name is required");
            editTextDoctorName.requestFocus();
            return;
        }

        if (specialty.isEmpty()) {
            editTextSpecialty.setError("Specialty is required");
            editTextSpecialty.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Phone Number is required");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        // Create a Map to store the doctor's details
        Map<String, String> doctor = new HashMap<>();
        doctor.put("name", doctorName);
        doctor.put("specialty", specialty);
        doctor.put("phone", phoneNumber);
        doctor.put("email", email);

        // Save doctor details to Firebase
        databaseReference.child(userId).setValue(doctor)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddDoctors.this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                    // Clear the input fields
                    editTextUserID.setText("");
                    editTextDoctorName.setText("");
                    editTextSpecialty.setText("");
                    editTextPhoneNumber.setText("");
                    editTextEmail.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddDoctors.this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
                });
    }
}