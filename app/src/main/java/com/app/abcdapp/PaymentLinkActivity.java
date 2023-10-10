package com.app.abcdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.abcdapp.helper.Constant;

import im.delight.android.webview.AdvancedWebView;

public class PaymentLinkActivity extends AppCompatActivity implements AdvancedWebView.Listener{
    private AdvancedWebView mWebView;
    Activity activity;
    String project_type = "",link = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_link);
        activity = PaymentLinkActivity.this;
        project_type = getIntent().getStringExtra(Constant.PROJECT_TYPE);

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(activity, this);
        mWebView.setMixedContentAllowed(true);
        mWebView.setDesktopMode(true);

        // Set the user-agent to mimic a desktop browser
        WebSettings webSettings = mWebView.getSettings();
        String desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
        webSettings.setUserAgentString(desktopUserAgent);

        // Other settings for your WebView, if necessary
        webSettings.setJavaScriptEnabled(true);

        // Load the URL
        if(project_type.equals("regular")){
            link = "https://slveenterprises.org/product/30059624/Database-Creation-Training";

        }else if (project_type.equals("amail")){
            link = "https://slveenterprises.org/product/30059624/Database-Creation-Training";

        }else {
            link = "https://slveenterprises.org/product/30257685/Skill-Training-On-Database-Creation---1-Year";

        }
        mWebView.loadUrl(link);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                if (url.startsWith("phonepe") || url.startsWith("tez") || url.startsWith("paytmmp")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);




                }

                return true;
            }

        });
    }
    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Log.d("PAYMENT_LINK",url);

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}