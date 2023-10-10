package com.app.abcdapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.activities.GenrateQRActivity;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GenrateQRFragment extends Fragment {
    Handler handler;
    Session session;
    Activity activity;
    TextView tvReferEarn;
    String tasktype = "regular";
    int mcgtimer = 0;
    long mcgtimer_m = 0;


    public GenrateQRFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_genrate_q_r, container, false);
        activity = getActivity();
        session = new Session(activity);
        tvReferEarn = root.findViewById(R.id.tvReferEarn);
        if(getArguments() != null){
            tasktype = getArguments().getString(Constant.TASK_TYPE);
            mcgtimer = getArguments().getInt(Constant.MCG_TIMER);

        }
        mcgtimer_m = mcgtimer * 1000L;


        setReferValue();



        handler = new Handler();
        GotoActivity();
        return root;


    }

    private void setReferValue()
    {
        double codebonus =  Double.parseDouble(session.getData(Constant.REFER_BONUS_CODES))* 0.17;
        double totalbonus = codebonus + Double.parseDouble(session.getData(Constant.REFER_BONUS_AMOUNT));
        if (session.getData(Constant.PROJECT_TYPE).equals("champion")){
            tvReferEarn.setText("Refer & Earn Rs 600 + Rs 1000");
        }else {

            tvReferEarn.setText("Refer And Earn Rs. "+totalbonus+" Instantly");
        }


    }


    private void GotoActivity()
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tasktype.equals("champion")){
                    MainActivity.fm.beginTransaction().replace(R.id.Container, new FindMissingFragment()).commitAllowingStateLoss();

                }else {

                    MainActivity.fm.beginTransaction().replace(R.id.Container, new HomeFragment()).commitAllowingStateLoss();
                }







            }
        },300);
    }
}