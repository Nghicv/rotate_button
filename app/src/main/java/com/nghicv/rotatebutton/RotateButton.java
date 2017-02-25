package com.nghicv.rotatebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nghic on 2/18/2017.
 */

public class RotateButton extends View {

    private Bitmap mBitmap;
    private float mAngel;

    public RotateButton(Context context) {
        super(context);
    }

    public RotateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadBitmap();
    }

    public RotateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RotateButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mBitmap == null) {
            return;
        }

        int desiredWidth = mBitmap.getWidth();
        int desireHeight = mBitmap.getHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(desiredWidth, widthSize);
                break;
            default:
                width = desiredWidth;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(desireHeight, heightSize);
                break;
            default:
                height = desireHeight;
                break;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        /*matrix.postTranslate(-mBitmap.getWidth()/2, -mBitmap.getHeight()/2);
        matrix.postRotate(mAngel);
        matrix.postTranslate(getWidth()/2, getHeight()/2);*/
        matrix.setRotate(mAngel, getWidth()/2, getHeight()/2);
        canvas.drawBitmap(mBitmap, matrix, null);
    }

    private void loadBitmap() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable
                .ic_chevron_double_down_grey600_36dp);
    }

    public void setAngel(float angel) {
        mAngel = angel;
        invalidate();
    }
}
