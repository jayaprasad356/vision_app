package com.app.abcdapp.activities;

import static com.app.abcdapp.helper.Constant.CHAMPION_CODES;
import static com.app.abcdapp.helper.Constant.MESSAGE;
import static com.app.abcdapp.helper.Constant.SUCCESS;
import static com.app.abcdapp.helper.Constant.URL_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.Adapter.RewardUrlAdapter;
import com.app.abcdapp.Adapter.TransactionAdapter;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.app.abcdapp.model.RewardUrls;
import com.app.abcdapp.model.ValidUrls;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.webview.AdvancedWebView;

public class LinksRewardActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;
    String Url;
    Activity activity;
    EditText etSecretCode;
    Button btnClaimnow;
    TextView Timer;
    Session session;
    String UrlId,Codes,DestinationUrl;
    Button btnSkip;
    ArrayList<String> validUrls = new ArrayList<>();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_reward);
        activity = LinksRewardActivity.this;
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        etSecretCode = findViewById(R.id.etSecretCode);
        btnClaimnow = findViewById(R.id.btnClaimnow);
        Url = getIntent().getStringExtra(Constant.URL);
        DestinationUrl = getIntent().getStringExtra(Constant.DESTINATION_URL);
        Codes = getIntent().getStringExtra(Constant.CODES);
        session = new Session(activity);
        UrlId = getIntent().getStringExtra(Constant.URL_ID);
        Timer = findViewById(R.id.Timer);
        btnSkip = findViewById(R.id.btnSkip);

        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setListener(activity, this);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        // disable third-party cookies only
        mWebView.setThirdPartyCookiesEnabled(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setClickable(true);
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.getSettings().setSupportMultipleWindows(true);
        validUrliList();

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                Timer.setText("Please Wait : "+millisUntilFinished/1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Timer.setVisibility(View.GONE);
                btnSkip.setVisibility(View.VISIBLE);
            }

        }.start();


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnClaimnow.setOnClickListener(view -> {
            if (etSecretCode.getText().toString().trim().equals("")){
                Toast.makeText(activity, "Secret Code is Empty", Toast.LENGTH_SHORT).show();
            }else {
                checkSecretCode();
            }
        });


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {



                if (validUrls.contains(Uri.parse(url).getHost())){
                    return false;

                }
                Log.d("OTHER_LINKS",url);
                // Otherwise, the link is not for a page on your site, so launch another Activity that handles URLs
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                return true;
            }
        });

    }

    private void validUrliList() {
        Map<String, String> params = new HashMap<>();
        ApiConfig.RequestToVolley((result, response) -> {
            validUrls.clear();
            Log.d("SECET_CODES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);

                        Gson g = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1 != null) {
                                validUrls.add(jsonObject1.getString(Constant.URL));
                            } else {
                                break;
                            }
                        }
                        mWebView.loadUrl(Url);

                    }else {
                        Toast.makeText(activity, ""+jsonObject.getString(MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.VALID_URLS_LIST, params, true);

    }

    private void checkSecretCode() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.USER_ID));
        params.put(Constant.URL_ID,UrlId);
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("SECET_CODES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(SUCCESS)) {
                        session.setInt(Constant.CODES, session.getInt(Constant.CODES) + Integer.parseInt(Codes));

                        Toast.makeText(activity, ""+jsonObject.getString(MESSAGE), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(activity,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(activity, ""+jsonObject.getString(MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.ADD_REWARD_URL_URL, params, true);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }



    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) {
        if (url.equals(DestinationUrl)){
            checkSecretCode();

        }

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Log.d("EXTERNAL_REQUEST_ERR",failingUrl);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) {
        Log.d("EXTERNAL_REQUEST",url);
    }

}