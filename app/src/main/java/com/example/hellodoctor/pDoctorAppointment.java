package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class pDoctorAppointment extends AppCompatActivity implements pAppointmentAdapter.SelectedTimeSlotCallback {

    private TextView doctorIdTextView, doctorNameTextView, doctorSpecializationTextView, doctorPhoneTextView;
    private RecyclerView recyclerViewAppointments;
    private pAppointmentAdapter appointmentAdapter;
    private List<pAppointment> appointmentList;
    private DatabaseReference doctorTimeRef;
    private DatabaseReference patientAppointmentsRef;

    private String selectedDate, selectedDoctorId, selectedDoctorName, selectedTimeSlot;
    private int selectedSlotNumber;
    private Set<String> unavailableSlots; // Track unavailable slots
    private Map<String, Set<String>> doctorAppointmentsMap; // Map to track unavailable slots for each doctor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdoctor_appointment);

        // Initialize UI elements
        doctorIdTextView = findViewById(R.id.doctorIdTextView);
        doctorNameTextView = findViewById(R.id.doctorNameTextView);
        doctorSpecializationTextView = findViewById(R.id.doctorSpecializationTextView);
        doctorPhoneTextView = findViewById(R.id.doctorPhoneTextView);
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);

        // Initialize Firebase Database references
        doctorTimeRef = FirebaseDatabase.getInstance().getReference("doctor_time");
        patientAppointmentsRef = FirebaseDatabase.getInstance().getReference("patient_appointments");

        unavailableSlots = new HashSet<>(); // Initialize the set
        doctorAppointmentsMap = new HashMap<>(); // Initialize the map

        // Get doctor details from previous activity
        String doctorId = getIntent().getStringExtra("doctorId");
        String doctorName = getIntent().getStringExtra("doctorName");
        String doctorSpecialization = getIntent().getStringExtra("doctorSpecialization");
        String doctorPhone = getIntent().getStringExtra("doctorPhone");

        if (doctorId != null) {
            doctorIdTextView.setText(doctorId);
            doctorNameTextView.setText(doctorName);
            doctorSpecializationTextView.setText(doctorSpecialization);
            doctorPhoneTextView.setText(doctorPhone);

            // Fetch appointments and availability
            fetchAppointments(doctorId);
        } else {
            Toast.makeText(this, "Doctor ID is missing", Toast.LENGTH_SHORT).show();
        }

        // Book Now button click listener
        Button bookNowButton = findViewById(R.id.bookNowButton);
        bookNowButton.setOnClickListener(v -> {
            if (selectedTimeSlot != null) {
                navigateToConfirmationPage(); // Navigate to the confirmation page
            } else {
                Toast.makeText(pDoctorAppointment.this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAppointments(String doctorId) {
        doctorTimeRef.orderByChild("doctor_id").equalTo(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList = new ArrayList<>();
                unavailableSlots.clear(); // Clear previous unavailable slots
                doctorAppointmentsMap.clear(); // Clear previous doctor appointments map

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    pAppointment appointment = snapshot.getValue(pAppointment.class);
                    if (appointment != null) {
                        appointmentList.add(appointment);
                        fetchUnavailableSlotsForDoctor(doctorId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(pDoctorAppointment.this, "Failed to load appointments: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUnavailableSlotsForDoctor(String doctorId) {
        patientAppointmentsRef.orderByChild("doctorId").equalTo(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unavailableSlots.clear(); // Clear previous unavailable slots

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot appointmentSnapshot = snapshot.child("timeSlot");
                    if (appointmentSnapshot.exists()) {
                        String timeSlot = appointmentSnapshot.getValue(String.class);
                        if (timeSlot != null) {
                            unavailableSlots.add(timeSlot);
                        }
                    }
                }
                setupRecyclerView(); // Setup RecyclerView with updated data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(pDoctorAppointment.this, "Failed to load patient appointments: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        if (appointmentAdapter == null) {
            appointmentAdapter = new pAppointmentAdapter(appointmentList, this, this);
            recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewAppointments.setAdapter(appointmentAdapter);
        } else {
            appointmentAdapter.setUnavailableSlots(unavailableSlots); // Update adapter with unavailable slots
            appointmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTimeSlotSelected(String timeSlot, String date, String doctorId, String name, int slotNumber) {
        selectedTimeSlot = timeSlot;
        selectedDate = date;
        selectedDoctorId = doctorId;
        selectedDoctorName = name;
        selectedSlotNumber = slotNumber;
    }

    private void navigateToConfirmationPage() {
        Intent intent = new Intent(pDoctorAppointment.this, AppointmentDetailsActivity.class);
        intent.putExtra("username", "John Doe"); // Replace with actual username
        intent.putExtra("slotNumber", selectedSlotNumber);
        intent.putExtra("timeSlot", selectedTimeSlot);
        intent.putExtra("date", selectedDate);
        intent.putExtra("doctorId", selectedDoctorId);
        intent.putExtra("doctorName", selectedDoctorName);
        startActivity(intent);
    }
}