package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class CheckInActivity extends AppCompatActivity {
    String Mobile,Password;
    Activity activity;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        activity = CheckInActivity.this;
        session = new Session(activity);
        Mobile = session.getData(Constant.MOBILE);
        Password = session.getData(Constant.PASSWORD);

        Login();
    }
    private void Login() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.MOBILE,Mobile);
        params.put(Constant.PASSWORD,Password);
        params.put(Constant.DEVICE_ID,Constant.getDeviceId(activity));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        if (jsonObject.getBoolean(Constant.USER_VERIFY)){
                            if (jsonObject.getBoolean(Constant.DEVICE_VERIFY)){
                                String codegenerate = "0",withdrawal_status = "0";
                                JSONArray userArray = jsonObject.getJSONArray(Constant.DATA);
                                JSONArray setArray = jsonObject.getJSONArray(Constant.SETTINGS);
                                session.setData(Constant.SYNC_TIME,setArray.getJSONObject(0).getString(Constant.SYNC_TIME));
                                session.setInt(Constant.SYNC_CODES,Integer.parseInt(setArray.getJSONObject(0).getString(Constant.SYNC_CODES)));
                                session.setData(Constant.REWARD,setArray.getJSONObject(0).getString(Constant.REWARD));
                                session.setData(Constant.AD_SHOW_TIME,setArray.getJSONObject(0).getString(Constant.AD_SHOW_TIME));
                                session.setData(Constant.MIN_WITHDRAWAL,setArray.getJSONObject(0).getString(Constant.MIN_WITHDRAWAL));
                                session.setData(Constant.AD_STATUS,setArray.getJSONObject(0).getString(Constant.AD_STATUS));
                                session.setData(Constant.FETCH_TIME,setArray.getJSONObject(0).getString(Constant.FETCH_TIME));
                                session.setData(Constant.AD_REWARD_ID,setArray.getJSONObject(0).getString(Constant.AD_REWARD_ID));
                                session.setData(Constant.JOIN_CODES,setArray.getJSONObject(0).getString(Constant.JOIN_CODES));
                                session.setData(Constant.REFER_BONUS_CODES,setArray.getJSONObject(0).getString(Constant.REFER_BONUS_CODES));
                                session.setData(Constant.REFER_BONUS_AMOUNT,setArray.getJSONObject(0).getString(Constant.REFER_BONUS_AMOUNT));
                                session.setData(Constant.REFER_DESCRIPTION,setArray.getJSONObject(0).getString(Constant.REFER_DESCRIPTION));
                                session.setData(Constant.CHAMPION_TASK,setArray.getJSONObject(0).getString(Constant.CHAMPION_TASK));
                                session.setData(Constant.CHAMPION_CODES,setArray.getJSONObject(0).getString(Constant.CHAMPION_CODES));
                                session.setData(Constant.CHAMPION_SEARCH_COUNT,setArray.getJSONObject(0).getString(Constant.CHAMPION_SEARCH_COUNT));
                                session.setData(Constant.CHAMPION_DEMO_LINK,setArray.getJSONObject(0).getString(Constant.CHAMPION_DEMO_LINK));
                                session.setData(Constant.REGULAR_DEMO_LINK,setArray.getJSONObject(0).getString(Constant.REGULAR_DEMO_LINK));

                                if (setArray.getJSONObject(0).getString(Constant.CODE_GENERATE).equals("1")){
                                    codegenerate = userArray.getJSONObject(0).getString(Constant.CODE_GENERATE);
                                }
                                if (setArray.getJSONObject(0).getString(Constant.WITHDRAWAL_STATUS).equals("1")){
                                    withdrawal_status = userArray.getJSONObject(0).getString(Constant.WITHDRAWAL_STATUS);
                                }
                                session.setBoolean(Constant.CHECKIN,false);

                                Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                                session.setUserData(userArray.getJSONObject(0).getString(Constant.ID),
                                        userArray.getJSONObject(0).getString(Constant.NAME),
                                        userArray.getJSONObject(0).getString(Constant.MOBILE),
                                        userArray.getJSONObject(0).getString(Constant.PASSWORD),
                                        userArray.getJSONObject(0).getString(Constant.DOB),
                                        userArray.getJSONObject(0).getString(Constant.EMAIL),
                                        userArray.getJSONObject(0).getString(Constant.CITY),
                                        userArray.getJSONObject(0).getString(Constant.REFERRED_BY),
                                        userArray.getJSONObject(0).getString(Constant.EARN),
                                        userArray.getJSONObject(0).getString(Constant.WITHDRAWAL),
                                        userArray.getJSONObject(0).getString(Constant.TOTAL_REFERRALS),
                                        userArray.getJSONObject(0).getInt(Constant.TODAY_CODES),
                                        userArray.getJSONObject(0).getInt(Constant.TOTAL_CODES),
                                        userArray.getJSONObject(0).getString(Constant.BALANCE),
                                        userArray.getJSONObject(0).getString(Constant.REFER_BALANCE),
                                        userArray.getJSONObject(0).getString(Constant.DEVICE_ID),
                                        userArray.getJSONObject(0).getString(Constant.STATUS),
                                        userArray.getJSONObject(0).getString(Constant.REFER_CODE),
                                        userArray.getJSONObject(0).getString(Constant.REFER_BONUS_SENT),
                                        codegenerate,
                                        userArray.getJSONObject(0).getString(Constant.CODE_GENERATE_TIME),
                                        userArray.getJSONObject(0).getString(Constant.LAST_UPDATED),
                                        userArray.getJSONObject(0).getString(Constant.JOINED_DATE),
                                        withdrawal_status,userArray.getJSONObject(0).getString(Constant.TASK_TYPE),
                                        userArray.getJSONObject(0).getString(Constant.TRIAL_EXPIRED),
                                        userArray.getJSONObject(0).getString(Constant.CHAMPION_TASK_ELIGIBLE),
                                        userArray.getJSONObject(0).getString(Constant.TRIAL_COUNT),
                                        userArray.getJSONObject(0).getString(Constant.MCG_TIMER),
                                        userArray.getJSONObject(0).getString(Constant.SECURITY),
                                        userArray.getJSONObject(0).getString(Constant.ONGOING_SA_BALANCE),
                                        userArray.getJSONObject(0).getString(Constant.SALARY_ADVANCE_BALANCE),
                                        userArray.getJSONObject(0).getString(Constant.SA_REFER_COUNT),
                                        userArray.getJSONObject(0).getString(Constant.REFUND_WALLET),
                                        userArray.getJSONObject(0).getString(Constant.TOTAL_REFUND),
                                        userArray.getJSONObject(0).getString(Constant.LEVEL),
                                        userArray.getJSONObject(0).getString(Constant.PER_CODE_VAL),
                                        userArray.getJSONObject(0).getString(Constant.WORKED_DAYS),
                                        userArray.getJSONObject(0).getString(Constant.PROJECT_TYPE),
                                        userArray.getJSONObject(0).getString(Constant.EARNINGS_WALLET),
                                        userArray.getJSONObject(0).getString(Constant.BONUS_WALLET),
                                        userArray.getJSONObject(0).getInt(Constant.TODAY_MAILS),
                                        userArray.getJSONObject(0).getInt(Constant.TOTAL_MAILS),
                                        userArray.getJSONObject(0).getString(Constant.TARGET_REFERS),
                                        userArray.getJSONObject(0).getString(Constant.DAILY_WALLET),
                                        userArray.getJSONObject(0).getString(Constant.MONTHLY_WALLET),
                                        userArray.getJSONObject(0).getString(Constant.CH_DAILY_WALLET),
                                        userArray.getJSONObject(0).getString(Constant.CH_MONTHLY_WALLET)
                                );
                                if (session.getBoolean(Constant.NEW_IMPORT_DATA)){
                                    session.setBoolean("is_logged_in", true);
                                    startActivity(new Intent(activity, MainActivity.class));
                                    finish();

                                }else {
                                    startActivity(new Intent(activity, ImportDataActivity.class));
                                    finish();
                                }

                            }
                            else {

                                showAlertdialog();
                            }

                        }else {
                            ApprovalAlertdialog(jsonObject.getString(Constant.MESSAGE));
                        }


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
        }, activity, Constant.LOGIN_URL, params,true);



    }
    private void showAlertdialog() {


        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Would you request to change device ?");
        builder.setTitle("Device verification failed !");
        builder.setCancelable(false);
        builder.setPositiveButton("Send request to admin", (DialogInterface.OnClickListener) (dialog, which) -> {
            changeDeviceApi(dialog);
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }
    private void changeDeviceApi(DialogInterface dialog)
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.MOBILE,Mobile);
        params.put(Constant.PASSWORD,Password);
        params.put(Constant.DEVICE_ID,Constant.getDeviceId(activity));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        dialog.cancel();

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
        }, activity, Constant.CHANGE_DEVICE_LIST_URL, params,true);



    }

    private void ApprovalAlertdialog(String msg) {


        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setTitle("Failed to Login");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }
}