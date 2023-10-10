package com.app.abcdapp.fragment;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.app.abcdapp.chat.constants.IConstants.CATEGORY;
import static com.app.abcdapp.chat.constants.IConstants.EXTRA_USER_ID;
import static com.app.abcdapp.chat.constants.IConstants.NAME;
import static com.app.abcdapp.chat.constants.IConstants.TICKET_ID;
import static com.app.abcdapp.chat.constants.IConstants.TYPE;
import static com.app.abcdapp.helper.Constant.AD_AVAILABLE;
import static com.app.abcdapp.helper.Constant.AD_STATUS;
import static com.app.abcdapp.helper.Constant.AD_TYPE;
import static com.app.abcdapp.helper.Constant.CHAMPION_TASK;
import static com.app.abcdapp.helper.Constant.DESCRIPTION;
import static com.app.abcdapp.helper.Constant.L_TRIAL_AVAILABLE;
import static com.app.abcdapp.helper.Constant.L_TRIAL_STATUS;
import static com.app.abcdapp.helper.Constant.PER_CODE_VAL;
import static com.app.abcdapp.helper.Constant.TASK_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.LevelsAdapter;
import com.app.abcdapp.PaymentLinkActivity;
import com.app.abcdapp.activities.LoadWebView2Activity;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.activities.NotificaionActivity;
import com.app.abcdapp.activities.ReferEarnActivity;
import com.app.abcdapp.chat.MessageActivity;
import com.app.abcdapp.chat.models.Ticket;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.DatabaseHelper;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.java.GenericTextWatcher;
import com.app.abcdapp.R;
import com.app.abcdapp.model.GenerateCodes;
import com.app.abcdapp.model.Levels;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment implements TextToSpeech.OnInitListener {

    TextView  tvTodayCodes, tvTotalCodes, tvHistorydays, tvTrialPeriod ;
    EditText  edPincode, edCity;
    Button btnGenerate, btnsyncNow, btnBuyServer, btnGetFreeCodes;
    ImageView ivInfo;
    private TextToSpeech textToSpeech;


    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four, otp_textbox_five, otp_textbox_six, otp_textbox_seven, otp_textbox_eight, otp_textbox_nine, otp_textbox_ten;
    DatabaseHelper databaseHelper;
    ArrayList<GenerateCodes> generateCodes = new ArrayList<GenerateCodes>();
    Session session;
    Activity activity;
    ScrollView frame;

    String Idnumber = "";

    Handler handler;
    long code_generate_time = 0;
    public static Dialog dialog = null;
    Button btnFindMissing, btnChampiontask, btnNavChampionTask;
    TextView tvBalance;
    View root;
    LinearLayout championLayout;
    private String AdId = "";
    TextView tvCodes;
    CircularProgressIndicator cbCodes, cbcodesTrail;
    TextView  tvInfo,tvLevels;
    ProgressDialog progressDialog;
    long st_timestamp;
    ClipboardManager clipBoard;
    LinearLayout lltrail, llPayed;
    String RandomId;
    DatabaseReference reference;
    ImageView imgNotification;
    LinearLayout shareLayout,level_linear;
    private boolean isBackgroundBlack = false;
    RecyclerView recycler;
    LevelsAdapter levelsAdapter;
    int totaltype = 0,totaltext = 0;
    int namecount = 0,idcount = 0,citycount = 0 , pincodecount = 0;


    TextView tvMonthlyBalance,tvDailyBalance;

    TextView tvField1,tvField2,tvField3,tvField4,tvField5;
    TextView tvCopy1,tvCopy2,tvCopy3,tvCopy4,tvCopy5;
    TextView tvGenerate1,tvGenerate2,tvGenerate3,tvGenerate4,tvGenerate5;
    ImageButton ibSpeak1,ibSpeak2,ibSpeak3,ibSpeak4,ibSpeak5;
    TextView tvPaste1,tvPaste2,tvPaste3,tvPaste4,tvPaste5;
    ImageButton ibCheck1,ibCheck2,ibCheck3,ibCheck4,ibCheck5;
    ImageButton ibPaste1,ibPaste2,ibPaste3,ibPaste4,ibPaste5;

    private boolean isCheck1 = false,isCheck2 = false,isCheck3 = false,isCheck4 = false,isCheck5 = false;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);


        activity = getActivity();
        session = new Session(activity);
     //   session.setInt(Constant.CODES,0);
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









        clipBoard = (ClipboardManager) getActivity().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
        databaseHelper = new DatabaseHelper(getActivity());


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
        GotoActivity();



        tvBalance = root.findViewById(R.id.tvBalance);
        tvLevels = root.findViewById(R.id.tvLevels);

        edPincode = root.findViewById(R.id.edPincode);
        edCity = root.findViewById(R.id.edCity);
        tvTodayCodes = root.findViewById(R.id.tvTodayCodes);
        tvTotalCodes = root.findViewById(R.id.tvTotalCodes);
        tvHistorydays = root.findViewById(R.id.tvHistorydays);


        tvField1 = root.findViewById(R.id.tvField1);
        tvField2 = root.findViewById(R.id.tvField2);
        tvField3 = root.findViewById(R.id.tvField3);
        tvField4 = root.findViewById(R.id.tvField4);
        tvField5 = root.findViewById(R.id.tvField5);
        tvCopy1 = root.findViewById(R.id.tvCopy1);
        tvCopy2 = root.findViewById(R.id.tvCopy2);
        tvCopy3 = root.findViewById(R.id.tvCopy3);
        tvCopy4 = root.findViewById(R.id.tvCopy4);
        tvCopy5 = root.findViewById(R.id.tvCopy5);
        tvGenerate1 = root.findViewById(R.id.tvGenerate1);
        tvGenerate2 = root.findViewById(R.id.tvGenerate2);
        tvGenerate3 = root.findViewById(R.id.tvGenerate3);
        tvGenerate4 = root.findViewById(R.id.tvGenerate4);
        tvGenerate5 = root.findViewById(R.id.tvGenerate5);
        ibSpeak1 = root.findViewById(R.id.ibSpeak1);
        ibSpeak2 = root.findViewById(R.id.ibSpeak2);
        ibSpeak3 = root.findViewById(R.id.ibSpeak3);
        ibSpeak4 = root.findViewById(R.id.ibSpeak4);
        ibSpeak5 = root.findViewById(R.id.ibSpeak5);
        tvPaste1 = root.findViewById(R.id.tvPaste1);
        tvPaste2 = root.findViewById(R.id.tvPaste2);
        tvPaste3 = root.findViewById(R.id.tvPaste3);
        tvPaste4 = root.findViewById(R.id.tvPaste4);
        tvPaste5 = root.findViewById(R.id.tvPaste5);
        ibCheck1 = root.findViewById(R.id.ibCheck1);
        ibCheck2 = root.findViewById(R.id.ibCheck2);
        ibCheck3 = root.findViewById(R.id.ibCheck3);
        ibCheck4 = root.findViewById(R.id.ibCheck4);
        ibCheck5 = root.findViewById(R.id.ibCheck5);
        ibPaste1 = root.findViewById(R.id.ibPaste1);
        ibPaste2 = root.findViewById(R.id.ibPaste2);
        ibPaste3 = root.findViewById(R.id.ibPaste3);
        ibPaste4 = root.findViewById(R.id.ibPaste4);
        ibPaste5 = root.findViewById(R.id.ibPaste5);

        ibPaste1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar("10");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar
                        ClipboardManager clipboardManager;
                        clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Check if there's any text in the clipboard
                        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType("text/plain")) {
                            CharSequence pasteText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                            tvPaste1.setText(pasteText);
                        }
                    }
                }, 1000); // 15 seconds in milliseconds


            }
        });

        ibPaste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar("10");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar
                        ClipboardManager clipboardManager;
                        clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Check if there's any text in the clipboard
                        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType("text/plain")) {
                            CharSequence pasteText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                            tvPaste2.setText(pasteText);
                        }
                    }
                }, 1000); // 15 seconds in milliseconds


            }
        });

        ibPaste3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar("10");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar
                        ClipboardManager clipboardManager;
                        clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Check if there's any text in the clipboard
                        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType("text/plain")) {
                            CharSequence pasteText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                            tvPaste3.setText(pasteText);
                        }
                    }
                }, 1000); // 15 seconds in milliseconds


            }
        });


        ibPaste4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar("10");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar
                        ClipboardManager clipboardManager;
                        clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Check if there's any text in the clipboard
                        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType("text/plain")) {
                            CharSequence pasteText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                            tvPaste4.setText(pasteText);
                        }
                    }
                }, 1000); // 15 seconds in milliseconds


            }
        });

        ibPaste5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar("10");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the ProgressBar
                        ClipboardManager clipboardManager;
                        clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        // Check if there's any text in the clipboard
                        if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType("text/plain")) {
                            CharSequence pasteText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                            tvPaste5.setText(pasteText);
                        }
                    }
                }, 1000); // 15 seconds in milliseconds


            }
        });






        tvCopy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText(tvField1.getText().toString());
            }
        });

        tvCopy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText(tvField2.getText().toString());
            }
        });

        tvCopy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText(tvField3.getText().toString());
            }
        });

        tvCopy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText(tvField4.getText().toString());
            }
        });

        tvCopy5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText(tvField5.getText().toString());
            }
        });



        tvGenerate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPaste1.getText().toString().equals(tvField1.getText().toString())){
                    progressbar("50");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Hide the ProgressBar
                            isCheck1 = true;

                            ibCheck1.setVisibility(View.VISIBLE);
                            tvGenerate1.setVisibility(View.GONE);
                            tvCopy1.setVisibility(View.GONE);
                            ibPaste1.setVisibility(View.GONE);
                        }
                    }, 5000); // 15 seconds in milliseconds
                }
                else if (tvPaste1.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Paste  Audio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Invaild", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvGenerate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPaste2.getText().toString().equals(tvField2.getText().toString())){
                    progressbar("50");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCheck2 = true;
                            // Hide the ProgressBar
                            ibCheck2.setVisibility(View.VISIBLE);
                            tvGenerate2.setVisibility(View.GONE);
                            tvCopy2.setVisibility(View.GONE);
                            ibPaste2.setVisibility(View.GONE);
                        }
                    }, 5000); // 15 seconds in milliseconds
                }
                else if (tvPaste2.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Paste  Audio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Invaild", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvGenerate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPaste3.getText().toString().equals(tvField3.getText().toString())){
                    progressbar("50");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCheck3 = true;
                            // Hide the ProgressBar
                            ibCheck3.setVisibility(View.VISIBLE);
                            tvGenerate3.setVisibility(View.GONE);
                            tvCopy3.setVisibility(View.GONE);
                            ibPaste3.setVisibility(View.GONE);                        }
                    }, 5000); // 15 seconds in milliseconds
                }
                else if (tvPaste3.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Paste  Audio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Invaild", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvGenerate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPaste4.getText().toString().equals(tvField4.getText().toString())){
                    progressbar("50");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCheck4 = true;
                            // Hide the ProgressBar
                            ibCheck4.setVisibility(View.VISIBLE);
                            tvGenerate4.setVisibility(View.GONE);
                            tvCopy4.setVisibility(View.GONE);
                            ibPaste4.setVisibility(View.GONE);                        }
                    }, 5000); // 15 seconds in milliseconds
                }
                else if (tvPaste4.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Paste  Audio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Invaild", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvGenerate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvPaste5.getText().toString().equals(tvField5.getText().toString())){
                    progressbar("50");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCheck5 = true;
                            // Hide the ProgressBar
                            ibCheck5.setVisibility(View.VISIBLE);
                            tvGenerate5.setVisibility(View.GONE);
                            tvCopy5.setVisibility(View.GONE);
                            ibPaste5.setVisibility(View.GONE);                        }
                    }, 5000); // 15 seconds in milliseconds
                }
                else if (tvPaste5.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Paste  Audio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Invaild", Toast.LENGTH_SHORT).show();
                }
            }
        });










        textToSpeech = new TextToSpeech(activity, this);

        btnGenerate = root.findViewById(R.id.btnGenerate);
        btnFindMissing = root.findViewById(R.id.btnFindMissing);
        btnNavChampionTask = root.findViewById(R.id.btnChampionTaskNav);
        frame = root.findViewById(R.id.frame);
        ivInfo = root.findViewById(R.id.ivInfo);

        tvInfo = root.findViewById(R.id.tvInfo);
        shareLayout = root.findViewById(R.id.shareLayout);
        level_linear = root.findViewById(R.id.level_linear);
        btnGetFreeCodes = root.findViewById(R.id.btnGetFreeCodes);
        recycler = root.findViewById(R.id.recycler);
        tvMonthlyBalance = root.findViewById(R.id.tvMonthlyBalance);
        tvDailyBalance = root.findViewById(R.id.tvDailyBalance);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        recycler.setLayoutManager(gridLayoutManager);
        levelList();





        tvCodes = root.findViewById(R.id.tvCodes);
        cbCodes = root.findViewById(R.id.cbCodes);
        cbcodesTrail = root.findViewById(R.id.cbTrail);
        cbcodesTrail.setProgress(session.getInt(Constant.REGULAR_TRIAL_COUNT));
        cbcodesTrail.setMax(10);
        btnsyncNow = root.findViewById(R.id.btnsyncNow);
        btnBuyServer = root.findViewById(R.id.btnBuyServer);
        tvTrialPeriod = root.findViewById(R.id.tvTrialPeriod);
        lltrail = root.findViewById(R.id.lTrial);
        llPayed = root.findViewById(R.id.llPayed);
        imgNotification = root.findViewById(R.id.imgNotification);
        Animation blink = new AlphaAnimation(0.0f, 1.0f);
        blink.setDuration(500);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.REVERSE);
        trynow();



        setHightlightValue();
        setCodeValue();


