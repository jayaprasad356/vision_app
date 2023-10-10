package com.app.abcdapp.java;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatTextView;

public class PasteTextView extends EditText {

    public PasteTextView(Context context) {
        super(context);
        init();
    }

    public PasteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize the view
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int drawableRightStart = getWidth() - getTotalPaddingRight();
                int drawableRightEnd = getWidth() - getPaddingRight();
                if (event.getX() >= drawableRightStart && event.getX() <= drawableRightEnd) {
                    // Clicked on drawableRight (paste icon)
                    pasteTextFromClipboard();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void pasteTextFromClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            if (item != null && item.getText() != null) {
                CharSequence textToPaste = item.getText();
                setText(textToPaste); // Set the text to the pasted content
            }
        }
    }
}
