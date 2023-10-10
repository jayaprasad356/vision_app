package com.app.abcdapp.helper;


import static com.app.abcdapp.chat.constants.IConstants.CATEGORY;
import static com.app.abcdapp.chat.constants.IConstants.EXTRA_USER_ID;
import static com.app.abcdapp.chat.constants.IConstants.NAME;
import static com.app.abcdapp.chat.constants.IConstants.TICKET_ID;
import static com.app.abcdapp.chat.constants.IConstants.TYPE;
import static com.app.abcdapp.helper.Constant.DESCRIPTION;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.abcdapp.activities.MainActivity;
import com.app.abcdapp.activities.NotificaionActivity;
import com.app.abcdapp.activities.SplashActivity;
import com.app.abcdapp.chat.MessageActivity;
import com.app.abcdapp.chat.models.Ticket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static final String TAG = "MyFirebaseMsgService";
    String RandomId;
    DatabaseReference reference;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    void sendPushNotification(JSONObject json) {
        try {
            Session session = new Session(getApplicationContext());

            JSONObject data = json.getJSONObject(Constant.DATA);

            String type = data.getString("type");
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String id = data.getString("id");
            Intent intent = null;


            if (type.equals("chat")) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("NOTIFY_CHAT", "true");

            } else if (type.equals("support_message")) {
                session.setBoolean(Constant.SUPPORT_MESSAGE, true);
                session.setData(Constant.SUPPORT_MESSAGE_TITLE, title);
                session.setData(Constant.SUPPORT_MESSAGE_DESC, message);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("NOTIFY_CHAT", Constant.SUPPORT_MESSAGE);
            } else if (type.equals("join_chat")) {
                checkJoining();
            } else {

                if (session.getBoolean("is_logged_in")) {
                    session.setBoolean(Constant.CHECK_NOTIFICATION, true);
                    intent = new Intent(getApplicationContext(), NotificaionActivity.class);

                } else {
                    intent = new Intent(getApplicationContext(), SplashActivity.class);
                }
            }


            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            mNotificationManager.showSmallNotification(title, message, intent);


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getLocalizedMessage());

        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    private void checkJoining() {
        Session session = new Session(getApplicationContext());
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE));
        FirebaseDatabase.getInstance()
                .getReference(Constant.JOINING_TICKET).child(session.getData(Constant.MOBILE)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Ticket user = dataSnapshot.getValue(Ticket.class);
                            Log.d("NOT_EXIST", user.getType() + " - " + session.getData(Constant.MOBILE));

                            sendChat(user.getId(), user.getName(), user.getCategory(), user.getType(), user.getDescription());

                        } else {
                            joinTicket(session);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void joinTicket(Session session) {
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
        hashMap.put(Constant.TIMESTAMP, tsLong.toString());
        reference.setValue(hashMap).addOnCompleteListener(task1 -> {

            sendChat(RandomId, session.getData(Constant.NAME), "Joining", Constant.JOINING_TICKET, "Enquiry For Joining");

        });
    }

    private void sendChat(String id, String name, String category, String type, String description) {

        //Log.d("CHAT_DETAILS","USER_ID - "+id + "\nName - "+name+"\nCategory - "+category+"\nType - "+type +"Description - "+description);
        final Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        intent.putExtra(TICKET_ID, id);
        intent.putExtra(NAME, name);
        intent.putExtra(TYPE, type);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(CATEGORY, category);
        startActivity(intent);
    }
}
