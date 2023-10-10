package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.abcdapp.Adapter.ScratchAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.Scratch;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RewardListActivity extends AppCompatActivity {


    Activity activity ;
    RecyclerView rvRewardList;
    ScratchAdapter scratchAdapter;
    Session session;
    ImageButton ibBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_list);

        activity = RewardListActivity.this;
        session = new Session(activity);

        rvRewardList = findViewById(R.id.rvRewardList);
        ibBack = findViewById(R.id.ibBack);

        ibBack.setOnClickListener(v -> {

            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);

        });


        rvRewardList.setLayoutManager(new GridLayoutManager(activity, 2));

       rvlist();


    }

    private void rvlist() {




        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("CAT_RES",response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        Log.d("CAT_RES",response);
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<Scratch> scratches = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                Scratch group = g.fromJson(jsonObject1.toString(), Scratch.class);
                                scratches.add(group);
                            } else {
                                break;
                            }
                        }

                        //important
                        scratchAdapter = new ScratchAdapter(activity, scratches);
                       rvRewardList.setAdapter(scratchAdapter);




                    }
                    else {
                        // Toast.makeText(getActivity(), ""+String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }



        }, activity, Constant.SCARCH_LIST_URL, params, true);





    }
}