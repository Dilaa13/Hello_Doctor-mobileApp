package com.example.hellodoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dct_menu extends AppCompatActivity {
    private CardView addDoctors, viewDoctors,cardLabTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dct_menu);

        // Find buttons by their IDs
        // btnAddDoctors = findViewById(R.id.btn_add_doctors);
        //Button btnViewDoctors = findViewById(R.id.btn_view_doctors);
        viewDoctors = findViewById(R.id.viewDoctors);
        cardLabTest = findViewById(R.id.cardLabTest);



        // Set click listener for Add Doctors button
        /*btnAddDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddDoctors activity
                Intent intent = new Intent(Dct_menu.this, AddDoctors.class);
                startActivity(intent);
            }
        });

        // Set click listener for View Doctors button
        btnViewDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewDoctors activity
                Intent intent = new Intent(Dct_menu.this, ViewDoctor.class);
                startActivity(intent);
            }
        });*/
        cardLabTest.setOnClickListener(v -> {
            Intent intent = new Intent(Dct_menu.this, AddDoctors.class); // Replace with your actual activity
            startActivity(intent);
        });
        viewDoctors.setOnClickListener(v -> {
            Intent intent = new Intent(Dct_menu.this, ViewDoctor.class); // Replace with your actual activity
            startActivity(intent);
        });
    }
}