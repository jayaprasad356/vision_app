package com.app.abcdapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.abcdapp.Adapter.RewardUrlAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.model.RewardUrls;

import java.util.ArrayList;


public class CompleteJobDetailFragment extends Fragment {

    public CompleteJobDetailFragment() {
        // Required empty public constructor
    }


    RecyclerView rvPaidJobs;
    RewardUrlAdapter rewardUrlAdapter;
    ArrayList<RewardUrls> rewardUrls = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_complete_job_detail, container, false);

        rvPaidJobs = root.findViewById(R.id.rvPaidJobs);
        rvPaidJobs=root.findViewById(R.id.rvPaidJobs);


        LinearLayoutManager freeLayoutManager = new LinearLayoutManager(getActivity());
        rvPaidJobs.setLayoutManager(freeLayoutManager);





        return root;
    }






}