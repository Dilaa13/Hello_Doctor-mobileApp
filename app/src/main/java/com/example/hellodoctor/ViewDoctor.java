package com.example.hellodoctor;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

public class ViewDoctor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar; // Optional: ProgressBar for loading indicator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerViewDoctors);
        progressBar = findViewById(R.id.progressBar); // Optional: Initialize ProgressBar if used

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(this, doctorList); // Pass context to the adapter
        recyclerView.setAdapter(doctorAdapter);

        // Fetch all doctors from Firebase
        fetchDoctors();
    }

    private void fetchDoctors() {
        // Show loading indicator
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        doctor.setId(snapshot.getKey()); // Ensure ID is set
                        doctorList.add(doctor);
                    }
                }
                doctorAdapter.notifyDataSetChanged();

                // Hide loading indicator
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewDoctor.this, "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

                // Hide loading indicator
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}