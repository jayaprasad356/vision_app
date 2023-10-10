package com.app.abcdapp.activities;

import static com.app.abcdapp.helper.Constant.SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.app.abcdapp.Adapter.FAQAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.FAQS;
import com.app.abcdapp.model.Transanction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaqActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton toolbar;
    Activity activity;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        activity = this;
        session = new Session(activity);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getFAQS();

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
                            FAQAdapter productAdapter = new FAQAdapter(faqs, FaqActivity.this);
                            recyclerView.setAdapter(productAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, activity, Constant.FAQ_LIST, params, true);
        }

    }

}