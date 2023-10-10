package com.app.abcdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.BuildConfig;
import com.app.abcdapp.R;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.Session;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ReferEarnActivity extends AppCompatActivity {


    ImageView backbtn;
    TextView tvRefercode,tvReferDescription;
    Button btncopy;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    String text;
    Session session;
    Activity activity;

    private static final int WIDTH = 1080;
    private static final int HEIGHT = 1920;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_earn);


        backbtn = findViewById(R.id.backbtn);
        tvRefercode = findViewById(R.id.tvRefercode);
        btncopy = findViewById(R.id.btncopy);
        tvReferDescription = findViewById(R.id.tvReferDescription);
        activity = ReferEarnActivity.this;
        session = new Session(activity);

        tvRefercode.setText(session.getData(Constant.REFER_CODE));

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        text = tvRefercode.getText().toString();

        tvReferDescription.setText(session.getData(Constant.REFER_DESCRIPTION));
        session = new Session(this);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btn = findViewById(R.id.btnGenerate);
        TextView tvName = findViewById(R.id.tvName);
     //   @SuppressLint({"MissingInflatedId", "LocalSuppress"})
      //  TextView tvText2 = findViewById(R.id.tvtext2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvtext3 = findViewById(R.id.tvtext3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvtext4 = findViewById(R.id.tvtext4);
        try {
            tvName.setText(session.getData(Constant.NAME));
          //  tvText2.setText(session.getData(Constant.EARN));
            tvtext3.setText("My Total Earning\n"+"Rs "+session.getData(Constant.EARN));
            tvtext4.setText(session.getData(Constant.REFER_CODE));
//            tvName.setText(session.getData(Constant.URL));
//            tvText2.setText(session.getData(Constant.EARN));
//            tvtext3.setText(session.getData(Constant.WITHDRAWAL));
        }catch (Exception e) {
            Toast.makeText(this, "Could not generate certificate", Toast.LENGTH_SHORT).show();
        }
        RelativeLayout lyt = findViewById(R.id.lyt);
        lyt.setDrawingCacheEnabled(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                convertLayoutToImageAndShare();


//                shareAppLink();
//                Bitmap map = Bitmap.createBitmap(lyt.getDrawingCache());
//                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),map,"title", null);
//                Uri bitmapUri = Uri.parse(bitmapPath);
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("*/*");
//                i.putExtra(Intent.EXTRA_STREAM, bitmapUri );
//                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
//                String shareMessage= "\nDOWNLOAD THE APP AND GET UNLIMITED EARNING .you can also Download App from below link and enter referral code while login-"+"\n"+text+"\n";
//                shareMessage = shareMessage +"\n https://play.google.com/store/apps/details?id=com.app.abcdapp \n\n";
//                i.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                startActivity(Intent.createChooser(i, "Share"));
            }
        });

        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
              //  shareAppLink();
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nDOWNLOAD THE APP AND GET UNLIMITED EARNING .you can also Download App from below link and enter referral code while login-"+"\n"+text+"\n";
                    shareMessage = shareMessage +"\n https://play.google.com/store/apps/details?id=com.app.abcdapp \n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }




//
//                Toast.makeText(getApplicationContext(), "Text Copied",
//                        Toast.LENGTH_SHORT).show();


            }
        });



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferEarnActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void shareAppLink() {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.appadmin.abcdapp.in/"))
                .setDomainUriPrefix("aabcduser.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.d("Link", dynamicLinkUri.toString());
        String referId = session.getData(Constant.REFER_CODE);
        String name = session.getData(Constant.NAME);

        String sharelinktext = "https://aabcduser.page.link/?" +
                "link=https://www.appadmin.abcdapp.in/myrefer.php?custid=" + referId + "-" + name +
                "&apn=" + getPackageName() +
                "&st=" + "My Refer Link" +
                "&sd=" + "Reward Coins 50";

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, sharelinktext.toString());
        intent.setType("text/plain");
        startActivity(intent);

    }


    private void convertLayoutToImageAndShare() {
        // Get the RelativeLayout from the XML layout
        RelativeLayout relativeLayout = findViewById(R.id.lyt);

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
            File file = new File(getExternalCacheDir(), "image.png");
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Share the image using WhatsApp
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file));
            shareIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(shareIntent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}