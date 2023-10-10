package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

public class RateusActivity extends AppCompatActivity {

    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);

        Button btnRateUs = findViewById(R.id.btn);
        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewInfo != null) {
                    startReviewFlow();
                } else {
                    showRateUsOption();
                }
            }
        });

        activateReviewInfo();
    }

    void activateReviewInfo() {
        reviewManager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> managerInfoTask = reviewManager.requestReviewFlow();
        managerInfoTask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                } else {
                    showRateUsOption();
                }
            }
        });
    }

    void startReviewFlow() {
        Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Toast.makeText(RateusActivity.this, "Rating is completed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showRateUsOption() {
        // Show the rate option or any other logic you want to handle
        Toast.makeText(RateusActivity.this, "Please rate us", Toast.LENGTH_SHORT).show();
    }
}