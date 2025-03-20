package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsListActivity extends AppCompatActivity {

    private ListView appointmentsListView;
    private List<PatientAppointment> appointmentList;
    private BookingAppointmentListAdapter appointmentAdapter;

    private DatabaseReference patientAppointmentsRef;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_list);

        appointmentsListView = findViewById(R.id.appointmentsListView);
        appointmentList = new ArrayList<>();
        appointmentAdapter = new BookingAppointmentListAdapter(this, appointmentList);
        appointmentsListView.setAdapter(appointmentAdapter);

        // Initialize the back button
        backButton = findViewById(R.id.backButton);

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the patient home page
                Intent intent = new Intent(AppointmentsListActivity.this, PatientHome.class);
                startActivity(intent);
                finish(); // Optional: finish this activity if you don't want the user to come back to it
            }
        });

        patientAppointmentsRef = FirebaseDatabase.getInstance().getReference("patient_appointments");

        fetchAppointments();
    }

    private void fetchAppointments() {
        patientAppointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PatientAppointment appointment = snapshot.getValue(PatientAppointment.class);
                    if (appointment != null) {
                        appointmentList.add(appointment);
                    }
                }
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AppointmentsListActivity.this, "Failed to load appointments: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}