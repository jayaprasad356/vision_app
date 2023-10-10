package com.app.abcdapp.activities;

import static com.app.abcdapp.chat.constants.IConstants.CATEGORY;
import static com.app.abcdapp.chat.constants.IConstants.EXTRA_USER_ID;
import static com.app.abcdapp.chat.constants.IConstants.NAME;
import static com.app.abcdapp.chat.constants.IConstants.TICKET_ID;
import static com.app.abcdapp.chat.constants.IConstants.TYPE;
import static com.app.abcdapp.helper.Constant.DESCRIPTION;
import static com.app.abcdapp.helper.Constant.REFERRED_BY;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.abcdapp.R;
import com.app.abcdapp.chat.MessageActivity;
import com.app.abcdapp.chat.TicketFragment;
import com.app.abcdapp.chat.models.Ticket;
import com.app.abcdapp.fragment.AmailFragment;
import com.app.abcdapp.fragment.BlankFragment;
import com.app.abcdapp.fragment.ExploreFragment;
import com.app.abcdapp.fragment.FaqFragment;
import com.app.abcdapp.fragment.FindMissingFragment;
import com.app.abcdapp.fragment.HomeFragment;
import com.app.abcdapp.fragment.InFoFragment;
import com.app.abcdapp.fragment.LinksRewardFragment;
import com.app.abcdapp.fragment.ProfileFragment;
import com.app.abcdapp.fragment.SupportFragment;
import com.app.abcdapp.fragment.WalletFragment;
import com.app.abcdapp.helper.ApiConfig;
import com.app.abcdapp.helper.Constant;
import com.app.abcdapp.helper.DatabaseHelper;
import com.app.abcdapp.helper.Session;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kiprotich.japheth.TextAnim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static FragmentManager fm = null;
    private BottomNavigationView navbar;
    Activity activity;
    Session session;
    String NOTIFY_CHAT;
    Dialog dialog = null;
    long fetch_time;
    DatabaseHelper databaseHelper;
    String RandomId;
    DatabaseReference reference;
    Button btnChatUs;

    BadgeDrawable badgeDrawable;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_ENABLE_GPS = 2;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        navbar = findViewById(R.id.bottomNavigation);
        navbar.setSelectedItemId(R.id.Home);
        activity = MainActivity.this;
        databaseHelper = new DatabaseHelper(activity);
        session = new Session(activity);
        NOTIFY_CHAT = getIntent().getStringExtra("NOTIFY_CHAT");
        if (session.getBoolean(Constant.CHECK_NOTIFICATION)) {
            showReadNotificationPopup();
        }else if (session.getBoolean(Constant.SUPPORT_MESSAGE)){
            showSupportNotificationPopup();

        }








        if (NOTIFY_CHAT != null) {
            if (NOTIFY_CHAT.equals("join_chat")) {
                checkJoining();
            } else {
                navbar.setSelectedItemId(R.id.Support);
                fm.beginTransaction().replace(R.id.Container, new SupportFragment()).commit();


            }


        } else {


            if (session.getData(Constant.STATUS).equals("0")) {
                fm.beginTransaction().replace(R.id.Container, new InFoFragment()).commitAllowingStateLoss();
            } else {
                navbar.setSelectedItemId(R.id.Explore);
                fm.beginTransaction().replace(R.id.Container, new ExploreFragment()).commitAllowingStateLoss();
            }

        }
        //badgeDrawable = new BadgeDrawable();

        setBadgeValue();





        //badgeDrawable.sette(Color.WHITE);



        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Profile:
                        fm.beginTransaction().replace(R.id.Container, new ProfileFragment()).commitAllowingStateLoss();
                        break;
                    case R.id.Home:
                        if (session.getData(Constant.STATUS).equals("0")) {
                            fm.beginTransaction().replace(R.id.Container, new InFoFragment()).commitAllowingStateLoss();
                        } else {
                            if (session.getData(Constant.PROJECT_TYPE).equals("amail"))
                                fm.beginTransaction().replace(R.id.Container, new AmailFragment()).commitAllowingStateLoss();
                            else if(session.getData(Constant.PROJECT_TYPE).equals("champion"))
                                fm.beginTransaction().replace(R.id.Container, new FindMissingFragment()).commitAllowingStateLoss();
                            else
                                fm.beginTransaction().replace(R.id.Container, new HomeFragment()).commitAllowingStateLoss();

                        }
                        break;
                    case R.id.Wallet:
                        try {
                            fetch_time = Long.parseLong(session.getData(Constant.FETCH_TIME)) * 1000;
                        } catch (Exception e) {
                            fetch_time = 5 * 1000;


                        }
                        showWallet();

                        break;

                    case R.id.Support:

                        fm.beginTransaction().replace(R.id.Container, new SupportFragment()).commitAllowingStateLoss();