//        edCity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                citycount = citycount + 1;
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        edPincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                pincodecount = pincodecount + 1;
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        btnBuyServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PaymentLinkActivity.class);
                intent.putExtra(Constant.PROJECT_TYPE,"regular");
                startActivity(intent);
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a custom dialog box and set its content view
                Dialog infoDialog = new Dialog(activity);
                infoDialog.setContentView(R.layout.info_dialog_layout);

                // Show the dialog box
                infoDialog.show();
            }
        });

        if (session.getData(Constant.STATUS).equals("0")) {
            lltrail.setVisibility(View.VISIBLE);
            llPayed.setVisibility(View.GONE);

            //tvTrialPeriod.setText("Trial Period \n" + session.getInt(Constant.REGULAR_TRIAL_COUNT) + "/10");
        }


        tvInfo.startAnimation(blink);
        btnBuyServer.startAnimation(blink);
        if (!session.getBoolean(Constant.BLINK_STATUS))


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


//        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four, otp_textbox_five, otp_textbox_six, otp_textbox_seven, otp_textbox_eight, otp_textbox_nine, otp_textbox_ten};
//        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit,idcount));
//        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit, idcount));
//        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit, idcount));
//        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit, idcount));
//        otp_textbox_five.addTextChangedListener(new GenericTextWatcher(otp_textbox_five, edit, idcount));
//        otp_textbox_six.addTextChangedListener(new GenericTextWatcher(otp_textbox_six, edit, idcount));
//        otp_textbox_seven.addTextChangedListener(new GenericTextWatcher(otp_textbox_seven, edit, idcount));
//        otp_textbox_eight.addTextChangedListener(new GenericTextWatcher(otp_textbox_eight, edit, idcount));
//        otp_textbox_nine.addTextChangedListener(new GenericTextWatcher(otp_textbox_nine, edit, idcount));
//        otp_textbox_ten.addTextChangedListener(new GenericTextWatcher(otp_textbox_ten, edit, idcount));
        generateCodes = databaseHelper.getLimitCodes();

        //adCheckApi();

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NotificaionActivity.class);
                startActivity(intent);
            }
        });


        btnFindMissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.Container, new FindMissingFragment()).commit();
            }
        });

        btnNavChampionTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.Container, new AmailFragment()).commit();
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnsyncNow.isEnabled()) {
                    Toast.makeText(activity, "Please Sync Your Codes", Toast.LENGTH_SHORT).show();

                }
                else if (isCheck1 && isCheck2 && isCheck3 && isCheck4 && isCheck5) {
                        long ed_timestamp = System.currentTimeMillis();
                        long difference = ed_timestamp - st_timestamp;
                        long seconds = difference / 1000;
                        int speed_time = Integer.parseInt(session.getData(Constant.MCG_TIMER)) - (int) seconds;
                        int positiveValue = Math.max(speed_time, 0);
                        if (ApiConfig.isConnected(activity)) {

                            if (session.getData(Constant.STATUS).equals("0")) {

                                session.setData(Constant.DAILY_WALLET,(Double.parseDouble(session.getData(Constant.DAILY_WALLET))+ 0.05) + "");
                                session.setData(Constant.MONTHLY_WALLET,(Double.parseDouble(session.getData(Constant.MONTHLY_WALLET))+ 0.12) + "");
                                Fragment fragment = new GenrateQRFragment();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.Container, fragment);
                                ft.commit();
                            } else {
                                if (session.getData(Constant.CODE_GENERATE).equals("1")) {
//                                    totaltype = tvName.getText().toString().trim().length() + tvId.getText().toString().trim().length() + tvPincode.getText().toString().trim().length() + tvCity.getText().toString().trim().length();
                                    totaltext = namecount + idcount + citycount + pincodecount;
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

                else {
                    Toast.makeText(activity, "Please Generate All Audio", Toast.LENGTH_SHORT).show();
                }

                }



        });





        return root;
    }

    private void copyText(String toString) {
        progressbar("10");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the ProgressBar
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", toString);
                clipboard.setPrimaryClip(clip);

            }
        }, 1000); // 15 seconds in milliseconds

    }

    private void suspectApi() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.CODES, session.getInt(Constant.CODES) + "");
        params.put("total_text", totaltext + "");
        params.put("typed_text", totaltype + "");
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("TRIAL_COMPLETION", response);

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }, activity, Constant.SUSPECT_CODES_URL, params, false);
    }

    private void trynow() {
        int trial_level = Integer.parseInt(session.getData(Constant.LEVEL)) + 1;





    }

    private void levelList()
    {
        ArrayList<Levels> levels = new ArrayList<>();
        Levels level1 = new Levels("1","1","100","0.17","0");
        Levels level2 = new Levels("2","2","100","0.34","3");
        Levels level3 = new Levels("3","3","100","0.51","5");
        Levels level4 = new Levels("3","4","100","0.68","8");
        Levels level5 = new Levels("3","5","100","1.02","10");
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);
        levels.add(level5);

        levelsAdapter = new LevelsAdapter(activity,levels);
        recycler.setAdapter(levelsAdapter);

    }



    private void setHightlightValue() {
        String level = (Integer.parseInt(session.getData(Constant.LEVEL)) + 1) + "";
        String paisa = "";
        String text = "";
        if (level.equals("1")){
            paisa = "0.17";
            text = "Get "+paisa+" paise Per Code In Next Level.";

        }else if (level.equals("2")){
            paisa = "0.34";
            text = "Get "+paisa+" paise Per Code In Next Level.";

        }else if (level.equals("3")){
            paisa = "0.51";
            text = "Get "+paisa+" paise Per Code In Next Level.";

        }else if (level.equals("4")){
            paisa = "0.68";
            text = "Get "+paisa+" paise Per Code In Next Level.";

        }else if (level.equals("5")){
            paisa = "1.02";
            text = "Get "+paisa+" paise Per Code In Next Level.";

        }else {
            text = "";

        }
//        tvHightlight.setText(text);

    }

    private void trialCompleted() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.TYPE, Constant.REGULAR);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("TRIAL_COMPLETION", response);

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }, activity, Constant.TASK_TRIAL_URL, params, true);
    }

    private void checkJoining() {
        reference = FirebaseDatabase.getInstance().getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    joinTicket();
                } else {

                    Ticket user = dataSnapshot.getValue(Ticket.class);

                    sendChat(user.getId(), user.getName(), user.getCategory(), user.getType(), user.getDescription());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void joinTicket() {
        Long tsLong = System.currentTimeMillis() / 1000;
        RandomId = session.getData(Constant.USER_ID) + "_" + tsLong.toString();
        reference = FirebaseDatabase.getInstance().getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constant.ID, RandomId);
        hashMap.put(Constant.CATEGORY, "Joining");
        hashMap.put(Constant.DESCRIPTION, "Enquiry For Joining");
        hashMap.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        hashMap.put(Constant.NAME, session.getData(Constant.NAME));
        hashMap.put(Constant.MOBILE, session.getData(Constant.MOBILE));
        hashMap.put(Constant.TYPE, Constant.JOINING_TICKET);
        hashMap.put(Constant.SUPPORT, "Admin");
        hashMap.put(Constant.TIMESTAMP, tsLong.toString());
        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
            sendChat(RandomId, session.getData(Constant.NAME), "Joining", Constant.JOINING_TICKET, "Enquiry For Joining");

        });
    }

    private void sendChat(String id, String name, String category, String type, String description) {

        //Log.d("CHAT_DETAILS","USER_ID - "+id + "\nName - "+name+"\nCategory - "+category+"\nType - "+type +"Description - "+description);
        final Intent intent = new Intent(activity, MessageActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        intent.putExtra(TICKET_ID, id);
        intent.putExtra(NAME, name);
        intent.putExtra(TYPE, type);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(CATEGORY, category);
        startActivity(intent);
    }


    public void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(root.findViewById(R.id.edPincode), InputMethodManager.SHOW_IMPLICIT);
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
                                if (session.getBoolean(L_TRIAL_STATUS)){
                                    session.setBoolean(L_TRIAL_STATUS,false);
                                    session.setBoolean(L_TRIAL_AVAILABLE,false);

                                }


                                session.setInt(Constant.CODES, 0);
                                session.setInt(Constant.TODAY_CODES, Integer.parseInt(jsonObject.getString(Constant.TODAY_CODES)));
                                session.setInt(Constant.TOTAL_CODES, Integer.parseInt(jsonObject.getString(Constant.TOTAL_CODES)));
                                session.setData(Constant.BALANCE, jsonObject.getString(Constant.BALANCE));
                                session.setData(Constant.LEVEL, jsonObject.getString(Constant.LEVEL));
                                session.setData(Constant.PER_CODE_VAL, jsonObject.getString(PER_CODE_VAL));
                                session.setData(Constant.REFUND_WALLET, jsonObject.getString(Constant.REFUND_WALLET));
                                session.setData(Constant.BLACK_BOX, jsonObject.getString(Constant.BLACK_BOX));
                                session.setData(Constant.DAILY_WALLET, jsonObject.getString(Constant.DAILY_WALLET));
                                session.setData(Constant.MONTHLY_WALLET, jsonObject.getString(Constant.MONTHLY_WALLET));



                                session.setData(Constant.STATUS, jsonObject.getString(Constant.STATUS));



                                setCodeValue();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                }, 2000);
                                levelList();

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
//            tvHightlight.setVisibility(View.VISIBLE);
        }

        else {
//            tvHightlight.setVisibility(View.GONE);
        }

        cbCodes.setMax(session.getInt(Constant.SYNC_CODES));
        cbCodes.setProgress(session.getInt(Constant.CODES));
        try {
            tvTodayCodes.setText(session.getInt(Constant.TODAY_CODES) + " + " + session.getInt(Constant.CODES));
            tvTotalCodes.setText(session.getInt(Constant.TOTAL_CODES) + " + " + session.getInt(Constant.CODES));
            tvDailyBalance.setText("₹ "+session.getData(Constant.DAILY_WALLET));
            tvMonthlyBalance.setText("₹ "+session.getData(Constant.MONTHLY_WALLET));
            double current_bal = (double) (session.getInt(Constant.CODES) * 0.17);
            tvBalance.setText(session.getData(Constant.BALANCE) + " + " + String.format("%.2f", current_bal) + "");
        } catch (Exception e) {
        }
        tvHistorydays.setText(session.getData(Constant.WORKED_DAYS));
        tvLevels.setText(session.getData(Constant.LEVEL));

    }

    @SuppressLint("ShowToast")
    private void addRewardCode() {
        int codereward = Integer.parseInt(session.getData(Constant.REWARD));
        session.setInt(Constant.CODES, session.getInt(Constant.CODES) + codereward);
        Snackbar snackbar = Snackbar.make(root, "Code Rewarded Successfully", 5000);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.green_500));
        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        root.setLayoutParams(params);
        snackbar.show();
        setCodeValue();
        dialog.cancel();
    }


    private void GotoActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                st_timestamp = System.currentTimeMillis();

                if (!activity.isFinishing()) {
                    // The activity is still running, so it's safe to interact with its views
                    frame.setVisibility(View.VISIBLE);
                    dialog.cancel();

                    if (session.getData(AD_STATUS).equals("1") && session.getData(AD_TYPE).equals("1")) {
                        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault());
                        Date c = Calendar.getInstance().getTime();
                        String currentDate = df.format(c);
                        if (!session.getBoolean(Constant.LAST_UPDATED_DATE_STATUS_AD)) {
                            session.setData(Constant.LAST_UPDATED_DATE_AD, currentDate);
                            session.setBoolean(Constant.LAST_UPDATED_DATE_STATUS_AD, true);

                        }
                        Date date1 = null;
                        try {
                            date1 = df.parse(currentDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Date date2 = null;
                        try {
                            date2 = df.parse(session.getData(Constant.LAST_UPDATED_DATE_AD));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long different = date1.getTime() - date2.getTime();
                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long elapsedHours = different / hoursInMilli;
                        long elapsedMinutue = different / minutesInMilli;

                        if (elapsedMinutue >= Long.parseLong(session.getData(Constant.AD_SHOW_TIME))) {
                            session.setBoolean(Constant.LAST_UPDATED_DATE_STATUS_AD, false);
                            // showAdDialog();


                        }
                    }
                }
            }
        }, code_generate_time);
    }

    private void showAdDialog() {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.ad_code_layout);
        dialog.setCancelable(false);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        Button btnReward = dialog.findViewById(R.id.btnReward);
        btnReward.setText("Click to Get " + session.getData(Constant.REWARD) + " Codes Free");
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (session.getData(AD_TYPE).equals("1")) {
                    //  showRewardedVideoAd();

                } else {
                    Intent intent = new Intent(activity, LoadWebView2Activity.class);
                    startActivity(intent);
                }

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();



