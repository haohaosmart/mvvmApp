package com.wanghao.mvvmapp.mvvm.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mmkv.MMKV;
import com.wanghao.mvvmapp.R;


public class CommonPageTitle extends RelativeLayout {

    private static final String TAG = "PageTitle";
    private RelativeLayout rlBackground;
    private LinearLayout llLeft;
    public TextView tvTitle;
    private ImageView ivLeft;
    private LinearLayout llRight;
    public TextView tvRight;
    private ImageView ivRight;


    public CommonPageTitle(Context context) {
        super(context);
        inflate(getContext(), R.layout.layout_common_page_title, this);

        rlBackground = findViewById(R.id.rlBackground);
        llLeft = findViewById(R.id.llLeft);
        llRight = findViewById(R.id.llRight);
        tvTitle = findViewById(R.id.tvTitle);
        ivLeft = findViewById(R.id.ivLeft);
        tvRight = findViewById(R.id.tvRight);
        ivRight = findViewById(R.id.ivRight);
    }

    public CommonPageTitle(final Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.layout_common_page_title, this);
        rlBackground = findViewById(R.id.rlBackground);
        llLeft = findViewById(R.id.llLeft);
        llRight = findViewById(R.id.llRight);
        tvTitle = findViewById(R.id.tvTitle);
        ivLeft = findViewById(R.id.ivLeft);
        tvRight = findViewById(R.id.tvRight);
        ivRight = findViewById(R.id.ivRight);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonPageTitle);
        CharSequence titleText = a.getText(R.styleable.CommonPageTitle_titleText);
        if (titleText != null) {
            tvTitle.setText(titleText);
        }

        int titleTextColor = a.getColor(R.styleable.CommonPageTitle_titleTextColor, 0);
        if (titleTextColor == 0) {
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorGrey900));
        } else {
            tvTitle.setTextColor(titleTextColor);
        }


        CharSequence rightText = a.getText(R.styleable.CommonPageTitle_rightText);
        if (rightText != null) {
            llRight.setVisibility(View.VISIBLE);
            tvRight.setText(rightText);
        }

        int rightTextColor = a.getColor(R.styleable.CommonPageTitle_rightTextColor, 0);
        if (rightTextColor == 0) {
            tvRight.setTextColor(context.getResources().getColor(R.color.colorGrey900));
        } else {
            tvRight.setTextColor(rightTextColor);
        }


        final boolean showBack = a.getBoolean(R.styleable.CommonPageTitle_showBack, false);
        llLeft.setVisibility(showBack ? View.VISIBLE : View.GONE);


        // 是否适配状态栏
        final boolean fitWindow = a.getBoolean(R.styleable.CommonPageTitle_fitWindow, true);

        llLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"---title---llLeft");
                if (showBack) {
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                }

            }
        });


        int backImgSrc = a.getResourceId(R.styleable.CommonPageTitle_backImgSrc, R.mipmap.ic_launcher);
        ivLeft.setImageResource(backImgSrc);


        int rightImgSrc = a.getResourceId(R.styleable.CommonPageTitle_titleRightImgSrc,  -1);
        if (rightImgSrc != -1){
            ivRight.setImageResource(rightImgSrc);
            llRight.setVisibility(View.VISIBLE);
        }


        int backgroundSrc = a.getResourceId(R.styleable.CommonPageTitle_backgroundSrc, R.color.white);
        rlBackground.setBackgroundResource(backgroundSrc);

        a.recycle();


        if (fitWindow) {
            fitStatusHeight(context);
        } else{
            LayoutParams layoutParams = (LayoutParams) rlBackground.getLayoutParams();
            layoutParams.height =  DisplayUtil.dp2px(context, 56);
            rlBackground.setLayoutParams(layoutParams);
        }
    }


    public void fitStatusHeight(Context context) throws NullPointerException, IllegalStateException {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        Log.d(TAG, "---statusBarHeight---" + statusBarHeight);
        LayoutParams layoutParams = (LayoutParams) rlBackground.getLayoutParams();
        layoutParams.height = statusBarHeight + DisplayUtil.dp2px(context, 56);
        rlBackground.setLayoutParams(layoutParams);
    }

    public void setTvTitle(String title) {
        if (title != null) {
            tvTitle.setText(title);
        }
    }

    public void showRight() {
        llRight.setVisibility(View.VISIBLE);
    }

    public void hideRight() {
        llRight.setVisibility(View.GONE);
    }


    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setLeftImg(int imgSrc) {
        ivLeft.setImageResource(imgSrc);
        llLeft.setVisibility(View.VISIBLE);
    }

    public void setRightText(String text) {
        llRight.setVisibility(View.VISIBLE);
        tvRight.setText(text);
    }

    public void setRightImg(int imgSrc){
        llRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(imgSrc);
    }

    public void showBack(boolean showBack) {
        llLeft.setVisibility(showBack ? View.VISIBLE : View.GONE);
        if(showBack) {
            llLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).finish();
                    }
                }
            });
        }
    }

    public void setRightOnClickListener(OnClickListener onClickListener) {
        llRight.setOnClickListener(onClickListener);
    }

    public void setLeftOnClickListener(OnClickListener onClickListener) {
        llLeft.setOnClickListener(onClickListener);
    }


    public void addRightView(View rightView){
        llRight.addView(rightView);
        llRight.setVisibility(View.VISIBLE);
    }


    public void setRightVisibility(int value){
        llRight.setVisibility(value);
    }

}