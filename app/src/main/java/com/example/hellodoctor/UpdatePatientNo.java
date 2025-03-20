package com.example.hellodoctor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePatientNo extends AppCompatActivity {

    private EditText etNumPatientsSoFar, etCurrentPatientNo;
    private Button btnUpdatePatientNo, btnViewCurrentPatient;
    private TextView tvPatientNumbers;
    private DatabaseReference dbRef;
    private String doctorID, timeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient_no);

        etNumPatientsSoFar = findViewById(R.id.etNumPatientsSoFar);
        etCurrentPatientNo = findViewById(R.id.etCurrentPatientNo);
        btnUpdatePatientNo = findViewById(R.id.btnUpdatePatientNo);
        btnViewCurrentPatient = findViewById(R.id.btnViewCurrentPatient);
        tvPatientNumbers = findViewById(R.id.tvPatientNumbers);

        // Get the doctor ID and time ID from the intent
        doctorID = getIntent().getStringExtra("DOCTOR_ID");
        timeID = getIntent().getStringExtra("TIME_ID");

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("doctor_time").child(timeID);

        btnUpdatePatientNo.setOnClickListener(v -> {
            String numPatientsSoFar = etNumPatientsSoFar.getText().toString().trim();
            String currentPatientNo = etCurrentPatientNo.getText().toString().trim();

            if (TextUtils.isEmpty(numPatientsSoFar) || TextUtils.isEmpty(currentPatientNo)) {
                Toast.makeText(UpdatePatientNo.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            } else {
                updatePatientNumbers(numPatientsSoFar, currentPatientNo);
            }
        });

        btnViewCurrentPatient.setOnClickListener(v -> viewPatientNumbers());

        // Load current patient numbers when activity starts
        loadPatientNumbers();
    }

    private void updatePatientNumbers(String numPatientsSoFar, String currentPatientNo) {
        dbRef.child("num_patients_so_far").setValue(numPatientsSoFar);
        dbRef.child("current_patient_no").setValue(currentPatientNo).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UpdatePatientNo.this, "Patient numbers updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdatePatientNo.this, "Failed to update patient numbers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewPatientNumbers() {
        String numPatientsSoFar = etNumPatientsSoFar.getText().toString().trim();
        String currentPatientNo = etCurrentPatientNo.getText().toString().trim();

        if (TextUtils.isEmpty(numPatientsSoFar) || TextUtils.isEmpty(currentPatientNo)) {
            Toast.makeText(UpdatePatientNo.this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
        } else {
            tvPatientNumbers.setText("Number of Patients So Far: " + numPatientsSoFar + "\nCurrent Patient Number: " + currentPatientNo);
        }
    }

    private void loadPatientNumbers() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String numPatientsSoFar = dataSnapshot.child("num_patients_so_far").getValue(String.class);
                String currentPatientNo = dataSnapshot.child("current_patient_no").getValue(String.class);

                if (numPatientsSoFar != null && currentPatientNo != null) {
                    etNumPatientsSoFar.setText(numPatientsSoFar);
                    etCurrentPatientNo.setText(currentPatientNo);
                } else {
                    tvPatientNumbers.setText("No data available.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdatePatientNo.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}