package com.app.abcdapp.fragment;

import static com.app.abcdapp.helper.Constant.SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.ImformationVideoAdapter;
import com.app.abcdapp.Adapter.RewardUrlAdapter;
import com.app.abcdapp.Adapter.TransactionAdapter;
import com.app.abcdapp.Adapter.ViewPagerAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.activities.LoadWebViewActivity;
import com.app.abcdapp.activities.RewardListActivity;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.ImformationVideo;
import com.app.abcdapp.model.RewardUrls;
import com.app.abcdapp.model.Transanction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LinksRewardFragment extends Fragment {
    RecyclerView recyclerView;
    Activity activity;
    Session session;
    RewardUrlAdapter rewardUrlAdapter;
    TextView tvText1, tvText2;
   Button btnCompleteJobDetail, btnInformationVideos;
   RecyclerView rvCompledJobDetail, rvInformationVideos;
    ImformationVideoAdapter imformationVideoAdapter;
    TextView tvOurLiveDashboard;
    LinearLayout llReward;
    RelativeLayout rlMain;
    private AdView mAdView;

    public LinksRewardFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_links_reward, container, false);
        activity = getActivity();
        session = new Session(activity);

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnCompleteJobDetail = root.findViewById(R.id.btnCompledJobDetail);
        btnInformationVideos = root.findViewById(R.id.btnInformationVideos);
        llReward = root.findViewById(R.id.llReward);
        rlMain = root.findViewById(R.id.rlMain);

        if (session.getData(Constant.THEME).equals("dark")) {
            rlMain.setBackgroundColor(getResources().getColor(R.color.dark));
        } else {
            rlMain.setBackgroundColor(getResources().getColor(R.color.white));
        }

        rvCompledJobDetail = root.findViewById(R.id.rvCompledJobDetail);
        rvInformationVideos = root.findViewById(R.id.rvImformation);
        tvOurLiveDashboard = root.findViewById(R.id.tvOurLiveDashboard);

        llReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RewardListActivity.class);
                startActivity(intent);
            }
        });

        // tvOurLiveDashboard set on click listener to open link in browser

        tvOurLiveDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dashboard.abcdapp.in/"));
                startActivity(browserIntent);
            }
        });

      //  showRewardWelcomeAlert();


        LinearLayoutManager freeLayoutManager = new LinearLayoutManager(getActivity());
        rvCompledJobDetail.setLayoutManager(freeLayoutManager);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvInformationVideos.setLayoutManager(linearLayoutManager);


        btnCompleteJobDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvCompledJobDetail.setVisibility(View.VISIBLE);
                rvInformationVideos.setVisibility(View.GONE);

                // btnCompleteJobDetail background color change

                btnCompleteJobDetail.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                btnInformationVideos.setBackgroundColor(getResources().getColor(R.color.grayColor));

            }
        });


        btnInformationVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvCompledJobDetail.setVisibility(View.GONE);
                rvInformationVideos.setVisibility(View.VISIBLE);



                // btnInformationVideos background color change

                btnInformationVideos.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                btnCompleteJobDetail.setBackgroundColor(getResources().getColor(R.color.grayColor));


            }
        });

        explorePaid();


        explorefree();
        linkLists();



        tvText1 = root.findViewById(R.id.tvText1);
        tvText2 = root.findViewById(R.id.tvText2);


        Animation animation = new TranslateAnimation(1000, 0, 0, 0);
        Animation animation1 = new TranslateAnimation(0, 1000, 0, 0);
        animation.setDuration(5000);
        animation1.setDuration(5000);
        animation.setRepeatCount(Animation.INFINITE);
        animation1.setRepeatCount(Animation.INFINITE);
        tvText1.startAnimation(animation);
        tvText2.startAnimation(animation1);


        return root;
    }


    public void showRewardWelcomeAlert() {
        session.setBoolean(Constant.WELLCOMEALERT,true);
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//    builder.setTitle("Name");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.rewards_lyt, null);
        builder.setView(customLayout);

        // add a button
        ImageButton btnClose = customLayout.findViewById(R.id.btnCancel);


        // create and show the alert dialog
        AlertDialog dialog = builder.create();

        // set animation for the alert dialog
        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.DialogAnimation; // replace with your own animation style
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }
    private void explorePaid() {

        // create new RewardUrls instances and set their properties


        Map<String, String> params = new HashMap<>();
        params.put(Constant.TYPE, "job");
        ApiConfig.RequestToVolley((result, response) -> {


            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("job",response);
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<RewardUrls> rewardUrls = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                RewardUrls group = g.fromJson(jsonObject1.toString(), RewardUrls.class);
                                rewardUrls.add(group);
                            } else {
                                break;
                            }
                        }
                        //important
                        rewardUrlAdapter = new RewardUrlAdapter(activity, rewardUrls);
                        rvCompledJobDetail.setAdapter(rewardUrlAdapter);
                    } else {
                        Toast.makeText(getActivity(), "" + String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }



        }, activity, Constant.JOB_DETAILS, params, false);


    }
    private void explorefree() {

        // create new RewardUrls instances and set their properties



        Map<String, String> params = new HashMap<>();
        params.put(Constant.TYPE,"info" );
        ApiConfig.RequestToVolley((result, response) -> {


            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("info",response);
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<ImformationVideo> imformationVideos = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                ImformationVideo group = g.fromJson(jsonObject1.toString(), ImformationVideo.class);
                                imformationVideos.add(group);
                            } else {
                                break;
                            }
                        }
                        //important
                        imformationVideoAdapter = new ImformationVideoAdapter(activity, imformationVideos);
                        rvInformationVideos.setAdapter(imformationVideoAdapter);
                    } else {
                        Toast.makeText(getActivity(), "" + String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }



        }, activity, Constant.JOB_DETAILS, params, false);


    }




    private void linkLists() {
        Map<String, String> params = new HashMap<>();

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    tvText1.setText(jsonObject.getString("latest_joined"));
                    tvText2.setText(jsonObject.getString("latest_refer"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.EXPLORE_INFO, params, true);

    }

}