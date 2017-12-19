package com.reid.smail.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.reid.smail.R;

/**
 * Created by reid on 2017/12/6.
 */

public class ProgressImageView extends AppCompatImageView {

    private Paint mPaint;

    public ProgressImageView(Context context) {
        this(context, null);
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getDrawable() != null) return;

        if (mPaint == null) {
            init();
        }

        mPaint.setColor(getResources().getColor(R.color.icon));
        canvas.drawRect(0,0,getWidth(), getHeight(), mPaint);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_drawer_shot);
        Bitmap bitmap = drawable.getBitmap();
        int left = getWidth() / 2- bitmap.getWidth()/2;
        int top = getHeight() /2 - bitmap.getHeight() /2;
        canvas.drawBitmap(bitmap, left, top, mPaint);
    }

}
