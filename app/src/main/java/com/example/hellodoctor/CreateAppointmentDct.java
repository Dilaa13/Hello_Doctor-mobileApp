package com.example.hellodoctor;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateAppointmentDct extends AppCompatActivity {

    private TextInputEditText editTextHospital, editTextStartTime, editTextEndTime, editTextNumberOfPatients, editTextNumberOfReports;
    private Button buttonSubmit, buttonCancel, buttonViewTimeSlots, buttonSelectDate;
    private TextView textViewSelectDate;
    private DatabaseReference databaseReference;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment_dct);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("appointments");

        // Initialize UI elements
        editTextHospital = findViewById(R.id.editTextHospital);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        editTextNumberOfPatients = findViewById(R.id.editTextNumberOfPatients);
        editTextNumberOfReports = findViewById(R.id.editTextNumberOfReports);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonViewTimeSlots = findViewById(R.id.buttonViewTimeSlots);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        textViewSelectDate = findViewById(R.id.textViewSelectDate);

        // Set up date picker for selecting date
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Set up button listeners
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });

        buttonViewTimeSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTimeSlots();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAppointmentDct.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                buttonSelectDate.setText(selectedDate); // Update button text to show selected date
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void createAppointment() {
        String hospital = editTextHospital.getText().toString().trim();
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String numberOfPatients = editTextNumberOfPatients.getText().toString().trim();
        String numberOfReports = editTextNumberOfReports.getText().toString().trim();

        if (hospital.isEmpty() || selectedDate == null || selectedDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || numberOfPatients.isEmpty() || numberOfReports.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique key for the appointment
        String appointmentId = databaseReference.push().getKey();

        if (appointmentId != null) {
            // Create a Map to store the appointment details
            Map<String, String> appointment = new HashMap<>();
            appointment.put("hospital", hospital);
            appointment.put("date", selectedDate); // Use selectedDate instead of a direct editText field
            appointment.put("start_time", startTime);
            appointment.put("end_time", endTime);
            appointment.put("number_of_patients", numberOfPatients);
            appointment.put("number_of_reports", numberOfReports);

            // Save appointment details to Firebase
            databaseReference.child(appointmentId).setValue(appointment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CreateAppointmentDct.this, "Appointment Created", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreateAppointmentDct.this, "Failed to Create Appointment", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void viewTimeSlots() {
        // Start the ViewTime activity
        Intent intent = new Intent(CreateAppointmentDct.this, ViewAllRecordsActivity.class);
        startActivity(intent);
    }
}