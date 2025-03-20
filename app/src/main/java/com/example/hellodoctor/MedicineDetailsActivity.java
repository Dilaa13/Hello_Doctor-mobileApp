package com.example.hellodoctor;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MedicineDetailsActivity extends AppCompatActivity {

    private ImageView medicineImageView;
    private TextView medicineNameTextView;
    private TextView dosageTextView;
    private TextView descriptionTextView;
    private TextView sideEffectsTextView;
    private TextView storageTextView;
    private TextView manufacturerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        // Initialize views
        medicineImageView = findViewById(R.id.medicineImageView);
        medicineNameTextView = findViewById(R.id.medicineNameTextView);
        dosageTextView = findViewById(R.id.dosageTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        sideEffectsTextView = findViewById(R.id.sideEffectsTextView);
        storageTextView = findViewById(R.id.storageTextView);
        manufacturerTextView = findViewById(R.id.manufacturerTextView);

        // Set data (replace with actual data)
        // Example data: Replace with dynamic data from your database or API
        medicineImageView.setImageResource(R.drawable.download);
        medicineNameTextView.setText("Paracetamol");
        dosageTextView.setText("Dosage: 500mg");
        descriptionTextView.setText("Used to treat pain and fever.");
        sideEffectsTextView.setText("Possible side effects include nausea, rash.");
        storageTextView.setText("Store in a cool, dry place.");
        manufacturerTextView.setText("XYZ Pharmaceuticals");
    }
}