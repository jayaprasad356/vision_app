package com.app.abcdapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity {

    MaterialButton verifybtn;
    String MobileNumber,code,mobile_registered;
    TextView tvMobileno,resend,Timer;
    Session session;
    LinearLayout tvwaiting,llnotRecived;
    OtpTextView otp_view;
    private String mVerificationId = "";
    public PhoneAuthProvider.ForceResendingToken mtoken;
    Activity activity;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String TAG = "OTPACT";
    String Email, Name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        activity = OtpActivity.this;
        session = new Session(activity);
        Email = getIntent().getStringExtra(Constant.EMAIL);
        Name = getIntent().getStringExtra(Constant.NAME);
        mAuth = FirebaseAuth.getInstance();


      //  MobileNumber = getIntent().getStringExtra(Constant.MOBILE);
       // MobileNumber = getString(session.getData(Constant.MOBILE));


        resend = findViewById(R.id.resend);
        Timer = findViewById(R.id.Timer);
        tvwaiting = findViewById(R.id.tvwaiting);
        llnotRecived = findViewById(R.id.llnotRecived);
        tvMobileno = findViewById(R.id.tvMobileno);
        verifybtn = findViewById(R.id.verifybtn);
        otp_view = findViewById(R.id.optView);


        tvMobileno.setText("+91"+session.getData(Constant.MOBILE));

        MobileNumber = tvMobileno.getText().toString();
        mobile_registered = session.getData(Constant.MOBILE_REGISTERED);






        startPhoneNumberVerification(MobileNumber);

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                Timer.setText(""+millisUntilFinished/1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvwaiting.setVisibility(View.GONE);
                llnotRecived.setVisibility(View.VISIBLE);
            }

        }.start();


//        verifybtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (otp_view.getOTP().length() == 6) {
////                     login();
//                    if (!mVerificationId.equals("")) {
//                        verifyPhoneNumberWithCode(mVerificationId, otp_view.getOTP().toString());
//
//                    } else {
//                        Toast.makeText(activity, "Invalid OTP", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } else {
//                    Toast.makeText(activity, "Enter OTP", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                }
//
//                // Show a message and update the UI
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String verificationId,
//                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//                mVerificationId = verificationId;
//                mtoken = token;
//
//
//            }
//        };
//        resend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resendVerificationCode("+91" + MobileNumber, mtoken);
//            }
//        });
//        startPhoneNumberVerification("+91" + MobileNumber);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String code = credential.getSmsCode();
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (code != null) {
                                otp_view.setOTP(code);



                                verifybtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(otp_view.getOTP().equals(code)) {
                                         //   Toast.makeText(OtpActivity.this, "working", Toast.LENGTH_SHORT).show();

                                            if (mobile_registered.equals("true")){

                                                Toast.makeText(activity, ""+mobile_registered, Toast.LENGTH_SHORT).show();
                                                Intent intent  = new Intent(activity,MainActivity.class);
                                                startActivity(intent);
                                                finish();


                                            }

                                            else {
                                                Toast.makeText(activity, "false",Toast.LENGTH_SHORT).show();
                                                Intent intent  = new Intent(activity,ProfileActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }


                                        }
                                    }
                                });
                                login();
                                // Update UI
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(activity, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    public void resendVerificationCode(String phoneNumber,
                                       PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,           //a reference to an activity if this method is in a custom service
                mCallbacks,
                token);        // resending with token got at previous call's `callbacks` method `onCodeSent`
        // [END start_phone_auth]
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            verifyPhoneNumberWithCode(mVerificationId,code);
        }

        private void verifyPhoneNumberWithCode(String verificationId, String code) {
            // [START verify_with_code]
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
            // [END verify_with_code]
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void login() {


//        Map<String, String> params = new HashMap<>();
//        //request
//
//        params.put(Constant.MOBILE, MobileNumber);
//
//
//        ApiConfig.RequestToVolley((result, response) -> {
//            if (result) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.d("OTP_RES", response);
//
//
//                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
//                        session.setBoolean(Constant.OFFLINE_MODE, false);
//                        session.setBoolean(Constant.OFFLINE_DATA, false);
//                        if (jsonObject.getBoolean(Constant.NEW_USER)) {
//                            Intent intent = new Intent(OtpActivity.this, ProfileActivity.class);
//                            intent.putExtra(Constant.MOBILE, MobileNumber);
//                            intent.putExtra(Constant.EMAIL, Email);
//                            intent.putExtra(Constant.NAME, Name);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//                            JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
//                            session.setBoolean("is_logged_in", true);
//                            session.setData(Constant.ID, jsonArray.getJSONObject(0).getString(Constant.ID));
//                            session.setData(Constant.NAME, jsonArray.getJSONObject(0).getString(Constant.NAME));
//                            session.setData(Constant.MOBILE, jsonArray.getJSONObject(0).getString(Constant.MOBILE));
//                            session.setData(Constant.DOB, jsonArray.getJSONObject(0).getString(Constant.DOB));
//                            session.setData(Constant.ADDRESS, jsonArray.getJSONObject(0).getString(Constant.ADDRESS));
//                            session.setData(Constant.GENDER, jsonArray.getJSONObject(0).getString(Constant.GENDER));
//                            session.setData(Constant.BANK_ID, jsonArray.getJSONObject(0).getString(Constant.BANK_ID));
//                            session.setData(Constant.EMAIL, jsonArray.getJSONObject(0).getString(Constant.EMAIL));
//                            startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
//                            finish();
//                        }
//                    } else {
//                        Toast.makeText(this, "" + jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            } else {
//                Toast.makeText(this, String.valueOf(response) + String.valueOf(result), Toast.LENGTH_SHORT).show();
//
//            }
//            //pass url
//        }, OtpActivity.this, Constant.LOGIN_URL, params, true);
//

    }

}