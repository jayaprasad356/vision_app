package com.app.abcdapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class InFoFragment extends Fragment {
    Button btnRegularTask, btnAmail, btnDownloadDetails, btnDemoVideoAmail, btnDemoVideoRegular,btnChampionDemo,btnChampionTrial;
    Session session;
    //  TextView tvDashboard;
    WebView webView;
    private AdView mAdView;
    InterstitialAd interstitialAd;
    private static final String PREF_LAST_AD = "pref_last_ad";
    String plan1_video, plan2_video;
    Button btnplan1, btnplan2;
    private AlertDialog alertDialog;
    String id, userId, discount, expiryDate, isScratched, status;
    LinearLayout relativeLayout;


    public InFoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_in_fo, container, false);
        session = new Session(getActivity());

        rvlist();


        // Create an InterstitialAd object
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-7994561209093816/5762531710");

// Set an AdListener to handle ad loading and showing events
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Ad loaded successfully, you can now show it
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                // Ad failed to load, handle the error
            }

            @Override
            public void onAdClosed() {
                // Ad closed by the user, you can now load a new one
            }
        });

// Load the ad
        loadAd();


        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        btnplan1 = root.findViewById(R.id.btnplan1);
        btnplan2 = root.findViewById(R.id.btnplan2);
        btnRegularTask = root.findViewById(R.id.btn_trail_regular);
        btnAmail = root.findViewById(R.id.btn_trail_champion);
        btnDownloadDetails = root.findViewById(R.id.btnDownloadDetails);
        btnDemoVideoAmail = root.findViewById(R.id.btn_demo_video_amail);
        btnDemoVideoRegular = root.findViewById(R.id.btn_demo_video_regular);
        btnChampionDemo = root.findViewById(R.id.btnChampionDemo);
        btnChampionTrial = root.findViewById(R.id.btnChampionTrial);
        // tvDashboard=root.findViewById(R.id.tvDashboard);
        webView = root.findViewById(R.id.webview);
        webView.setVerticalScrollBarEnabled(true);
        webView.loadDataWithBaseURL("", session.getData(Constant.MAIN_CONTENT), "text/html", "UTF-8", "");
        btnRegularTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.Container, new HomeFragment()).commit();
            }
        });
        btnAmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.Container, new AmailFragment()).commit();
            }
        });
        btnChampionTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.Container, new FindMissingFragment()).commit();
            }
        });
        btnDownloadDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("" + session.getData(Constant.JOB_DETAILS_LINK)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        btnplan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(plan1_video); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        btnplan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(plan2_video);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });




//        tvDashboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = "https://dashboard.abcdapp.in/";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
//        });
        btnDemoVideoAmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("" + session.getData(Constant.AMAIL_DEMO_LINK)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        btnChampionDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("" + session.getData(Constant.CHAMPION_DEMO_LINK)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        btnDemoVideoRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("" + session.getData(Constant.REGULAR_DEMO_LINK)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        riseTicketCheck();


        return root;
    }

    private void loadAd() {
        long currentTime = System.currentTimeMillis();
        long lastReferearnTime = getLastReferearnTime();
        if (currentTime - lastReferearnTime < 10 * 60 * 1000) { // 30 minutes
            return;
        }
        setLastReferearnTime(currentTime);

        interstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private long getLastReferearnTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getLong(PREF_LAST_AD, 0);
    }

    private void setLastReferearnTime(long time) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_LAST_AD, time);
        editor.apply();
    }


    @Override
    public void onDestroyView() {
        // Destroy the AdView when the fragment is destroyed
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroyView();
    }


    private void riseTicketCheck() {
        Map<String, String> params = new HashMap<>();
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("SETTINGS_DATA", response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);


                        plan1_video = jsonArray.getJSONObject(0).getString("plan1_video");
                        plan2_video = jsonArray.getJSONObject(0).getString("plan2_video");


                    } else {
                        // Toast.makeText(requireActivity(), "Please try again later", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(requireActivity(), , Toast.LENGTH_SHORT).show();

                }


            }

            //pass url
        }, requireActivity(), Constant.SETTINGS_URL, params, false);

    }




    private void openCustomDialog() {
        if (!isAdded()) {
            return; // Fragment is not attached to an activity, exit the method
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        relativeLayout = dialogView.findViewById(R.id.ll1);
        TextView tvPrice = dialogView.findViewById(R.id.tvPrice);
        TextView tvDate = dialogView.findViewById(R.id.tvDate);
        TextView tvVoucherId = dialogView.findViewById(R.id.tvVocherid);
        ScratchView scratchView = dialogView.findViewById(R.id.scratchView);
        View overlayView = dialogView.findViewById(R.id.scratchView);
        ImageButton ibClose = dialogView.findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        Button btnShare = dialogView.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        tvDate.setText("Valid till - " + expiryDate);
        tvPrice.setText("Rs. " + discount);
        tvVoucherId.setText("#" + id);

        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                // Code to be executed when the ScratchView is fully revealed
                overlayView.setVisibility(View.GONE);
                btnShare.setVisibility(View.VISIBLE);

                scartch_finish();

            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                Log.d("Revealed", String.valueOf(percent));
            }
        });

        // Create and show the dialog
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void rvlist() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("SCRATCH_RES", response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("CAT_RES", response);

                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);


                        if (jsonArray.length() > 0) {
                            // Get the first data object
                            JSONObject firstDataObject = jsonArray.getJSONObject(0);

                            // Extract values from the first data object
                            id = firstDataObject.getString("id");
                            userId = firstDataObject.getString("user_id");
                            discount = firstDataObject.getString("discount");
                            expiryDate = firstDataObject.getString("expiry_date");
                            isScratched = firstDataObject.getString("is_scratched");
                            status = firstDataObject.getString("status");


                            openCustomDialog();

                        } else {
                            // No data available
                        }
                    } else {
                        Toast.makeText(getActivity(), "" + String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(requireActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, requireActivity(), Constant.MY_SCRATCH_URL, params, true);
    }
    private void scartch_finish() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.SCRATCH_ID, id);

        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("CAT_RES", response);

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("CAT_RES", response);

                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);



                    } else {
                        Toast.makeText(getActivity(), "" + String.valueOf(jsonObject.getString(Constant.MESSAGE)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(requireActivity(), String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, requireActivity(), Constant.SCRATCH_CARD, params, true);
    }


    private void share() {


        // Scale down the dimensions of the layout
        int scaleFactor = 2; // Scale down by a factor of 2
        int scaledWidth = relativeLayout.getMeasuredWidth() / scaleFactor;
        int scaledHeight = relativeLayout.getMeasuredHeight() / scaleFactor;

        try {
            // Create a bitmap for the scaled-down version of the layout
            Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(1f / scaleFactor, 1f / scaleFactor);
            relativeLayout.draw(canvas);

            // Save the Bitmap to a file
            File file = new File(requireContext().getExternalCacheDir(), "image.png");
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Share the image using WhatsApp
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getActivity(), requireContext().getPackageName() + ".fileprovider", file));
            shareIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(shareIntent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}