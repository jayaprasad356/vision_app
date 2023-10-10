package com.app.abcdapp.java;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class PasteEditText extends EditText {

    private Drawable drawableRight;
    private Rect bounds;

    public PasteEditText(Context context) {
        super(context);
        init();
    }

    public PasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawableRight = getCompoundDrawables()[2]; // Assuming drawableRight is the third (index 2) drawable
        bounds = drawableRight.getBounds();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (getRight() - bounds.width())) {
                // Clicked on drawableRight (paste icon)
                pasteTextFromClipboard();
                return true;
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
                getText().insert(getSelectionStart(), textToPaste);
            }
        }
    }
}
