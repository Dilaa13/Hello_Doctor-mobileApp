package com.example.hellodoctor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DA1 extends AppCompatActivity {

    private TextView tvSelectDate, tvDoctorID;
    private EditText etStartTime, etEndTime, etNumPatients, etNumReports, etTimeID;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da1);

        tvSelectDate = findViewById(R.id.tvSelectDate);
        tvDoctorID = findViewById(R.id.tvDoctorID);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etNumPatients = findViewById(R.id.etNumPatients);
        etNumReports = findViewById(R.id.etNumReports);
        etTimeID = findViewById(R.id.etTimeID);

        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctor_time");

        // Get the doctor ID passed from the previous activity
        String doctorId = getIntent().getStringExtra("DOCTOR_ID");
        if (doctorId != null) {
            tvDoctorID.setText(doctorId);  // Set the doctor ID in the TextView
        }

        // Date Picker
        tvSelectDate.setOnClickListener(v -> showDatePicker());

        // Time Pickers for Start Time and End Time
        etStartTime.setOnClickListener(v -> showTimePicker(etStartTime));
        etEndTime.setOnClickListener(v -> showTimePicker(etEndTime));

        // Submit Button
        btnSubmit.setOnClickListener(v -> submitAppointment());

        // Cancel Button
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DA1.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year1);
                    tvSelectDate.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(EditText timeField) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                DA1.this,
                (view, hourOfDay, minute1) -> {
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int hourIn12HrFormat = (hourOfDay == 0 || hourOfDay == 12) ? 12 : hourOfDay % 12;
                    String selectedTime = String.format("%02d:%02d %s", hourIn12HrFormat, minute1, amPm);
                    timeField.setText(selectedTime);
                },
                hour, minute, false);
        timePickerDialog.show();
    }

    private void submitAppointment() {
        String doctorId = tvDoctorID.getText().toString();
        String date = tvSelectDate.getText().toString();
        String startTime = etStartTime.getText().toString();
        String endTime = etEndTime.getText().toString();
        String timeID = etTimeID.getText().toString();
        String numPatients = etNumPatients.getText().toString();
        String numReports = etNumReports.getText().toString();

        // Validate the input fields
        if (doctorId.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || timeID.isEmpty() || numPatients.isEmpty() || numReports.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to hold the appointment details
        Map<String, String> appointmentDetails = new HashMap<>();
        appointmentDetails.put("doctor_id", doctorId);
        appointmentDetails.put("date", date);
        appointmentDetails.put("start_time", startTime);
        appointmentDetails.put("end_time", endTime);
        appointmentDetails.put("time_id", timeID);
        appointmentDetails.put("num_patients", numPatients);
        appointmentDetails.put("num_reports", numReports);

        // Reference to the appointment using timeID
        DatabaseReference appointmentRef = databaseReference.child(timeID);

        // Save the appointment to Firebase
        appointmentRef.setValue(appointmentDetails)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DA1.this, "Time Created", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity on successful submission
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DA1.this, "Failed to create appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}