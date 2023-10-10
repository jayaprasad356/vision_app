package com.app.abcdapp.fragment;

import static com.app.abcdapp.helper.Constant.AD_AVAILABLE;
import static com.app.abcdapp.helper.Constant.CHAMPION_TASK;
import static com.app.abcdapp.helper.Constant.PER_CODE_VAL;
import static com.app.abcdapp.helper.Constant.TASK_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.abcdapp.Adapter.LevelsAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.activities.LoginActivity;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.activities.NotificaionActivity;
import com.app.abcdapp.activities.SplashActivity;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.DatabaseHelper;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.GenerateCodes;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WatchadRegularFragment extends Fragment {



    Handler handler;
    Activity activity;
    Session session;



    Button btnGenerate, btnsyncNow, btnBuyServer, btnGetFreeCodes,btnWatchAd;
    ImageButton sharebtn;
    ImageView ivInfo;

    DatabaseHelper databaseHelper;
    ArrayList<GenerateCodes> generateCodes = new ArrayList<GenerateCodes>();
    ScrollView frame;
    String Idnumber = "";
    long code_generate_time = 0;
    public static Dialog dialog = null;
    Button btnFindMissing, btnChampiontask, btnNavChampionTask;
    TextView tvBalance;
    View root;
    LinearLayout championLayout;
    private String AdId = "";
    TextView tvCodes;
    CircularProgressIndicator cbCodes, cbcodesTrail;
    TextView tvHightlight, tvInfo, tvShareEarnings,tvLevels;
    ProgressDialog progressDialog;
    long st_timestamp;
    ClipboardManager clipBoard;
    LinearLayout lltrail, llPayed;
    String RandomId;
    DatabaseReference reference;
    ImageView imgNotification,ivAd;
    LinearLayout shareLayout,level_linear;
    private boolean isBackgroundBlack = false;

    TextView tvTodayCodes, tvTotalCodes, tvHistorydays, tvTrialPeriod;
    private int remainingTime = 24;








    public WatchadRegularFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root = inflater.inflate(R.layout.fragment_watchad_regular, container, false);

        activity = getActivity();
        session = new Session(activity);



        if (session.getData(Constant.SECURITY).equals("1")) {
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
            );
        }



        progressDialog = new ProgressDialog(activity);
        if (progressDialog != null && progressDialog.isShowing() && getActivity() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }



        if (session.getData(Constant.CHAMPION_TASK_ELIGIBLE).equals("1") && session.getData(CHAMPION_TASK).equals("1")) {
//            championLayout.setVisibility(View.VISIBLE);
        }
        handler = new Handler();
        try {
            code_generate_time = Long.parseLong(session.getData(Constant.CODE_GENERATE_TIME)) * 1000;


        } catch (Exception e) {
            code_generate_time = 3 * 1000;


        }

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.customdia2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();


        btnGenerate = root.findViewById(R.id.btnGenerate);
        btnWatchAd = root.findViewById(R.id.btnWatchAd);

        btnWatchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAd.setVisibility(View.VISIBLE);
                btnGenerate.setVisibility(View.VISIBLE);
                btnWatchAd.setVisibility(View.GONE);
                btnGenerate.setEnabled(false);
                startCountdown();
            }
        });




        GotoActivity();




        tvBalance = root.findViewById(R.id.tvBalance);
        tvLevels = root.findViewById(R.id.tvLevels);
        btnGenerate = root.findViewById(R.id.btnGenerate);
        btnFindMissing = root.findViewById(R.id.btnFindMissing);
        btnNavChampionTask = root.findViewById(R.id.btnChampionTaskNav);
        sharebtn = root.findViewById(R.id.sharebtn);
        frame = root.findViewById(R.id.frame);
        ivInfo = root.findViewById(R.id.ivInfo);
        tvHightlight = root.findViewById(R.id.tvHightlight);
        tvInfo = root.findViewById(R.id.tvInfo);
        tvShareEarnings = root.findViewById(R.id.tvShareEarnings);
        shareLayout = root.findViewById(R.id.shareLayout);
        level_linear = root.findViewById(R.id.level_linear);
        btnGetFreeCodes = root.findViewById(R.id.btnGetFreeCodes);
        tvTodayCodes = root.findViewById(R.id.tvTodayCodes);
        tvTotalCodes = root.findViewById(R.id.tvTotalCodes);
        tvHistorydays = root.findViewById(R.id.tvHistorydays);
        tvTrialPeriod = root.findViewById(R.id.tvTrialPeriod);
        btnsyncNow = root.findViewById(R.id.btnsyncNow);
        tvCodes = root.findViewById(R.id.tvCodes);
        cbCodes = root.findViewById(R.id.cbCodes);
        imgNotification = root.findViewById(R.id.imgNotification);
        ivAd = root.findViewById(R.id.ivAd);

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NotificaionActivity.class);
                startActivity(intent);
            }
        });


        setCodeValue();




        btnsyncNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.getBoolean(AD_AVAILABLE)) {
                    btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg_disabled));
                    btnsyncNow.setEnabled(false);
                    walletApi();

                } else {
                    Toast.makeText(activity, "Please watch ad and claim ad bonus then sync codes", Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.setData(Constant.MY_TASK_ADS,"1");


                if (btnsyncNow.isEnabled()) {
                    Toast.makeText(activity, "Please Sync Your Codes", Toast.LENGTH_SHORT).show();

                } else {


                        long ed_timestamp = System.currentTimeMillis();

                        long difference = ed_timestamp - st_timestamp;
                        long seconds = difference / 1000;
                        int speed_time = Integer.parseInt(session.getData(Constant.MCG_TIMER)) - (int) seconds;
                        int positiveValue = Math.max(speed_time, 0);
                        if (ApiConfig.isConnected(activity)) {
                            if (session.getData(Constant.STATUS).equals("0")) {
                                if (session.getInt(Constant.REGULAR_TRIAL_COUNT) >= 10) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setMessage("Congratulations. You have successfully completed your trail codes. Chat with us to start actual job by purchasing database.")
                                            .setCancelable(false)
                                            .setPositiveButton("Chat Now", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                //   checkJoining();

                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                } else {
                                    session.setInt(Constant.REGULAR_TRIAL_COUNT, session.getInt(Constant.REGULAR_TRIAL_COUNT) + 1);

                                    if (session.getInt(Constant.REGULAR_TRIAL_COUNT) >= 10) {
                                       // trialCompleted();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(Constant.MCG_TIMER, positiveValue);
                                        bundle.putString(Constant.TASK_TYPE, Constant.REGULAR);
                                        Fragment fragment = new GenrateQRFragment();
                                        fragment.setArguments(bundle);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.Container, fragment);
                                        ft.commit();
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(Constant.MCG_TIMER, positiveValue);
                                        bundle.putString(Constant.TASK_TYPE, Constant.REGULAR);
                                        Fragment fragment = new GenrateQRFragment();
                                        fragment.setArguments(bundle);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.Container, fragment);
                                        ft.commit();

                                    }

                                }
                            } else {
                                if (session.getData(Constant.CODE_GENERATE).equals("1")) {
                                    session.setInt(Constant.CODES, session.getInt(Constant.CODES) + Integer.parseInt(session.getData(PER_CODE_VAL)));
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constant.MCG_TIMER, positiveValue);
                                    bundle.putString(Constant.TASK_TYPE, Constant.REGULAR);
                                    Fragment fragment = new GenrateQRFragment();
                                    fragment.setArguments(bundle);
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.Container, fragment);
                                    ft.commit();

                                } else {
                                    Toast.makeText(activity, "You are Restricted for Generating Code", Toast.LENGTH_SHORT).show();

                                }

                            }


                        }



                }


            }
        });



        if (session.getData(Constant.THEME).equals("dark")) {
            setDarkMode();

        } else {
            setLightMode();

        }

        return root;
    }

    private void GotoActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.cancel();


            }
        }, 2000);
    }



    public void walletApi() {
        if (ApiConfig.isConnected(activity)) {
            progressDialog.setTitle("Codes are Syncing");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (session.getInt(Constant.CODES) != 0) {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
                params.put(Constant.CODES, session.getInt(Constant.CODES) + "");
                params.put(TASK_TYPE, "regular");
                ApiConfig.RequestToVolley((result, response) -> {
                    Log.d("WALLET_RES", response);
                    if (result) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean(Constant.SUCCESS)) {

                                session.setInt(Constant.CODES, 0);
                                session.setInt(Constant.TODAY_CODES, Integer.parseInt(jsonObject.getString(Constant.TODAY_CODES)));
                                session.setInt(Constant.TOTAL_CODES, Integer.parseInt(jsonObject.getString(Constant.TOTAL_CODES)));
                                session.setData(Constant.BALANCE, jsonObject.getString(Constant.BALANCE));
                                session.setData(Constant.REFUND_WALLET, jsonObject.getString(Constant.REFUND_WALLET));

                                session.setData(Constant.STATUS, jsonObject.getString(Constant.STATUS));
                                setCodeValue();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                }, 2000);

                            } else {
                                btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg));
                                btnsyncNow.setEnabled(true);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        try {
                                            Toast.makeText(activity, "" + jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }, 2000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, activity, Constant.WALLET_URL, params, true);


            }

        } else {
            btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg));
            btnsyncNow.setEnabled(true);
        }
    }


    private void setCodeValue() {
        if (session.getInt(Constant.CODES) >= session.getInt(Constant.SYNC_CODES)) {
            btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg));
            btnsyncNow.setEnabled(true);

        } else {
            btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg_disabled));
            btnsyncNow.setEnabled(false);

        }
        tvCodes.setText(session.getInt(Constant.CODES) + "");

        if (tvCodes.getText().toString().equals("0") || tvCodes.getText().toString().equals("1") || tvCodes.getText().toString().equals("2") ) {
            tvHightlight.setVisibility(View.VISIBLE);
        }

        else {
            tvHightlight.setVisibility(View.GONE);
        }

        cbCodes.setMax(session.getInt(Constant.SYNC_CODES));
        cbCodes.setProgress(session.getInt(Constant.CODES));
        try {
            tvTodayCodes.setText(session.getInt(Constant.TODAY_CODES) + " + " + session.getInt(Constant.CODES));
            tvTotalCodes.setText(session.getInt(Constant.TOTAL_CODES) + " + " + session.getInt(Constant.CODES));
            double current_bal = (double) (session.getInt(Constant.CODES) * 0.17);
            tvBalance.setText(session.getData(Constant.BALANCE) + " + " + String.format("%.2f", current_bal) + "");
        } catch (Exception e) {
        }
        tvHistorydays.setText(session.getData(Constant.WORKED_DAYS));
        tvLevels.setText(session.getData(Constant.LEVEL));

    }


    private void setLightMode() {


        RelativeLayout rlHome = root.findViewById(R.id.rlHome);
        LinearLayout l1 = root.findViewById(R.id.l1);
        LinearLayout llBalance = root.findViewById(R.id.llBalance);



        CardView cardView = root.findViewById(R.id.card1);
        // cardBackgroundColor change to black
        cardView.setCardBackgroundColor(Color.parseColor("#4d4a75"));
        rlHome.setBackgroundColor(Color.parseColor("#4d4a75"));
        l1.setBackgroundColor(Color.parseColor("#4d4a75"));
        btnGenerate.setBackgroundColor(Color.parseColor("#0288D1"));
        cbCodes.setTrackColor(Color.parseColor("#FFFFFF"));
        llBalance.setBackgroundColor(Color.parseColor("#f3c234"));


    }

    private void setDarkMode() {


        RelativeLayout rlHome = root.findViewById(R.id.rlHome);
        LinearLayout l1 = root.findViewById(R.id.l1);
        LinearLayout llBalance = root.findViewById(R.id.llBalance);




        CardView cardView = root.findViewById(R.id.card1);
        // cardBackgroundColor change to black
        cardView.setCardBackgroundColor(Color.parseColor("#2D2B2B"));
        rlHome.setBackgroundColor(Color.parseColor("#2D2B2B"));
        l1.setBackgroundColor(Color.parseColor("#2D2B2B"));
        btnGenerate.setBackgroundColor(Color.parseColor("#565657"));
        cbCodes.setTrackColor(Color.parseColor("#D4D4D4"));
        llBalance.setBackgroundColor(Color.parseColor("#A27801"));

    }

    private void startCountdown() {
        btnGenerate.setEnabled(false); // Disable the button during countdown

        new CountDownTimer(remainingTime * 1000, 1000) { // Countdown for remainingTime seconds with 1 second intervals
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) (millisUntilFinished / 1000); // Update remaining time

                // Update button text with remaining time
                btnGenerate.setText("" + remainingTime + "");
            }

            @Override
            public void onFinish() {
                btnGenerate.setEnabled(true); // Enable the button after countdown finishes
                btnGenerate.setText("Generate"); // Reset button text
            }
        }.start();
    }


}