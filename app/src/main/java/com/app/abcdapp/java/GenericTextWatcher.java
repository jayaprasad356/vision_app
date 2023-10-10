package com.app.abcdapp.java;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.app.abcdapp.R;

public class GenericTextWatcher implements TextWatcher {
    private final EditText[] editText;
    private View view;
    private int idcount;
    public GenericTextWatcher(View view, EditText[] editText, int idcount)
    {
        this.editText = editText;
        this.view = view;
        this.idcount = idcount;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        idcount = idcount +1;
        String text = editable.toString();
        switch (view.getId()) {

            case R.id.otp_edit_box1:
                if (text.length() == 1)
                    editText[1].requestFocus();
                break;
            case R.id.otp_edit_box2:

                if (text.length() == 1)
                    editText[2].requestFocus();
                else if (text.length() == 0)
                    editText[0].requestFocus();
                break;
            case R.id.otp_edit_box3:
                if (text.length() == 1)
                    editText[3].requestFocus();
                else if (text.length() == 0)
                    editText[1].requestFocus();
                break;
            case R.id.otp_edit_box4:
                if (text.length() == 1)
                    editText[4].requestFocus();
                else if (text.length() == 0)
                    editText[2].requestFocus();
                break;
            case R.id.otp_edit_box5:
                if (text.length() == 1)
                    editText[5].requestFocus();
                else if (text.length() == 0)
                    editText[3].requestFocus();
                break;
            case R.id.otp_edit_box6:
                if (text.length() == 1)
                    editText[6].requestFocus();
                else if (text.length() == 0)
                    editText[4].requestFocus();
                break;
            case R.id.otp_edit_box7:
                if (text.length() == 1)
                    editText[7].requestFocus();
                else if (text.length() == 0)
                    editText[5].requestFocus();
                break;
            case R.id.otp_edit_box8:
                if (text.length() == 1)
                    editText[8].requestFocus();
                else if (text.length() == 0)
                    editText[6].requestFocus();
                break;
            case R.id.otp_edit_box9:
                if (text.length() == 1)
                    editText[9].requestFocus();
                else if (text.length() == 0)
                    editText[7].requestFocus();
                break;
            case R.id.otp_edit_box10:
                if (text.length() == 0)
                    editText[8].requestFocus();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }
}
