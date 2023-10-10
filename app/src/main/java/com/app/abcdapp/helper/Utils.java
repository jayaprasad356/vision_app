package com.app.abcdapp.helper;


import static com.app.abcdapp.chat.constants.IConstants.REF_TOKENS;
import static com.app.abcdapp.chat.managers.Utils.IS_TRIAL;

import android.annotation.SuppressLint;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.app.abcdapp.R;
import com.app.abcdapp.chat.fcmmodels.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Utils {

    @SuppressLint("ClickableViewAccessibility")
    public static void setHideShowPassword(final EditText edtPassword) {
        edtPassword.setTag("show");
        edtPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (edtPassword.getTag().equals("show")) {
                        edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_hide, 0);
                        edtPassword.setTransformationMethod(null);
                        edtPassword.setTag("hide");
                    } else {
                        edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
                        edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                        edtPassword.setTag("show");
                    }
                    return true;
                }
            }
            return false;
        });
    }
    public static void getErrors(final Exception e) {
        if (IS_TRIAL) {
            final String stackTrace = "Pra ::" + Log.getStackTraceString(e);
            System.out.println(stackTrace);
        }
    }

    public static void uploadToken(String referenceToken) {
        try {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(REF_TOKENS);
                Token token = new Token(referenceToken);
                reference.child(firebaseUser.getUid()).setValue(token);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }
}
