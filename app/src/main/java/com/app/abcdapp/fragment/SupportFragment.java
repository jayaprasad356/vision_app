package com.app.abcdapp.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.NotificationAdapter;
import com.app.abcdapp.Adapter.SupportAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Notification;
import com.app.abcdapp.model.Support;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SupportFragment extends Fragment {


    RecyclerView recyclerView;
    Activity activity;
    Session session;
    Button btnChatSupport;
    TextView tvcustomerCare;


    public SupportFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_support, container, false);

        activity = getActivity();
        session = new Session(activity);

        session.setInt(Constant.SUPPORT_MESSAGE_COUNT ,0);
        session.setBoolean(Constant.SUPPORT_MESSAGE, false);

        callSetBadgeValueInMainActivity();
        recyclerView = root.findViewById(R.id.recyclerView);
        btnChatSupport = root.findViewById(R.id.btnChatSupport);
        tvcustomerCare = root.findViewById(R.id.tvcustomerCare);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));

        btnChatSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsgToWhatsapp(session.getData(Constant.CUSTOMER_SUPPORT_MOBILE),"com.whatsapp","Hi,I need Help");


            }
        });
        tvcustomerCare.setText("Customer Support Number : +91 " + session.getData(Constant.CUSTOMER_CARE));



        supportnotificationList();

        return root;
    }
    private void sendMsgToWhatsapp(String mobile, String packageName,String message) {
        String phoneNumber = "+91" + mobile;

        PackageManager packageManager = activity.getPackageManager(); // Call packageManager on the activity instance
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message +"\n\n"; // Include the message in the URL

            i.setPackage(packageName);
            i.setData(Uri.parse(url));

            activity.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
    private void callSetBadgeValueInMainActivity() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setBadgeValue();
        } else {
            Log.e("YourFragment", "Could not get reference to MainActivity.");
        }
    }


    private void supportnotificationList()
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Support> supports = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Support group = g.fromJson(jsonObject1.toString(), Support.class);
                                supports.add(group);
                            } else {
                                break;
                            }
                        }
                        SupportAdapter supportAdapter = new SupportAdapter(requireActivity(), supports);
                        recyclerView.setAdapter(supportAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.SUPPORT_NOTIFICATION_LISTS_URL, params, true);

    }

}