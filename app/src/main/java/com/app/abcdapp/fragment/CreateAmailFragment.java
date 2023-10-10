package com.app.abcdapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.abcdapp.R;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;

public class CreateAmailFragment extends Fragment {
    Handler handler;
    Session session;
    Activity activity;
    String tasktype = "regular";
    int mcgtimer = 0;
    long mcgtimer_m = 0;
    TextView tvEmail;


    public CreateAmailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_create_amail, container, false);
        activity = getActivity();
        session = new Session(activity);

        tvEmail = root.findViewById(R.id.tvEmail);
        tvEmail.setText(session.getData(Constant.GENERATED_AMAIL));
        if(getArguments() != null){
            tasktype = getArguments().getString(Constant.TASK_TYPE);
            mcgtimer = getArguments().getInt(Constant.MCG_TIMER);

        }
        mcgtimer_m = mcgtimer * 1000L;


        //setReferValue();



        handler = new Handler();
        GotoActivity();
        return root;


    }

    private void GotoActivity()
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.fm.beginTransaction().replace(R.id.Container, new AmailFragment()).commitAllowingStateLoss();




            }
        },1000);
    }
}