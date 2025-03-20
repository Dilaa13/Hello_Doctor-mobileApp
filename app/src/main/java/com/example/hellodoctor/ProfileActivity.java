package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, profileEmail, profileUsername, profilePassword;
    private TextView titleName, titleUsername;
    private Button editProfile;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeUI();

        // Get user data from the intent
        loadDataFromIntent();

        // Set up the edit profile button listener
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToEditProfile();
            }
        });

        // Set up the back button listener
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshPageAndNavigateHome();
            }
        });
    }

    private void initializeUI() {
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfile = findViewById(R.id.editButton);
        backImg = findViewById(R.id.backimg); // Initialize ImageView for back button
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        updateUI(name, email, username, password);
    }

    private void updateUI(String name, String email, String username, String password) {
        titleName.setText(name);
        titleUsername.setText(username);
        profileName.setText(name);
        profileEmail.setText(email);
        profileUsername.setText(username);
        profilePassword.setText(password);
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("name", titleName.getText().toString());
        intent.putExtra("email", profileEmail.getText().toString());
        intent.putExtra("username", profileUsername.getText().toString());
        intent.putExtra("password", profilePassword.getText().toString());
        startActivityForResult(intent, 1); // Request code 1
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the updated data
            String updatedName = data.getStringExtra("name");
            String updatedEmail = data.getStringExtra("email");
            String updatedUsername = data.getStringExtra("username");
            String updatedPassword = data.getStringExtra("password");

            // Update the UI
            updateUI(updatedName, updatedEmail, updatedUsername, updatedPassword);
        }
    }

    private void refreshPageAndNavigateHome() {
        // Refresh the activity
        Intent intent = getIntent();
        finish(); // Finish current activity
        startActivity(intent); // Start the activity again to refresh the content

        // Navigate to PatientHome
        Intent homeIntent = new Intent(ProfileActivity.this, PatientHome.class);
        startActivity(homeIntent);
    }
}
