package com.example.hellodoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellodoctor.R;

public class EmergencyActivity extends AppCompatActivity {

    private Button contactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        contactButton = findViewById(R.id.buttonContact);

        contactButton.setOnClickListener(v -> {
            // Open dialer with hotline number
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:123456789")); // Replace with your hotline number
            startActivity(intent);
        });
    }
}