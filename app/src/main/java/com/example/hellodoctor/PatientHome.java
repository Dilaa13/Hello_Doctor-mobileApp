package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

public class PatientHome extends AppCompatActivity {

    private RecyclerView recyclerViewDoctors;
    private pDoctorAdapter doctorAdapter;
    private List<pDoctor> doctorList;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView textViewNoDoctors;
    private LinearLayout logoutLayout, appointmentsLayout, articleLayout, emergencyLayout;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");

        // Initialize UI elements
        recyclerViewDoctors = findViewById(R.id.recyclerViewDoctors);
        progressBar = findViewById(R.id.progressBar);
        textViewNoDoctors = findViewById(R.id.textViewNoDoctors);
        logoutLayout = findViewById(R.id.logoutLayout);
        articleLayout = findViewById(R.id.articlelayout);
        appointmentsLayout = findViewById(R.id.appointmentsLayout);
        emergencyLayout = findViewById(R.id.emergencylayout);
        searchBar = findViewById(R.id.searchBar);

        recyclerViewDoctors.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        doctorList = new ArrayList<>();
        doctorAdapter = new pDoctorAdapter(this, doctorList);
        recyclerViewDoctors.setAdapter(doctorAdapter);

        // Get user data from the intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        // Set up the user profile picture click listener
        ImageView userProfilePic = findViewById(R.id.userProfilePic);
        userProfilePic.setOnClickListener(v -> {
            Intent profileIntent = new Intent(PatientHome.this, ProfileActivity.class);
            profileIntent.putExtra("name", name);
            profileIntent.putExtra("email", email);
            profileIntent.putExtra("username", username);
            profileIntent.putExtra("password", password);
            startActivity(profileIntent);
        });

        // Set up click listeners for different layouts
        setupClickListeners();

        // Set up search bar listener
        setupSearchBar();

        // Fetch all doctors from Firebase
        fetchDoctors();
    }

    private void setupClickListeners() {
        logoutLayout.setOnClickListener(v -> navigateToLoginActivity());

        appointmentsLayout.setOnClickListener(v -> {
            Intent appointmentsIntent = new Intent(PatientHome.this, AppointmentsListActivity.class);
            startActivity(appointmentsIntent);
        });

        articleLayout.setOnClickListener(v -> {
            Intent articleIntent = new Intent(PatientHome.this, ArticlesActivity.class);
            startActivity(articleIntent);
        });

        emergencyLayout.setOnClickListener(v -> {
            Intent emergencyIntent = new Intent(PatientHome.this, EmergencyActivity.class);
            startActivity(emergencyIntent);
        });
    }

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchDoctors() {
        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    pDoctor doctor = snapshot.getValue(pDoctor.class);
                    if (doctor != null) {
                        doctor.setId(snapshot.getKey());
                        doctorList.add(doctor);
                    }
                }

                // Check if the doctor list is empty
                updateUIState();

                doctorAdapter.notifyDataSetChanged();

                // Hide loading indicator
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientHome.this, "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

                // Hide loading indicator
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateUIState() {
        if (doctorList.isEmpty()) {
            textViewNoDoctors.setVisibility(View.VISIBLE);
            recyclerViewDoctors.setVisibility(View.GONE);
        } else {
            textViewNoDoctors.setVisibility(View.GONE);
            recyclerViewDoctors.setVisibility(View.VISIBLE);
        }
    }

    private void filterDoctors(String query) {
        List<pDoctor> filteredList = new ArrayList<>();
        for (pDoctor doctor : doctorList) {
            if (doctor.getName().toLowerCase().contains(query.toLowerCase()) ||
                    doctor.getSpecialty().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(doctor);
            }
        }
        doctorAdapter.updateList(filteredList);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(PatientHome.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish this activity to prevent the user from navigating back to it
    }
}