//
//                        if (session.getData(Constant.STATUS).equals("0")) {
//                            sendMsgToWhatsapp(session.getData(Constant.CUSTOMER_SUPPORT_MOBILE),"com.whatsapp");
//
//                            //checkJoining();
//                        } else {
//
//                              fm.beginTransaction().replace(R.id.Container, new FaqFragment()).commitAllowingStateLoss();
//                        }
                        break;
                    case R.id.Explore:
                        fm.beginTransaction().replace(R.id.Container, new ExploreFragment()).commitAllowingStateLoss();
                        //showAlertDialogExplore();

                        break;
                }
                return true;
            }
        });

        //addBadgeToMenuItem(navbar, R.id.Support);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            session.setData(Constant.FCM_ID, token);
        });

    }

    public  void setBadgeValue() {
        int supportMessageCount = session.getInt(Constant.SUPPORT_MESSAGE_COUNT);

        if (supportMessageCount != 0) {
            badgeDrawable = navbar.getOrCreateBadge(R.id.Support);
            badgeDrawable.setNumber(supportMessageCount);
            badgeDrawable.setBackgroundColor(Color.RED);
        } else {
            // If the support message count is 0, remove the badge
            navbar.removeBadge(R.id.Support);
        }
    }

    private void showSupportNotificationPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(session.getData(Constant.SUPPORT_MESSAGE_TITLE)).setMessage(session.getData(Constant.SUPPORT_MESSAGE_DESC))
                .setCancelable(false)
                .setPositiveButton("reply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.setBoolean(Constant.SUPPORT_MESSAGE, false);
                        sendMsgToWhatsapp(session.getData(Constant.CUSTOMER_SUPPORT_MOBILE),"com.whatsapp",session.getData(Constant.SUPPORT_MESSAGE_DESC));

                    }
                }).setNegativeButton("later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        session.setInt(Constant.SUPPORT_MESSAGE_COUNT ,session.getInt(Constant.SUPPORT_MESSAGE_COUNT) + 1);
                        session.setBoolean(Constant.SUPPORT_MESSAGE, false);
                        setBadgeValue();
                        dialogInterface.cancel();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isDrawOverOtherAppsEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (appOpsMgr != null) {
                int mode = appOpsMgr.checkOpNoThrow(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, android.os.Process.myUid(), context.getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return false;
    }
//    private void setBadgeCount(int count) {
//        if (badgeDrawable != null) {
//            badgeDrawable.setBadgeCount(count);
//            navbar.postInvalidate(); // Request to redraw the BottomNavigationView
//        }
//    }

    // Helper method to add badge to menu item
    private void addBadgeToMenuItem(BottomNavigationView bottomNavigationView, int menuItemId) {
        MenuItem menuItem = bottomNavigationView.getMenu().findItem(menuItemId);
        View actionView = LayoutInflater.from(this).inflate(R.layout.badge_layout, bottomNavigationView, false);
        TextView badgeTextView = actionView.findViewById(R.id.badge_text_view);
        badgeTextView.setBackground(badgeDrawable);
        menuItem.setActionView(actionView);
    }



    private void accessLocation() {

        // Check if the ACCESS_FINE_LOCATION permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, so check if GPS is enabled

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                // GPS is enabled, so get the user's location
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        session.setData(Constant.LATITUDE, latitude + "");
                        session.setData(Constant.LONGTITUDE, longitude + "");
                        // Do something with the latitude and longitude
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } else {
                // GPS is not enabled, so prompt the user to enable it
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_ENABLE_GPS);
            }
        } else {
            // Permission is not granted, so request it from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

    }
    private void sendMsgToWhatsapp(String mobile, String packageName,String message) {
        String phoneNumber = "+91" + mobile;

        PackageManager packageManager = activity.getPackageManager(); // Call packageManager on the activity instance
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message; // Include the message in the URL

            i.setPackage(packageName);
            i.setData(Uri.parse(url));

            activity.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkJoining() {
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE));
        FirebaseDatabase.getInstance()
                .getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Ticket user = dataSnapshot.getValue(Ticket.class);
                            sendChat(user.getId(), user.getName(), user.getCategory(), user.getType(), user.getDescription());

                        } else {
                            joinTicket();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    // Handle the user's permission and GPS enablement responses
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, so check if GPS is enabled
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // GPS is enabled, so get the user's location
                    // ...
                } else {
                    // GPS is not enabled, so prompt the user to enable it
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_ENABLE_GPS);
                }
            } else {
                Toast.makeText(this, "Please Give Location Permission For Verify User", Toast.LENGTH_SHORT).show();
                // Permission is not granted, so handle the error
                // ...
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_GPS) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is now enabled, so get the user's location
                // ...
            } else {
                Toast.makeText(activity, "GPS NOT ENABLED", Toast.LENGTH_SHORT).show();
                // GPS is still not enabled, so handle the error
                // ...
            }
        }
    }

    public void showAlertWelcome() {
        session.setBoolean(Constant.WELLCOMEALERT, true);
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//    builder.setTitle("Name");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.welcome_layout, null);
        builder.setView(customLayout);

        // add a button
        Button btnClose = customLayout.findViewById(R.id.btnClose);


        // create and show the alert dialog
        AlertDialog dialog = builder.create();

        // set animation for the alert dialog
        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.DialogAnimation; // replace with your own animation style
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void joinTicket() {
        Long tsLong = System.currentTimeMillis() / 1000;
        RandomId = session.getData(Constant.USER_ID) + "_" + tsLong.toString();
        reference = FirebaseDatabase.getInstance().getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constant.ID, RandomId);
        hashMap.put(Constant.CATEGORY, "Joining");
        hashMap.put(Constant.DESCRIPTION, "Enquiry For Joining");
        hashMap.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        hashMap.put(Constant.NAME, session.getData(Constant.NAME));
        hashMap.put(Constant.MOBILE, session.getData(Constant.MOBILE));
        hashMap.put(Constant.TYPE, Constant.JOINING_TICKET);
        hashMap.put(Constant.SUPPORT, "Admin");
        hashMap.put(Constant.REFERRED_BY, session.getData(REFERRED_BY));
        hashMap.put(Constant.TIMESTAMP, tsLong.toString());
        reference.setValue(hashMap).addOnCompleteListener(task1 -> {

            sendChat(RandomId, session.getData(Constant.NAME), "Joining", Constant.JOINING_TICKET, "Enquiry For Joining");

        });
    }

    private void sendChat(String id, String name, String category, String type, String description) {

        //Log.d("CHAT_DETAILS","USER_ID - "+id + "\nName - "+name+"\nCategory - "+category+"\nType - "+type +"Description - "+description);
        final Intent intent = new Intent(activity, MessageActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        intent.putExtra(TICKET_ID, id);
        intent.putExtra(NAME, name);
        intent.putExtra(TYPE, type);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(CATEGORY, category);
        startActivity(intent);
    }

    private void importUrl() {
        Map<String, String> params = new HashMap<>();
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        databaseHelper.deleteUrls();
                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1 != null) {
                                databaseHelper.AddtoUrl(jsonObject1.getString(Constant.ID), jsonObject1.getString(Constant.URL), "0");
                            } else {
                                break;
                            }
                        }


                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.IMPORT_URLS, params, false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (session.getData(Constant.LATITUDE).equals("")) {
            accessLocation();
        }


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            onStop();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void walletApi() {
        finishAffinity();

    }

    @Override
    protected void onStop() {
        super.onStop();
        walletApi();
    }


    private void showWallet() {


        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Fetching Transactions");
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.dismiss();
                fm.beginTransaction().replace(R.id.Container, new WalletFragment()).commitAllowingStateLoss();


            }
        }, fetch_time);


    }

    public void showReadNotificationPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("New Notification Available.")
                .setCancelable(false)
                .setPositiveButton("Click here to read", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(activity, NotificaionActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}