package com.app.abcdapp;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class NoMatchHighlightTextView extends AppCompatTextView {

    public NoMatchHighlightTextView(Context context) {
        super(context);
    }

    public NoMatchHighlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoMatchHighlightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        CharSequence text = getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            BackgroundColorSpan[] spans = spannable.getSpans(0, spannable.length(), BackgroundColorSpan.class);
            for (BackgroundColorSpan span : spans) {
                spannable.removeSpan(span);
            }
        }
    }
}
