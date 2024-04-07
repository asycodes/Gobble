package com.sutd.t4app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText firstName = findViewById(R.id.firstNameEditText);
        EditText lastName = findViewById(R.id.lastNameEditText);
        EditText email = findViewById(R.id.emailEditText);
        EditText pass = findViewById(R.id.passwordEditText);

        Button submit = findViewById(R.id.signUpButton);

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameValue = firstName.getText().toString();
                String lastNameValue = lastName.getText().toString();
                String emailValue = email.getText().toString();
                String passValue = pass.getText().toString();

                // OKOK now we will need to do validation!!!!
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    // The email address is in a valid format.
                } else {
                    // The email address is in an invalid format.
                }

                if (passValue.matches(passwordPattern)) {
                    // The password is considered strong.
                } else {
                    // The password does not meet the criteria.
                }
            }
        });
    }

}
