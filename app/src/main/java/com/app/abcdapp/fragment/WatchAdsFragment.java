package com.app.abcdapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.abcdapp.R;
import com.app.abcdapp.activities.PurchaseServerActivity;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.google.android.material.progressindicator.CircularProgressIndicator;


public class WatchAdsFragment extends Fragment {


    private VideoView videoView;
    private Button btnGenerate,btnBuyServer;
    private CircularProgressIndicator cbTrail;
    Activity activity;
    Session session;
    ImageView ivInfo;
    TextView tvTrialPeriod;




    public WatchAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_watch_ads, container, false);

        activity = requireActivity();
        session = new Session(requireActivity());

        videoView = root.findViewById(R.id.videoView);
        btnGenerate = root.findViewById(R.id.btnGenerate);
        btnBuyServer = root.findViewById(R.id.btnBuyServer);
        cbTrail = root.findViewById(R.id.cbTrail);
        ivInfo = root.findViewById(R.id.ivInfo);
        tvTrialPeriod = root.findViewById(R.id.tvTrialPeriod);

        cbTrail = root.findViewById(R.id.cbTrail);
        cbTrail.setProgress(session.getInt(Constant.REGULAR_TRIAL_COUNT));
        cbTrail.setMax(10);

        String videoUrl = "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4"; // Replace with your video URL
        Uri videoUri = Uri.parse(videoUrl);

        MediaController mediaController = new MediaController(getContext());
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.start();


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                // Use the video width and height as needed
//                Log.d("Video Dimensions", "Width: " + videoWidth + ", Height: " + videoHeight);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Show the button when the video completes
                btnGenerate.setVisibility(View.VISIBLE);

            }
        });








        btnBuyServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), PurchaseServerActivity.class);
                startActivity(intent);            }
        });

        Animation blink = new AlphaAnimation(0.0f, 1.0f);
        blink.setDuration(500);
        blink.setInterpolator(new LinearInterpolator());
        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.REVERSE);
        btnBuyServer.startAnimation(blink);
        tvTrialPeriod.startAnimation(blink);


        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a custom dialog box and set its content view
                Dialog infoDialog = new Dialog(activity);
                infoDialog.setContentView(R.layout.info_dialog_layout);

                // Show the dialog box
                infoDialog.show();
            }
        });


        return root;
    }
}