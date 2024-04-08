package com.sutd.t4app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText firstName = findViewById(R.id.firstNameEditText);
        TextInputLayout firstNameInputLayout = findViewById(R.id.firstNameTextInputLayout);

        EditText lastName = findViewById(R.id.lastNameEditText);
        TextInputLayout lastNameTextInputLayout = findViewById(R.id.lastNameEditTextTextInputLayout);

        EditText email = findViewById(R.id.emailEditText);
        TextInputLayout emailTextInputLayout = findViewById(R.id.emailTextInputLayout);

        EditText pass = findViewById(R.id.passwordEditText);
        TextInputLayout passTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        Button submit = findViewById(R.id.signUpButton);

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameValue = firstName.getText().toString();
                String lastNameValue = lastName.getText().toString();
                String emailValue = email.getText().toString();
                String passValue = pass.getText().toString();
                boolean isValid = true; // default true

                // OKOK now we will need to do validation!!!!
                if (firstNameValue.isEmpty()) {
                    firstName.setError("First name is required");
                    isValid = false;
                }else{
                    firstName.setError(null);
                }

                if (lastNameValue.isEmpty()) {
                    lastName.setError("Last name is required");
                    isValid = false;
                }else{
                    lastName.setError(null);
                }
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    // The email address is in a valid format.
                    emailTextInputLayout.setError(null);
                } else {
                    //
                    emailTextInputLayout.setError("Invalid email format");
                    isValid = false;

                }

                if (passValue.matches(passwordPattern)) {
                    // The password is considered strong.
                    passTextInputLayout.setError(null);
                } else {
                    // The password does not meet the criteria.
                    passTextInputLayout.setError("Password is WEAK");
                    isValid = false;

                }

                if (isValid) {
                    // All inputs are valid, proceed with the sign-up process
                    Log.d("signup check","all valid");

                    // do a check if theres existing user
                    // maybe do an email verification? not a must but itll be good
                    // captcha to prevent traffic abuse
                    // once that is done we create a userProfile
                    // go to profilequestion fragment
                    //
                }
            }
        });
    }

}
