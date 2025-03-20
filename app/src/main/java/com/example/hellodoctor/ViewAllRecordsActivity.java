package com.example.hellodoctor;

import android.os.Bundle;
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

public class ViewAllRecordsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private DatabaseReference databaseReference;
    private String doctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_records);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();

        // Pass the context along with the list to the adapter
        adapter = new AppointmentAdapter(this, appointmentList);
        recyclerView.setAdapter(adapter);

        // Get the doctor ID passed from the previous activity
        doctorID = getIntent().getStringExtra("DOCTOR_ID");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("doctor_time");

        fetchAppointments();
    }

    private void fetchAppointments() {
        // Filter by doctor ID
        databaseReference.orderByChild("doctor_id").equalTo(doctorID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Appointment appointment = snapshot.getValue(Appointment.class);
                            if (appointment != null) {
                                appointmentList.add(appointment);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }
}