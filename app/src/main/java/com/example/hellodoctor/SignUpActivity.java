package com.example.hellodoctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    checkIfUserExistsAndRegister();
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        return validateName() && validateEmail() && validateUsername() && validatePassword();
    }

    private boolean validateName() {
        String val = signupName.getText().toString();
        if (TextUtils.isEmpty(val)) {
            signupName.setError("Name cannot be empty");
            signupName.requestFocus();
            return false;
        } else {
            signupName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = signupEmail.getText().toString().trim();

        // Regular expression to check if the email is valid
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(emailInput)) {
            signupEmail.setError("Email cannot be empty");
            signupEmail.requestFocus();
            return false;
        } else if (!emailInput.matches(emailPattern)) {
            signupEmail.setError("Invalid email address");
            signupEmail.requestFocus();
            return false;
        } else {
            signupEmail.setError(null); // Clear error if valid
            return true;
        }
    }


    private boolean validateUsername() {
        String val = signupUsername.getText().toString();
        if (TextUtils.isEmpty(val)) {
            signupUsername.setError("Username cannot be empty");
            signupUsername.requestFocus();
            return false;
        } else {
            signupUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = signupPassword.getText().toString();
        if (TextUtils.isEmpty(val)) {
            signupPassword.setError("Password cannot be empty");
            signupPassword.requestFocus();
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }
    }

    private void checkIfUserExistsAndRegister() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        String email = signupEmail.getText().toString();
        String username = signupUsername.getText().toString();

        // Check if the username or email already exists
        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    signupEmail.setError("Email already registered");
                    signupEmail.requestFocus();
                } else {
                    reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                signupUsername.setError("Username already taken");
                                signupUsername.requestFocus();
                            } else {
                                // If email and username are unique, register the user
                                registerUser();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SignUpActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUser() {
        String name = signupName.getText().toString();
        String email = signupEmail.getText().toString();
        String username = signupUsername.getText().toString();
        String password = signupPassword.getText().toString();

        // Create a new user object
        HelperClass user = new HelperClass(name, email, username, password);

        // Generate a unique key and add the user to the database
        reference.push().setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
