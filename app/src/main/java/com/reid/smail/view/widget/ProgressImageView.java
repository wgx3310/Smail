package com.reid.smail.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by reid on 2017/12/6.
 */

public class ProgressImageView extends AppCompatImageView {

    public static final int FONT_SIZE = 14;
    public static final int ROUND_WIDTH = 50;
    public static final int STROKE_WIDTH = 7;
    private int mFontSize;
    private int mRoundWidth;
    private int mStrokeWidth;
    private Paint mPaint;
    private boolean mShowProgress;
    private int mProgress;
    private float mTextY;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private RectF mOval;

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
        float scale = getResources().getDisplayMetrics().density;

        mFontSize = (int) (FONT_SIZE * scale);
        mRoundWidth = (int) (ROUND_WIDTH * scale);
        mStrokeWidth = (int) (STROKE_WIDTH * scale);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mFontSize);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        mRadius = mRoundWidth / 2;

        mTextY = mCenterY + mFontSize * 11.0f / 28;

        mOval = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX
                + mRadius, mCenterY + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mShowProgress || mProgress == 100){
            return;
        }

        if (mCenterX == 0 || mCenterY == 0) {
            init();
        }
        // 画最外层的大圆环
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);

        // 画进度百分比
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.BLACK);
        mPaint.setTypeface(Typeface.MONOSPACE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        String progressStr = mProgress + "%";
        canvas.drawText(progressStr, mCenterX, mTextY, mPaint);

        // 画圆环的进度
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(mOval, 0, 360 * mProgress / 100, false, mPaint);
    }

    public void setProgress(int progress) {
        if (mShowProgress){
            if (progress >= 0 && progress <= 100){
                if (mProgress != progress){
                    mProgress = progress;
                    postInvalidate();
                }

                if (mProgress == 100){
                    mShowProgress = false;
                }
            }else {
                mShowProgress = false;
            }
        }
    }

    public void showProgress(boolean show){
        mShowProgress = show;
    }

}
