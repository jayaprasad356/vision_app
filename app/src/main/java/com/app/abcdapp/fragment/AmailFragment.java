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
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.LevelsAdapter;
import com.app.abcdapp.PaymentLinkActivity;
import com.app.abcdapp.R;
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
import com.app.abcdapp.model.GenerateCodes;
import com.app.abcdapp.model.GenerateEmails;
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


public class AmailFragment extends Fragment {

    TextView tvName, tvInstitute, tvCity, tvId, tvTodayCodes, tvTotalCodes, tvHistorydays, tvTrialPeriod,tvEarningBalance,tvBonusBalance;
    EditText edName, edPincode, edCity;
    Button btnGenerate, btnsyncNow, btnBuyServer, btnGetFreeCodes;
    ImageButton sharebtn;
    ImageView ivInfo;


    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four, otp_textbox_five, otp_textbox_six, otp_textbox_seven, otp_textbox_eight, otp_textbox_nine, otp_textbox_ten;
    DatabaseHelper databaseHelper;
    ArrayList<GenerateEmails> generateEmails = new ArrayList<GenerateEmails>();
    Session session;
    Activity activity;
    ScrollView frame;

    String Idnumber = "";

    Handler handler;
    long code_generate_time = 0;
    public static Dialog dialog = null;
    Button btnFindMissing, btnNavChampionTask;
    TextView tvBalance;
    View root;
    TextView tvCodes;
    CircularProgressIndicator cbCodes, cbcodesTrail;
    TextView tvHightlight, tvInfo, tvShareEarnings,tvTargerrefers;
    ProgressDialog progressDialog;
    long st_timestamp;
    ClipboardManager clipBoard;
    LinearLayout lltrail, llPayed;
    String RandomId;
    DatabaseReference reference;
    ImageView imgNotification;
    LinearLayout shareLayout,level_linear;
    TextView tvChampionTask;
    Button addToMBEarningWallet,addToMBBonusWallet;
    int totaltype = 0,totaltext = 0;
    int namecount = 0,idcount = 0,citycount = 0 , pincodecount = 0;




