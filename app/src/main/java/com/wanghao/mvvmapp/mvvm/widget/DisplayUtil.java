package com.wanghao.mvvmapp.mvvm.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class DisplayUtil {

    private static final String TAG = "DisplayUtil";

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dp2pxForFloat(Context context, float dipValue) {
        Log.d(TAG, "---dipValue ---" + dipValue);
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

  /*  public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }*/

    public static float sp2px(Context context, float value) {

        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
        return dimension;
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float getDisplayDensity(Context context) {
        if (context == null) {
            return -1;
        }
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidthDp(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // ????????????????????????
        int height = dm.heightPixels;       // ????????????????????????
        float density = dm.density;         // ???????????????0.75 / 1.0 / 1.5???
        int densityDpi = dm.densityDpi;     // ????????????dpi???120 / 160 / 240???
        // ??????????????????:????????????????????????/????????????
        int screenWidth = (int) (width / density);  // ????????????(dp)
        int screenHeight = (int) (height / density);// ????????????(dp)
        return screenWidth;
    }


    public static int getScreenHeightDp(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // ????????????????????????
        int height = dm.heightPixels;       // ????????????????????????
        float density = dm.density;         // ???????????????0.75 / 1.0 / 1.5???
        int densityDpi = dm.densityDpi;     // ????????????dpi???120 / 160 / 240???
        // ??????????????????:????????????????????????/????????????
        int screenWidth = (int) (width / density);  // ????????????(dp)
        int screenHeight = (int) (height / density);// ????????????(dp)
        return screenHeight;
    }

    /**
     * ?????????????????????
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
