package com.app.abcdapp.fragment;

import static com.app.abcdapp.helper.Constant.SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.RepayListAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.activities.ApplyLeaveActivity;
import com.app.abcdapp.activities.MyRefundActivity;
import com.app.abcdapp.activities.NotificaionActivity;
import com.app.abcdapp.activities.RateusActivity;
import com.app.abcdapp.activities.ReferEarnActivity;
import com.app.abcdapp.activities.ReferDetailsActivity;
import com.app.abcdapp.activities.RiseTicketActivity;
import com.app.abcdapp.activities.TrialEarningsActivity;
import com.app.abcdapp.activities.UpdateProfileActivity;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.RepayListModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    CardView cardView1;
    ImageView imgMenu;
    Session session;
    Activity activity;
    Button btncopy,btnApplyLeave;
    private AlertDialog alertDialog;
    private ReviewInfo reviewInfo;
    private ReviewManager manager;
    private SwitchMaterial switchDarkMode;
    RelativeLayout  rl1 ;
    LinearLayout llReferCode;
    private AdView mAdView;





    Button btnReferDetails;

    TextView tvName,tvMobile,tvEarn,tvWithdrawal,tvCodes,tvBalance,tvRefercode,tvTotalRefer,tvReferDescription,btnTrialEarnings,tvEarnings,tvReferrals;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  =  inflater.inflate(R.layout.fragment_profile, container, false);


        activity = getActivity();
        session = new Session(getActivity());




        mAdView = root.findViewById(R.id.adView);
        tvEarnings = root.findViewById(R.id.tvEarnings);
        tvReferrals = root.findViewById(R.id.tvReferrals);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        ImageView profileImageView = root.findViewById(R.id.profileImageView);
        Drawable drawable = getResources().getDrawable(R.drawable.proficenew);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

// Define the desired width and height for the resized image
        int desiredWidth = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp);
        int desiredHeight = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp);

// Resize the bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);

// Set the resized bitmap to the ImageView
        profileImageView.setImageBitmap(resizedBitmap);





        switchDarkMode = root.findViewById(R.id.switch_dark_mode);









        cardView1 = root.findViewById(R.id.cardView1);
        rl1 = root.findViewById(R.id.rl1);
        llReferCode = root.findViewById(R.id.llReferCode);
        imgMenu = root.findViewById(R.id.imgMenu);
        tvName = root.findViewById(R.id.tvName);
        tvMobile = root.findViewById(R.id.tvMobile);
        tvEarn = root.findViewById(R.id.tvEarn);
        tvWithdrawal = root.findViewById(R.id.tvWithdrawal);
        tvCodes = root.findViewById(R.id.tvCodes);
        tvBalance = root.findViewById(R.id.tvBalance);
        btncopy = root.findViewById(R.id.btncopy);
        tvRefercode = root.findViewById(R.id.tvRefercode);
        tvTotalRefer = root.findViewById(R.id.tvTotalRefer);
        tvReferDescription = root.findViewById(R.id.tvReferDescription);
        btnReferDetails = root.findViewById(R.id.btnReferDetails);
        btnApplyLeave = root.findViewById(R.id.btnApplyLeave);
        btnTrialEarnings = root.findViewById(R.id.btnTrialEarnings);
        tvName.setText(session.getData(Constant.NAME));
        tvMobile.setText(session.getData(Constant.MOBILE));
        tvEarn.setText(session.getData(Constant.EARN)) ;
        tvWithdrawal.setText(session.getData(Constant.WITHDRAWAL));
        tvCodes.setText(session.getInt(Constant.TOTAL_CODES) + "");
        tvBalance.setText(session.getData(Constant.BALANCE));
        tvTotalRefer.setText(session.getData(Constant.TOTAL_REFERRALS));
        tvReferDescription.setText(session.getData(Constant.REFER_DESCRIPTION));
        tvEarnings.setText(session.getData(Constant.EARN));
        tvReferrals.setText(session.getData(Constant.TOTAL_REFERRALS));
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReferEarnActivity.class);
                startActivity(intent);
            }
        });
        btnReferDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReferDetailsActivity.class);
                startActivity(intent);

            }
        });
        btnTrialEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrialEarningsActivity.class);
                startActivity(intent);

            }
        });
        checkTrialEarnings();

        btnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.getData(Constant.STATUS).equals("1")){
                    Intent intent = new Intent(activity, ApplyLeaveActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(activity, "Only for Verfied Users", Toast.LENGTH_SHORT).show();
                }

            }
        });


        tvRefercode.setText(" Your Refer Code : "+session.getData(Constant.REFER_CODE));
        String text = tvRefercode.getText().toString();
        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nDOWNLOAD THE APP AND GET UNLIMITED EARNING .you can also Download App from below link and enter referral code while login-"+"\n"+text+"\n";
                    shareMessage = shareMessage +"\n https://play.google.com/store/apps/details?id=com.app.abcdapp \n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });



        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(view);
            }
        });




        return root;
    }


    private void checkTrialEarnings()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        if(jsonArray.getJSONObject(0).getString(Constant.TRIAL_EARNINGS).equals("1")){
                           // btnTrialEarnings.setVisibility(View.VISIBLE);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.TRIAL_EARNINGS_ELIGIBLE_URL, params, true);

    }

    private void showpopup(View v) {

        PopupMenu popup = new PopupMenu(getActivity(),v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override

    public boolean onMenuItemClick(MenuItem item) {
        if (getActivity() == null) {
            // Fragment not attached to activity
            return false;
        }
        switch (item.getItemId()) {
            case R.id.logoutitem:
                session.logoutUser(activity);
                return true;
            case R.id.notification:
                Intent intent = new Intent(getActivity(), NotificaionActivity.class);
                startActivity(intent);
                return true;

            case R.id.rateus:
                Intent intent1 = new Intent(getActivity(), RateusActivity.class);
                startActivity(intent1);
                return true;

            case R.id.ReferEarn:
                Intent intents = new Intent(getActivity(), ReferEarnActivity.class);
                startActivity(intents);
                return true;
            case R.id.Uptdatepofile:
                Intent intentd = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intentd);
                return true;
            case R.id.jobDetails:
                String url = session.getData(Constant.JOB_DETAILS_LINK);
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Uri uri = Uri.parse(url);
                Intent intentf = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentf);
                return true;
            default:
                return false;
        }
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.leave_popup, null);

        EditText editTextDate = view.findViewById(R.id.editTextDate);
        editTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showDatePickerDialog(editTextDate);
                }
                return false;
            }
        });

        TextInputEditText editTextName = view.findViewById(R.id.editTextName);

        Button buttonApply = view.findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String date = editTextDate.getText().toString();
                // Do something with name and date
                alertDialog.dismiss();
            }
        });

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editText.setText(dateString);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

}