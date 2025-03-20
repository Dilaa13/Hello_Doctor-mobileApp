package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        return validateUsername() && validatePassword();
    }

    private boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (TextUtils.isEmpty(val)) {
            loginUsername.setError("Username cannot be empty");
            loginUsername.requestFocus();
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (TextUtils.isEmpty(val)) {
            loginPassword.setError("Password cannot be empty");
            loginPassword.requestFocus();
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    private void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        // Check if username and password are both "admin"
        if ("admin".equals(userUsername) && "admin".equals(userPassword)) {
            // Start the Home activity if credentials are correct
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);
            finish(); // Close the login activity
            return;
        }

        // Check user credentials in Firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                            // Credentials are correct
                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String emailFromDB = userSnapshot.child("email").getValue(String.class);

                            // Start the PatientHome activity
                            Intent intent = new Intent(LoginActivity.this, PatientHome.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("username", userUsername);
                            startActivity(intent);
                            finish(); // Close the login activity
                            return; // Exit loop if credentials are correct
                        } else {
                            loginPassword.setError("Invalid credentials");
                            loginPassword.requestFocus();
                        }
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
