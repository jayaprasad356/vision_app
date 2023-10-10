package com.app.abcdapp.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.abcdapp.R;


import static com.app.abcdapp.helper.Constant.SUCCESS;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.abcdapp.Adapter.FAQAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.activities.FaqActivity;
import com.app.abcdapp.activities.SplashActivity;
import com.app.abcdapp.activities.TicketActivity;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.model.FAQS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.abcdapp.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaqFragment extends Fragment {

    RecyclerView recyclerView;
    ImageButton toolbar;
    View view;
    Activity activity;
    Button ticketButton;
    Session session;
    FrameLayout rl1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_faq, container, false);

        session = new Session(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
        ticketButton = view.findViewById(R.id.chatSupport);
        rl1 = view.findViewById(R.id.rl1);
        activity = getActivity();



        if (session.getData(Constant.THEME).equals("dark")) {
            rl1.setBackgroundColor(getResources().getColor(R.color.dark));
        } else {
            rl1.setBackgroundColor(getResources().getColor(R.color.white));
        }

        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsgToWhatsapp(session.getData(Constant.CUSTOMER_SUPPORT_MOBILE),"com.whatsapp");

//                Intent intent = new Intent(getActivity(), TicketActivity.class);
//                startActivity(intent);
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        getFAQS();

        return view;
    }


    private void getFAQS() {

        if (ApiConfig.isConnected(activity)) {
            Map<String, String> params = new HashMap<>();
            ApiConfig.RequestToVolley((result, response) -> {
                Log.d("Faq Api", response);
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                            ArrayList<FAQS> faqs = new ArrayList<>();
                            Gson g = new Gson();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    FAQS group = g.fromJson(jsonObject1.toString(), FAQS.class);
                                    faqs.add(group);
                                } else {
                                    break;
                                }
                            }
                            FAQAdapter productAdapter = new FAQAdapter(faqs, activity);
                            recyclerView.setAdapter(productAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, activity, Constant.FAQ_LIST, params, true);
        }

    }
    private void sendMsgToWhatsapp(String mobile, String packageName) {
        String phoneNumber = "+91" + mobile;
        String message = "Hi"; // Replace with the message you want to send
        // Replace with the phone number you want to navigate to in WhatsApp

        PackageManager packageManager = activity.getPackageManager(); // Call packageManager on the activity instance
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message; // Include the message in the URL

            i.setPackage(packageName);
            i.setData(Uri.parse(url));

            activity.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}