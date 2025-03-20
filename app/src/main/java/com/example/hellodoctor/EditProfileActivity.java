package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail, editUsername, editPassword;
    private Button saveButton;
    private String nameUser, emailUser, usernameUser, passwordUser;
    private DatabaseReference reference;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        backImg = findViewById(R.id.backimgedit);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateProfile()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No changes found", Toast.LENGTH_SHORT).show();
                }
                moveToProfileActivity();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateProfile()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
                moveToProfileActivity();
            }
        });
    }

    private boolean updateProfile() {
        boolean isUpdated = false;

        if (!nameUser.equals(editName.getText().toString().trim())) {
            reference.child(usernameUser).child("name").setValue(editName.getText().toString().trim());
            nameUser = editName.getText().toString().trim();
            isUpdated = true;
        }

        if (!emailUser.equals(editEmail.getText().toString().trim())) {
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString().trim());
            emailUser = editEmail.getText().toString().trim();
            isUpdated = true;
        }

        if (!passwordUser.equals(editPassword.getText().toString().trim())) {
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString().trim());
            passwordUser = editPassword.getText().toString().trim();
            isUpdated = true;
        }

        return isUpdated;
    }

    private void showData() {
        Intent intent = getIntent();
        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }

    private void moveToProfileActivity() {
        Intent intent = new Intent();
        intent.putExtra("name", nameUser);
        intent.putExtra("email", emailUser);
        intent.putExtra("username", usernameUser);
        intent.putExtra("password", passwordUser);
        setResult(RESULT_OK, intent);
        finish(); // Finish this activity and return to ProfileActivity
    }
}