    public AmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_amail, container, false);



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









        clipBoard = (ClipboardManager) getActivity().getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
        databaseHelper = new DatabaseHelper(getActivity());

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
        tvName = root.findViewById(R.id.tvName);
        tvInstitute = root.findViewById(R.id.tvInstitute);
        tvCity = root.findViewById(R.id.tvCity);
        tvId = root.findViewById(R.id.tvId);
        tvTargerrefers = root.findViewById(R.id.tvTargerrefers);
        edName = root.findViewById(R.id.edName);
        edPincode = root.findViewById(R.id.edPincode);
        edCity = root.findViewById(R.id.edCity);
        tvTodayCodes = root.findViewById(R.id.tvTodayCodes);
        tvTotalCodes = root.findViewById(R.id.tvTotalCodes);
        tvHistorydays = root.findViewById(R.id.tvHistorydays);
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
        tvChampionTask = root.findViewById(R.id.tvChampionTask);
        addToMBEarningWallet = root.findViewById(R.id.addToMBEarningWallet);
        addToMBBonusWallet = root.findViewById(R.id.addToMBBonusWallet);
        tvChampionTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.getInt(Constant.MAILS) == 0){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.Container, new AmailFragment()).commit();

                }else {
                    Toast.makeText(activity, "Sync Codes then Move to Next Task", Toast.LENGTH_SHORT).show();
                }



            }
        });
        addToMBEarningWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMainBalance("earnings_wallet");



            }
        });
        addToMBBonusWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMainBalance("bonus_wallet");




            }
        });
        levelList();



        otp_textbox_one = root.findViewById(R.id.otp_edit_box1);
        otp_textbox_two = root.findViewById(R.id.otp_edit_box2);
        otp_textbox_three = root.findViewById(R.id.otp_edit_box3);
        otp_textbox_four = root.findViewById(R.id.otp_edit_box4);
        otp_textbox_five = root.findViewById(R.id.otp_edit_box5);
        otp_textbox_six = root.findViewById(R.id.otp_edit_box6);
        otp_textbox_seven = root.findViewById(R.id.otp_edit_box7);
        otp_textbox_eight = root.findViewById(R.id.otp_edit_box8);
        otp_textbox_nine = root.findViewById(R.id.otp_edit_box9);
        otp_textbox_ten = root.findViewById(R.id.otp_edit_box10);
        tvCodes = root.findViewById(R.id.tvCodes);
        cbCodes = root.findViewById(R.id.cbCodes);
        cbcodesTrail = root.findViewById(R.id.cbTrail);
        cbcodesTrail.setProgress(session.getInt(Constant.REGULAR_TRIAL_COUNT));
        cbcodesTrail.setMax(10);
        btnsyncNow = root.findViewById(R.id.btnsyncNow);
        btnBuyServer = root.findViewById(R.id.btnBuyServer);
        tvTrialPeriod = root.findViewById(R.id.tvTrialPeriod);
        tvEarningBalance = root.findViewById(R.id.tvEarningBalance);
        tvBonusBalance = root.findViewById(R.id.tvBonusBalance);
        lltrail = root.findViewById(R.id.lTrial);
        llPayed = root.findViewById(R.id.llPayed);
        imgNotification = root.findViewById(R.id.imgNotification);
        Animation blink = new AlphaAnimation(0.0f, 1.0f);
        blink.setDuration(500);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.REVERSE);
        tvHightlight.startAnimation(blink);




        setHightlightValue();
        setCodeValue();

        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                namecount = namecount + 1;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                citycount = citycount + 1;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pincodecount = pincodecount + 1;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnBuyServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PaymentLinkActivity.class);
                intent.putExtra(Constant.PROJECT_TYPE,"amail");
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
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // code to show next day
                session.setBoolean(Constant.BLINK_STATUS, true);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);

                // Get the current time and calculate the milliseconds until midnight
                Calendar midnight = Calendar.getInstance();
                midnight.set(Calendar.HOUR_OF_DAY, 24);
                midnight.set(Calendar.MINUTE, 0);
                midnight.set(Calendar.SECOND, 0);
                midnight.set(Calendar.MILLISECOND, 0);
                long delay = midnight.getTimeInMillis() - System.currentTimeMillis();

                // Use a Handler to show the button again at midnight
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        session.setBoolean(Constant.BLINK_STATUS, false);
                    }
                }, delay);

                // Change the text of the button to "Hide Button" on click
                Intent intent = new Intent(activity, ReferEarnActivity.class);
                startActivity(intent);
            }
        });

        if (session.getData(Constant.STATUS).equals("0") || (session.getData(Constant.STATUS).equals("1") && !session.getData(Constant.PROJECT_TYPE).equals("amail") )) {
            lltrail.setVisibility(View.VISIBLE);
            llPayed.setVisibility(View.GONE);
            tvHightlight.setVisibility(View.GONE);
            tvChampionTask.setVisibility(View.GONE);
            tvTrialPeriod.setText("");
        }


        tvInfo.startAnimation(blink);
        btnBuyServer.startAnimation(blink);
        if (!session.getBoolean(Constant.BLINK_STATUS))
            tvShareEarnings.startAnimation(blink);

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


        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four, otp_textbox_five, otp_textbox_six, otp_textbox_seven, otp_textbox_eight, otp_textbox_nine, otp_textbox_ten};
        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit,idcount));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit, idcount));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit, idcount));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit, idcount));
        otp_textbox_five.addTextChangedListener(new GenericTextWatcher(otp_textbox_five, edit, idcount));
        otp_textbox_six.addTextChangedListener(new GenericTextWatcher(otp_textbox_six, edit, idcount));
        otp_textbox_seven.addTextChangedListener(new GenericTextWatcher(otp_textbox_seven, edit, idcount));
        otp_textbox_eight.addTextChangedListener(new GenericTextWatcher(otp_textbox_eight, edit, idcount));
        otp_textbox_nine.addTextChangedListener(new GenericTextWatcher(otp_textbox_nine, edit, idcount));
        otp_textbox_ten.addTextChangedListener(new GenericTextWatcher(otp_textbox_ten, edit, idcount));
        generateEmails = databaseHelper.getLimitEmails();


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
                fm.beginTransaction().replace(R.id.Container, new HomeFragment()).commit();
            }
        });

        btnNavChampionTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.Container, new HomeFragment()).commit();
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnsyncNow.isEnabled()) {
                    Toast.makeText(activity, "Please Sync Your Codes", Toast.LENGTH_SHORT).show();

                } else {
                    Idnumber = otp_textbox_one.getText().toString().trim() + otp_textbox_two.getText().toString().trim() +
                            otp_textbox_three.getText().toString().trim() + otp_textbox_four.getText().toString().trim() + otp_textbox_five.getText().toString().trim() +
                            otp_textbox_six.getText().toString().trim() + otp_textbox_seven.getText().toString().trim() + otp_textbox_eight.getText().toString().trim() +
                            otp_textbox_nine.getText().toString().trim() + otp_textbox_ten.getText().toString().trim();
                    if (!tvName.getText().toString().trim().equals(edName.getText().toString().trim())) {

                        edName.setError("Name not match");
                        edName.requestFocus();
                        return;

                    } else if (!tvId.getText().toString().trim().equals(Idnumber.toString().trim())) {


                        // Toast.makeText(getActivity(), "Id number not match", Toast.LENGTH_SHORT).show();
                        otp_textbox_ten.setError("Id number not match");
                        otp_textbox_ten.requestFocus();
                        return;
                    } else if (!tvCity.getText().toString().trim().equals(edCity.getText().toString().trim())) {

                        // Toast.makeText(getActivity(), "City not match", Toast.LENGTH_SHORT).show();
                        edCity.setError("location not match");
                        edCity.requestFocus();
                        return;
                    } else if (!tvInstitute.getText().toString().equals(edPincode.getText().toString())) {

                        // Toast.makeText(getActivity(), "Pin code not match", Toast.LENGTH_SHORT).show();
                        edPincode.setError("Institution not match");
                        edPincode.requestFocus();
                        return;
                    } else {
                        long ed_timestamp = System.currentTimeMillis();

                        long difference = ed_timestamp - st_timestamp;
                        long seconds = difference / 1000;
                        int speed_time = Integer.parseInt(session.getData(Constant.MCG_TIMER)) - (int) seconds;
                        int positiveValue = Math.max(speed_time, 0);
                        if (ApiConfig.isConnected(activity)) {
                            String formattedEmail = tvId.getText().toString().trim()+"@"+tvInstitute.getText().toString().trim().toLowerCase().replace(" ", "")+".com";
                            session.setData(Constant.GENERATED_AMAIL,formattedEmail);
                            if (session.getData(Constant.STATUS).equals("0") || (session.getData(Constant.STATUS).equals("1") && !session.getData(Constant.PROJECT_TYPE).equals("amail") )) {
                                session.setData(Constant.EARNINGS_WALLET,(Double.parseDouble(session.getData(Constant.EARNINGS_WALLET))+ 0.75) + "");
                                session.setData(Constant.BONUS_WALLET,(Double.parseDouble(session.getData(Constant.BONUS_WALLET))+ 2.25) + "");
                                Fragment fragment = new CreateAmailFragment();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.Container, fragment);
                                ft.commit();
                            } else {
                                if (session.getData(Constant.CODE_GENERATE).equals("1") && session.getInt(Constant.TODAY_MAILS) < 10) {
                                    totaltype = tvName.getText().toString().trim().length() + tvId.getText().toString().trim().length() + tvInstitute.getText().toString().trim().length() + tvCity.getText().toString().trim().length();
                                    totaltext = namecount + idcount + citycount + pincodecount;
                                    session.setInt(Constant.MAILS, session.getInt(Constant.MAILS) + Integer.parseInt(session.getData(PER_CODE_VAL)));

                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Constant.MCG_TIMER, positiveValue);
                                    bundle.putString(Constant.TASK_TYPE, Constant.REGULAR);
                                    Fragment fragment = new CreateAmailFragment();
                                    fragment.setArguments(bundle);
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.Container, fragment);
                                    ft.commit();

                                } else {
                                    Toast.makeText(activity, "You are Restricted for Creating  Email", Toast.LENGTH_SHORT).show();

                                }

                            }


                        }


                    }
                }


            }
        });







        return root;
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
                        session.setData(Constant.EARNINGS_WALLET, userArray.getJSONObject(0).getString(Constant.EARNINGS_WALLET));
                        session.setData(Constant.BONUS_WALLET, userArray.getJSONObject(0).getString(Constant.BONUS_WALLET));
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



    }

    private void setLightMode() {


        RelativeLayout rlHome = root.findViewById(R.id.rlHome);
        LinearLayout l1 = root.findViewById(R.id.l1);
        LinearLayout llBalance = root.findViewById(R.id.llBalance);



        CardView cardView = root.findViewById(R.id.card1);
        // cardBackgroundColor change to black
        cardView.setCardBackgroundColor(Color.parseColor("#4d4a75"));
        edName.setBackgroundColor(Color.parseColor("#FFFFFF"));
        edPincode.setBackgroundColor(Color.parseColor("#FFFFFF"));
        edCity.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_one.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_two.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_three.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_four.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_five.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_six.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_seven.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_eight.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_nine.setBackgroundColor(Color.parseColor("#FFFFFF"));
        otp_textbox_ten.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
        edName.setBackgroundColor(Color.parseColor("#D4D4D4"));
        edPincode.setBackgroundColor(Color.parseColor("#D4D4D4"));
        edCity.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_one.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_two.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_three.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_four.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_five.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_six.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_seven.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_eight.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_nine.setBackgroundColor(Color.parseColor("#D4D4D4"));
        otp_textbox_ten.setBackgroundColor(Color.parseColor("#D4D4D4"));
        rlHome.setBackgroundColor(Color.parseColor("#2D2B2B"));
        l1.setBackgroundColor(Color.parseColor("#2D2B2B"));
        btnGenerate.setBackgroundColor(Color.parseColor("#565657"));
        cbCodes.setTrackColor(Color.parseColor("#D4D4D4"));
        llBalance.setBackgroundColor(Color.parseColor("#A27801"));

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
        tvHightlight.setText(text);

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
            progressDialog.setTitle("Emails are Syncing");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (session.getInt(Constant.MAILS) != 0) {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
                params.put(Constant.MAILS, session.getInt(Constant.MAILS) + "");
                ApiConfig.RequestToVolley((result, response) -> {
                    if (result) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean(Constant.SUCCESS)) {
                                if (session.getBoolean(L_TRIAL_STATUS)){
                                    session.setBoolean(L_TRIAL_STATUS,false);
                                    session.setBoolean(L_TRIAL_AVAILABLE,false);

                                }


                                session.setInt(Constant.MAILS, 0);
                                session.setInt(Constant.TODAY_CODES, Integer.parseInt(jsonObject.getString(Constant.TODAY_CODES)));
                                session.setInt(Constant.TOTAL_CODES, Integer.parseInt(jsonObject.getString(Constant.TOTAL_CODES)));
                                session.setInt(Constant.TODAY_MAILS, Integer.parseInt(jsonObject.getString(Constant.TODAY_MAILS)));
                                session.setInt(Constant.TOTAL_MAILS, Integer.parseInt(jsonObject.getString(Constant.TOTAL_MAILS)));
                                session.setData(Constant.BALANCE, jsonObject.getString(Constant.BALANCE));
                                session.setData(Constant.LEVEL, jsonObject.getString(Constant.LEVEL));
                                session.setData(Constant.PER_CODE_VAL, jsonObject.getString(PER_CODE_VAL));
                                session.setData(Constant.REFUND_WALLET, jsonObject.getString(Constant.REFUND_WALLET));
                                session.setData(Constant.BLACK_BOX, jsonObject.getString(Constant.BLACK_BOX));
                                session.setData(Constant.STATUS, jsonObject.getString(Constant.STATUS));
                                session.setData(Constant.EARNINGS_WALLET, jsonObject.getString(Constant.EARNINGS_WALLET));
                                session.setData(Constant.BONUS_WALLET, jsonObject.getString(Constant.BONUS_WALLET));
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
        if (session.getInt(Constant.MAILS) >= 10) {
            btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg));
            btnsyncNow.setEnabled(true);

        } else {
            btnsyncNow.setBackground(ContextCompat.getDrawable(activity, R.drawable.syncbg_disabled));
            btnsyncNow.setEnabled(false);

        }
        tvCodes.setText(session.getInt(Constant.MAILS) + "");

        cbCodes.setMax(10);
        cbCodes.setProgress(session.getInt(Constant.MAILS));
        try {
            tvTodayCodes.setText(session.getInt(Constant.TODAY_MAILS) + " + " + session.getInt(Constant.MAILS));
            tvTotalCodes.setText(session.getInt(Constant.TOTAL_MAILS) + " + " + session.getInt(Constant.MAILS));
            tvEarningBalance.setText("₹ "+session.getData(Constant.EARNINGS_WALLET));
            tvBonusBalance.setText("₹ "+session.getData(Constant.BONUS_WALLET));
            //tvTargerrefers.setText(session.getData(Constant.TARGET_REFERS) + " Refers");
            //double current_bal = (double) (session.getInt(Constant.MAILS) * 3);
            tvBalance.setText(session.getData(Constant.BALANCE));
        } catch (Exception e) {
            Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        tvHistorydays.setText(session.getData(Constant.WORKED_DAYS));

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



        tvName.setText(generateEmails.get(0).getStudent_name());
        tvInstitute.setText(generateEmails.get(0).getInstitution());
        tvCity.setText(generateEmails.get(0).getEcity());
        tvId.setText(generateEmails.get(0).getId_number());


//
//        edName.setText(tvName.getText().toString());
//        edPincode.setText(tvInstitute.getText().toString());
//        edCity.setText(tvCity.getText().toString());
//        otp_textbox_one.setText(tvId.getText().toString().substring(0, 1));
//        otp_textbox_two.setText(tvId.getText().toString().substring(1, 2));
//        otp_textbox_three.setText(tvId.getText().toString().substring(2, 3));
//        otp_textbox_four.setText(tvId.getText().toString().substring(3, 4));
//        otp_textbox_five.setText(tvId.getText().toString().substring(4, 5));
//        otp_textbox_six.setText(tvId.getText().toString().substring(5, 6));
//        otp_textbox_seven.setText(tvId.getText().toString().substring(6, 7));
//        otp_textbox_eight.setText(tvId.getText().toString().substring(7, 8));
//        otp_textbox_nine.setText(tvId.getText().toString().substring(8, 9));
//        otp_textbox_ten.setText(tvId.getText().toString().substring(9, 10));





    }


}