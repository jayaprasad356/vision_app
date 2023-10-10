package com.app.abcdapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.abcdapp.Adapter.ImformationVideoAdapter;
import com.app.abcdapp.Adapter.RewardUrlAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.model.ImformationVideo;
import com.app.abcdapp.model.RewardUrls;

import java.util.ArrayList;


public class InformationVideosFragment extends Fragment {

    public InformationVideosFragment() {
        // Required empty public constructor
    }

    RecyclerView rvPaidJobs;
    ImformationVideoAdapter imformationVideoAdapter;
    ArrayList<ImformationVideo> imformationVideos = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_information_videos, container, false);


        rvPaidJobs=root.findViewById(R.id.rvPaidJobs);


        LinearLayoutManager freeLayoutManager = new LinearLayoutManager(getActivity());
        rvPaidJobs.setLayoutManager(freeLayoutManager);





        return root;
    }






}