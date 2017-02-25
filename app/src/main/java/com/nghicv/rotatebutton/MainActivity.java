package com.nghicv.rotatebutton;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mRootLayout;
    private FrameLayout mFrameLayoutMove;
    private RotateButton mBtnRotate;
    private LinearLayout mLinearLayoutMove;
    private TextView mTvSearch;

    private float x, y;
    private float mHeightScreen;
    private float mActionbarHeight;
    private float mMoveLayoutHeight;
    private float mStatusbarHeight;
    private float mTvSearchHeight;
    private boolean isGotViewHeight;
    private float mTempHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getActionBarHeight();
        mStatusbarHeight = getStatusBarHeight();
    }

    private void initViews() {
        mFrameLayoutMove = (FrameLayout) findViewById(R.id.frame_move);
        mBtnRotate = (RotateButton) findViewById(R.id.btn_rotate);
        mRootLayout = (RelativeLayout) findViewById(R.id.activity_main);
        mLinearLayoutMove = (LinearLayout) findViewById(R.id.ll_move);
        mTvSearch = (TextView) findViewById(R.id.tv_search);

        mBtnRotate.setAngel(180);
        mFrameLayoutMove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!isGotViewHeight) {
                    mHeightScreen = mLinearLayoutMove.getHeight();
                    mMoveLayoutHeight = mFrameLayoutMove.getHeight();
                    mTvSearchHeight = mTvSearch.getHeight();
                    isGotViewHeight = true;
                }
                float rawX = motionEvent.getRawX();
                float rawY = motionEvent.getRawY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = motionEvent.getX();
                        y = motionEvent.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams =
                                (RelativeLayout.LayoutParams) mLinearLayoutMove.getLayoutParams();
                        mTempHeight =
                                rawY - y - mActionbarHeight - mStatusbarHeight - mTvSearchHeight;

                        if (mTempHeight <= 0) {
                            mTempHeight = 0;
                        } else if (mTempHeight >= mHeightScreen) {
                            mTempHeight = mHeightScreen;
                        }

                        layoutParams.height = (int) mTempHeight;
                        mBtnRotate.setAngel(mTempHeight * 180 / (mHeightScreen));
                        mLinearLayoutMove.setLayoutParams(layoutParams);
                        //mFrameLayoutMove.setLayoutParams(layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        setupSizeMovedLayout(mTempHeight);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void getActionBarHeight() {
        // Calculate ActionBar's height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            mActionbarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getResources().getDisplayMetrics());
        }
    }

    public float getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setupSizeMovedLayout(final float height) {
        if (mHeightScreen <= 0) {
            return;
        }

        if (height >= mHeightScreen || height <= 0) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) mLinearLayoutMove.getLayoutParams();
                int heightSize;
                if (height <= 30) {
                    heightSize = 0;
                } else if (height + 30 >= mHeightScreen) {
                    heightSize = (int) mHeightScreen;
                } else {
                    heightSize = (int) (height >= mHeightScreen / 2 ? height + 30 : height - 30);
                }
                layoutParams.height = heightSize;
                mLinearLayoutMove.setLayoutParams(layoutParams);
                mBtnRotate.setAngel(heightSize * 180 / mHeightScreen);
                setupSizeMovedLayout(heightSize);
            }
        }, 1);
    }
}
