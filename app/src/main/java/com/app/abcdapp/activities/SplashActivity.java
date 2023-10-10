package com.app.abcdapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Session session;
    Activity activity;
    String link, description;
    String currentversion = "";
  //  private static final int REQUEST_CODE_READ_PHONE_STATE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = SplashActivity.this;
        session = new Session(activity);
        handler = new Handler();
        Log.d("APP_STATUS",session.getBoolean("is_logged_in") +"");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Check if the app has READ_PHONE_STATE permission
//            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                // Request the permission
//                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_READ_PHONE_STATE);
//            }else
//                checkVersion();
//
//
//        }

        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            currentversion = pInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        checkVersion();
    }

    private void checkVersion() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.FCM_ID, session.getData(Constant.FCM_ID));
        params.put(Constant.DEVICE_ID, Constant.getDeviceId(activity));
        params.put(Constant.APP_VERSION, currentversion);
        params.put(Constant.LATITUDE, session.getData(Constant.LATITUDE));
        params.put(Constant.LONGTITUDE, session.getData(Constant.LONGTITUDE));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("APP_UPDATE", response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        String codegenerate = "0", withdrawal_status = "0";
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        JSONArray jsonArray2 = jsonObject.getJSONArray(Constant.SETTINGS);
                        //session.setData(Constant.CURRENT_DATE,jsonObject.getString(Constant.CURRENT_DATE));
                        session.setData(Constant.CUSTOMER_SUPPORT_MOBILE,jsonObject.getString(Constant.CUSTOMER_SUPPORT_MOBILE));
                        session.setData(Constant.PAYMENT_LINK, jsonArray2.getJSONObject(0).getString(Constant.PAYMENT_LINK));
                        session.setData(Constant.WHATSAPP, jsonArray2.getJSONObject(0).getString(Constant.WHATSAPP));
                        session.setData(Constant.OFFER_IMAGE, jsonArray2.getJSONObject(0).getString(Constant.OFFER_IMAGE));
                        session.setData(Constant.JOB_DETAILS_LINK, jsonArray2.getJSONObject(0).getString(Constant.JOB_DETAILS_LINK));
                        session.setData(Constant.SYNC_TIME, jsonArray2.getJSONObject(0).getString(Constant.SYNC_TIME));
                        session.setInt(Constant.SYNC_CODES, Integer.parseInt(jsonArray2.getJSONObject(0).getString(Constant.SYNC_CODES)));
                        session.setData(Constant.REWARD, jsonArray2.getJSONObject(0).getString(Constant.REWARD));
                        session.setData(Constant.AD_SHOW_TIME, jsonArray2.getJSONObject(0).getString(Constant.AD_SHOW_TIME));
                        session.setData(Constant.MIN_WITHDRAWAL, jsonArray2.getJSONObject(0).getString(Constant.MIN_WITHDRAWAL));
                        session.setData(Constant.AD_STATUS, jsonArray2.getJSONObject(0).getString(Constant.AD_STATUS));
                        session.setData(Constant.FETCH_TIME, jsonArray2.getJSONObject(0).getString(Constant.FETCH_TIME));
                        session.setData(Constant.AD_REWARD_ID, jsonArray2.getJSONObject(0).getString(Constant.AD_REWARD_ID));
                        session.setData(Constant.JOIN_CODES, jsonArray2.getJSONObject(0).getString(Constant.JOIN_CODES));
                        session.setData(Constant.REFER_BONUS_CODES, jsonArray2.getJSONObject(0).getString(Constant.REFER_BONUS_CODES));
                        session.setData(Constant.REFER_BONUS_AMOUNT, jsonArray2.getJSONObject(0).getString(Constant.REFER_BONUS_AMOUNT));
                        session.setData(Constant.REFER_DESCRIPTION, jsonArray2.getJSONObject(0).getString(Constant.REFER_DESCRIPTION));
                        session.setData(Constant.AD_TYPE, jsonArray2.getJSONObject(0).getString(Constant.AD_TYPE));
                        session.setData(Constant.CHAMPION_TASK,jsonArray2.getJSONObject(0).getString(Constant.CHAMPION_TASK));
                        session.setData(Constant.CHAMPION_CODES,jsonArray2.getJSONObject(0).getString(Constant.CHAMPION_CODES));
                        session.setData(Constant.CHAMPION_SEARCH_COUNT,jsonArray2.getJSONObject(0).getString(Constant.CHAMPION_SEARCH_COUNT));
                        session.setData(Constant.CHAMPION_DEMO_LINK,jsonArray2.getJSONObject(0).getString(Constant.CHAMPION_DEMO_LINK));
                        session.setData(Constant.AMAIL_DEMO_LINK,jsonArray2.getJSONObject(0).getString(Constant.AMAIL_DEMO_LINK));
                        session.setData(Constant.REGULAR_DEMO_LINK,jsonArray2.getJSONObject(0).getString(Constant.REGULAR_DEMO_LINK));
                        session.setData(Constant.MAIN_CONTENT,jsonArray2.getJSONObject(0).getString(Constant.MAIN_CONTENT));
                        session.setData(Constant.AUTHKEY,jsonArray2.getJSONObject(0).getString(Constant.AUTHKEY));
                        session.setData(Constant.CUSTOMER_CARE,jsonArray2.getJSONObject(0).getString(Constant.CUSTOMER_CARE));

                        if (jsonObject.has(Constant.USER_DETAILS)) {
                            JSONArray userArray = jsonObject.getJSONArray(Constant.USER_DETAILS);
                            if (userArray.length() != 0) {
                                session.setData(Constant.STATUS, userArray.getJSONObject(0).getString(Constant.STATUS));
                                session.setData(Constant.TOTAL_REFERRALS, userArray.getJSONObject(0).getString(Constant.TOTAL_REFERRALS));
                                session.setData(Constant.WITHDRAWAL, userArray.getJSONObject(0).getString(Constant.WITHDRAWAL));
                                session.setData(Constant.CODE_GENERATE_TIME, userArray.getJSONObject(0).getString(Constant.CODE_GENERATE_TIME));
                                session.setData(Constant.JOINED_DATE, userArray.getJSONObject(0).getString(Constant.JOINED_DATE));
                                session.setData(Constant.REFER_BALANCE, userArray.getJSONObject(0).getString(Constant.REFER_BALANCE));
                                session.setData(Constant.BALANCE, userArray.getJSONObject(0).getString(Constant.BALANCE));
                                session.setData(Constant.TASK_TYPE, userArray.getJSONObject(0).getString(Constant.TASK_TYPE));
                                session.setData(Constant.TRIAL_COUNT, userArray.getJSONObject(0).getString(Constant.TRIAL_COUNT));
                                session.setData(Constant.TRIAL_EXPIRED, userArray.getJSONObject(0).getString(Constant.TRIAL_EXPIRED));
                                session.setData(Constant.CHAMPION_TASK_ELIGIBLE, userArray.getJSONObject(0).getString(Constant.CHAMPION_TASK_ELIGIBLE));
                                session.setData(Constant.MCG_TIMER, userArray.getJSONObject(0).getString(Constant.MCG_TIMER));
                                session.setData(Constant.SECURITY, userArray.getJSONObject(0).getString(Constant.SECURITY));
                                session.setData(Constant.ONGOING_SA_BALANCE, userArray.getJSONObject(0).getString(Constant.ONGOING_SA_BALANCE));
                                session.setData(Constant.SALARY_ADVANCE_BALANCE, userArray.getJSONObject(0).getString(Constant.SALARY_ADVANCE_BALANCE));
                                session.setData(Constant.SA_REFER_COUNT, userArray.getJSONObject(0).getString(Constant.SA_REFER_COUNT));
                                session.setData(Constant.REFUND_WALLET, userArray.getJSONObject(0).getString(Constant.REFUND_WALLET));
                                session.setData(Constant.TOTAL_REFUND, userArray.getJSONObject(0).getString(Constant.TOTAL_REFUND));
                                session.setData(Constant.EARN, userArray.getJSONObject(0).getString(Constant.EARN));
                                session.setData(Constant.REFER_CODE, userArray.getJSONObject(0).getString(Constant.REFER_CODE));
                                session.setInt(Constant.TODAY_CODES, Integer.parseInt(userArray.getJSONObject(0).getString(Constant.TODAY_CODES)));
                                session.setData(Constant.PROJECT_TYPE, userArray.getJSONObject(0).getString(Constant.PROJECT_TYPE));
                                session.setData(Constant.EARNINGS_WALLET, userArray.getJSONObject(0).getString(Constant.EARNINGS_WALLET));
                                session.setData(Constant.CH_DAILY_WALLET, userArray.getJSONObject(0).getString(Constant.CH_DAILY_WALLET));
                                session.setData(Constant.CH_MONTHLY_WALLET, userArray.getJSONObject(0).getString(Constant.CH_MONTHLY_WALLET));
                                session.setData(Constant.EARNINGS_WALLET, userArray.getJSONObject(0).getString(Constant.EARNINGS_WALLET));
                                session.setData(Constant.BONUS_WALLET, userArray.getJSONObject(0).getString(Constant.BONUS_WALLET));
                                session.setData(Constant.DAILY_WALLET, userArray.getJSONObject(0).getString(Constant.DAILY_WALLET));
                                session.setData(Constant.MONTHLY_WALLET, userArray.getJSONObject(0).getString(Constant.MONTHLY_WALLET));
                                session.setData(Constant.TARGET_REFERS, userArray.getJSONObject(0).getString(Constant.TARGET_REFERS));
                                session.setInt(Constant.TODAY_MAILS, Integer.parseInt(userArray.getJSONObject(0).getString(Constant.TODAY_MAILS)));
                                session.setInt(Constant.TOTAL_MAILS, Integer.parseInt(userArray.getJSONObject(0).getString(Constant.TOTAL_MAILS)));
                                if(session.getInt(Constant.TODAY_CODES) == 0 ){
                                    session.setBoolean(Constant.L_TRIAL_AVAILABLE,true);
                                }
                                if (!session.getBoolean(Constant.L_TRIAL_STATUS)){
                                    session.setData(Constant.LEVEL, userArray.getJSONObject(0).getString(Constant.LEVEL));
                                    session.setData(Constant.PER_CODE_VAL, userArray.getJSONObject(0).getString(Constant.PER_CODE_VAL));

                                }

                                session.setData(Constant.WORKED_DAYS, userArray.getJSONObject(0).getString(Constant.WORKED_DAYS));

                                if (jsonArray2.getJSONObject(0).getString(Constant.CODE_GENERATE).equals("1")) {
                                    codegenerate = userArray.getJSONObject(0).getString(Constant.CODE_GENERATE);
                                }
                                if (jsonArray2.getJSONObject(0).getString(Constant.WITHDRAWAL_STATUS).equals("1")) {
                                    withdrawal_status = userArray.getJSONObject(0).getString(Constant.WITHDRAWAL_STATUS);
                                }
                                session.setData(Constant.CODE_GENERATE, codegenerate);
                                session.setData(Constant.WITHDRAWAL_STATUS, withdrawal_status);
                            }
                        }
                        link = jsonArray.getJSONObject(0).getString(Constant.LINK);
                        description = jsonArray.getJSONObject(0).getString(Constant.DESCRIPTION);
                        String latestversion = jsonArray.getJSONObject(0).getString(Constant.VERSION);
                        if (session.getBoolean(Constant.NEW_IMPORT_DATA)){


                            if (Integer.parseInt(currentversion) >= Integer.parseInt(latestversion)) {
                                GotoActivity();

                            } else {
                                updateAlertDialog();
                            }

                        }
                        else {
                            if (session.getBoolean("is_logged_in")) {
                                Intent intent = new Intent(SplashActivity.this, ImportDataActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }


                    } else {
                        Log.d("MAINACTIVITY", jsonObject.getString(Constant.MESSAGE));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("APP_ERRROR", e.getMessage());
                }
            }

        }, activity, Constant.APPUPDATE_URL, params, false);

    }

    private void updateAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New update Available");
        builder.setMessage(description);
        builder.setCancelable(false);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }


    private void GotoActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Session session = new Session(SplashActivity.this);
                if (session.getBoolean("is_logged_in")) {
                    if (session.getData(Constant.STATUS).equals("2")) {
                        session.logoutUser(activity);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }


                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }


            }
        }, 2000);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case REQUEST_CODE_READ_PHONE_STATE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted, do what you want to do with the permission here
//                    checkVersion();
//
//                } else {
//                 return;
//
//                }
//                break;
//            }
//        }
//    }

}