package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ImageView backimg;
    EditText etName,etDob,etEmail,etRefferalcode,etCity,EtPassword;
    Button btnRegister;
    Spinner spinGender,spinVillageName,spinDistrict;
    Activity activity;
    String mobile;
    Session session;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = ProfileActivity.this;
        session = new Session(activity);

        backimg = findViewById(R.id.backimg);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etDob = findViewById(R.id.etDob);
        etRefferalcode = findViewById(R.id.etRefferalcode);
        etCity = findViewById(R.id.etCity);
        EtPassword = findViewById(R.id.EtPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                register();
            }
        });

        mobile = session.getData(Constant.MOBILE);





    }

    private void register() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.NAME,etName.getText().toString().trim());
        params.put(Constant.MOBILE,mobile);
        params.put(Constant.DOB,etDob.getText().toString().trim());
        params.put(Constant.EMAIL,etEmail.getText().toString().trim());
        params.put(Constant.CITY,etCity.getText().toString().trim());
        params.put(Constant.PASSWORD,EtPassword.getText().toString().trim());
        params.put(Constant.REFER_CODE,etRefferalcode.getText().toString().trim());
        params.put(Constant.DEVICE_ID,Constant.getDeviceId(activity));


        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("SIGNUP_RES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                      Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
//                        session.setData(Constant.MOBILE,EtPhoneNo.getText().toString().trim());
//                        session.setData(Constant.PASSWORD,EtPassword.getText().toString().trim());
//                        session.setBoolean(Constant.CHECKIN,true);
//                        showAlertdialog();

                    }
                    else {
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, String.valueOf(response) +String.valueOf(result), Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, ProfileActivity.this, Constant.USER_SIGNUP, params,true);


    }


}