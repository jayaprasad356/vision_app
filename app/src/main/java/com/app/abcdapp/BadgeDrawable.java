package com.app.abcdapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class BadgeDrawable extends Drawable {

    private Paint badgePaint;
    private Paint textPaint;
    private Rect badgeRect = new Rect();
    private String badgeCount = "";
    private boolean isShown = false;

    public BadgeDrawable() {
        badgePaint = new Paint();
        badgePaint.setColor(Color.RED);
        badgePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setBadgeCount(int count) {
        badgeCount = Integer.toString(count);
        isShown = count > 0;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isShown) return;

        Rect bounds = getBounds();
        float centerX = bounds.exactCenterX();
        float centerY = bounds.exactCenterY();

        badgeRect.set((int) (centerX - 30), (int) (centerY - 30), (int) (centerX + 30), (int) (centerY + 30));
        canvas.drawCircle(centerX, centerY, 30f, badgePaint);
        canvas.drawText(badgeCount, badgeRect.centerX(), badgeRect.centerY() + (textPaint.getTextSize() / 3), textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