//        tvField1.setText(generateCodes.get(0).getStudent_name());
//        tvField2.setText(generateCodes.get(0).getStudent_name());
//        tvField3.setText(generateCodes.get(0).getStudent_name());
//        tvField4.setText(generateCodes.get(0).getStudent_name());
//        tvField5.setText(generateCodes.get(0).getStudent_name());


        tvField1.setText("READ BOOK");
        tvField4.setText("புத்தகம் படிக்க");
        tvField5.setText("పుస్తకం చదువు");
        tvField2.setText("किताब पढ़ो");
        tvField3.setText("ಪುಸ್ತಕ ಓದು");



//        tvPincode.setText(generateCodes.get(0).getPin_code());
//        tvCity.setText(generateCodes.get(0).getEcity());
//        tvId.setText(generateCodes.get(0).getId_number());



        ibSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak ="Read Book";

                speakOut(toSpeak);


            }

        });

        ibSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak = "किताब पढ़ो";

                speakOut(toSpeak);


            }

        });


        ibSpeak3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak = "ಪುಸ್ತಕ ಓದು";

                speakOut(toSpeak);
            }
        });


        ibSpeak4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak = "புத்தகம் படிக்க";

                speakOut(toSpeak);
            }
        });


        ibSpeak5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak = "పుస్తకం చదువు";

                speakOut(toSpeak);
            }
        });




    }

    private void progressbar( String s) {
        RelativeLayout progress_layout;
        ProgressBar progressBar;
        TextView progressText;
        final int[] i = {0};
        String time = s; // Make sure 's' contains the desired time in milliseconds

        progress_layout = root.findViewById(R.id.progress_layout);
        progress_layout.setVisibility(View.VISIBLE);

        progressBar = root.findViewById(R.id.progress_bar);
        progressText = root.findViewById(R.id.progress_text);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set the limitations for the numeric
                // text under the progress bar
                if (i[0] <= 100) {
                    progressBar.setProgress(i[0]);
                    i[0]++;
                    handler.postDelayed(this, Integer.parseInt(time));

                    // Change the delay to 'time'
                } else {
                    handler.removeCallbacks(this);
                    progress_layout.setVisibility(View.GONE);
                }
            }
        }, Integer.parseInt(time)); // Change the initial delay to 'time'


    }




    private void speakOut(String toString) {

       // textToSpeech.speak(toString, TextToSpeech.QUEUE_FLUSH, null, null);

            String text = toString;

            // Split the text into words
            String[] words = text.split(" ");

            for (String word : words) {
                textToSpeech.speak(word, TextToSpeech.QUEUE_ADD, null, null);
                // Add a short delay between words (adjust as needed)
                try {
                    Thread.sleep(500); // 500 milliseconds (half a second)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }


    private void addToMainBalance(String wallet_type)
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.WALLET_TYPE, wallet_type);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        JSONArray userArray = jsonObject.getJSONArray(Constant.DATA);
                        session.setData(Constant.DAILY_WALLET, userArray.getJSONObject(0).getString(Constant.DAILY_WALLET));
                        session.setData(Constant.MONTHLY_WALLET, userArray.getJSONObject(0).getString(Constant.MONTHLY_WALLET));
                        session.setData(Constant.BALANCE, userArray.getJSONObject(0).getString(Constant.BALANCE));
                        setCodeValue();


                    } else {
                        Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }, activity, Constant.ADD_MAIN_BALANCE_URL, params, true);

    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the desired language (e.g., Locale.US)
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle the case where the language data is missing or not supported
            }
        } else {
            // Handle initialization failure
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Don't forget to release the TextToSpeech engine when done
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
        textToSpeech.shutdown();
    }
}