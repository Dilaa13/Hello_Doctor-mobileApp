package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Home extends AppCompatActivity {

    private CardView cardLabTest, cardByMedicine, cardFindDoctor, cardAppointment, cardOrderDetails, cardExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure this matches your XML filename

        // Initialize UI elements
        cardLabTest = findViewById(R.id.cardLabTest);
        cardByMedicine = findViewById(R.id.cardByMedicine);
        cardFindDoctor = findViewById(R.id.cardFindDoctor);
        cardAppointment = findViewById(R.id.cardAppointment);
        cardOrderDetails = findViewById(R.id.cardOrderDetails);
        cardExit = findViewById(R.id.cardExit);




        cardFindDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Dct_menu.class); // Replace with your actual activity
            startActivity(intent);
        });

        cardAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AppointmentsListActivity.class); // Replace with your actual activity
            startActivity(intent);
        });

        cardByMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, MedicineDetailsActivity.class); // Replace with your actual activity
            startActivity(intent);
        });
        cardExit.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, LoginActivity.class); // Replace with your actual activity
            startActivity(intent);
            finish();
        });

    }
}