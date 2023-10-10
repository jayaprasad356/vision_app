package com.app.abcdapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.app.abcdapp.R;
import com.app.abcdapp.helper.Utils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private LinearLayout llPhone, llOtp, llNewPassword;
    private Button btnVerify, btnOtpVerify, btnSubmit;
    private EditText edMobile, edOtp, edNewPassword, edConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        llPhone = findViewById(R.id.llphone);
        llOtp = findViewById(R.id.llOtp);
        llNewPassword = findViewById(R.id.llNewpassword);

        edMobile = findViewById(R.id.edMobile);
        edOtp = findViewById(R.id.otp);
        edNewPassword = findViewById(R.id.ednewpassword);
        edConfirmPassword = findViewById(R.id.edConfirmpassword);


        // Show/Hide password
        edNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
        edConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
        Utils.setHideShowPassword(edConfirmPassword);
        Utils.setHideShowPassword(edNewPassword);



        btnVerify = findViewById(R.id.btnVerify);
        btnOtpVerify = findViewById(R.id.btnOtpVerify);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initial visibility states
        llOtp.setVisibility(View.GONE);
        llNewPassword.setVisibility(View.GONE);

        // Verify button click
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edMobile.getText().toString().trim().isEmpty()) {
                    edMobile.setError(getString(R.string.enter_mobile));
                    edMobile.requestFocus();
                } else if (edMobile.getText().toString().trim().length() != 10) {
                    edMobile.setError(getString(R.string.enter_valid_mobile));
                    edMobile.requestFocus();
                } else {
                    // TODO: Implement your mobile number verification logic here
                    // For now, let's just show the OTP section for demonstration.
                    llPhone.setVisibility(View.GONE);
                    llOtp.setVisibility(View.VISIBLE);
                }
            }
        });

        // OTP Verify button click
        btnOtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOtp = edOtp.getText().toString().trim();

                // TODO: Implement your OTP verification logic here
                // For now, let's just show the New Password section for demonstration.
                llOtp.setVisibility(View.GONE);
                llNewPassword.setVisibility(View.VISIBLE);
            }
        });

        // Submit button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = edNewPassword.getText().toString().trim();
                String confirmPassword = edConfirmPassword.getText().toString().trim();

                // Check if the new password and confirm password are not empty and have at least 5 characters.
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords cannot be empty.", Toast.LENGTH_SHORT).show();
                } else if (newPassword.length() < 5) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password must be at least 5 characters long.", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Implement your actual password reset logic here
                    // For now, let's just show a toast message for demonstration.
                    Toast.makeText(ForgotPasswordActivity.this, "Password Reset Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
