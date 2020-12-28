package com.bjcr.component_base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EdgeEffect;

import java.lang.reflect.Field;

public class ScreenUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 大小转sp
     * @param sp
     * @param context
     * @return
     */
    public static float size2sp(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());
    }

    public static float getTextHeight(Paint textPaint) {
        return -textPaint.ascent() - textPaint.descent();
    }

    public static int getCeil5(float num) {
        boolean isNegative = num < 0;
        return (((int) ((isNegative ? -num : num) + 4.9f)) / 5 * 5) * (isNegative ? -1 : 1);
    }

    public static float calcTextSuitBaseY(RectF rectF, Paint paint) {
        return rectF.top + rectF.height() / 2 -
                (paint.getFontMetrics().ascent + paint.getFontMetrics().descent) / 2;
    }

    public static int tryGetStartColorOfLinearGradient(LinearGradient gradient) {
        try {
            Field field = LinearGradient.class.getDeclaredField("mColors");
            field.setAccessible(true);
            int[] colors = (int[]) field.get(gradient);
            return colors[0];
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Field field = LinearGradient.class.getDeclaredField("mColor0");
                field.setAccessible(true);
                return (int) field.get(gradient);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return Color.BLACK;
    }

    public static void trySetColorForEdgeEffect(EdgeEffect edgeEffect, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            edgeEffect.setColor(color);
            return;
        }
        try {
            Field edgeField = EdgeEffect.class.getDeclaredField("mEdge");
            edgeField.setAccessible(true);
            Drawable mEdge = (Drawable) edgeField.get(edgeEffect);
            mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mEdge.setCallback(null);
            Field glowField = EdgeEffect.class.getDeclaredField("mGlow");
            glowField.setAccessible(true);
            Drawable mGlow = (Drawable) glowField.get(edgeEffect);
            mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mGlow.setCallback(null);
        } catch (Exception ignored) {
        }
    }

    /**
     * 获取屏幕宽
     * @param mActivity
     * @return
     */
    public static Integer getScreenWidth(Activity mActivity){
        return mActivity.getWindowManager().getDefaultDisplay().getWidth() ;
    }

    /**
     * 获取屏幕高
     * @param mActivity
     * @return
     */
    public static Integer getScreenHeight(Activity mActivity){
        return mActivity.getWindowManager().getDefaultDisplay().getHeight() ;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 判断软件盘是否显示
     * @param activity
     * @return
     */
    public static boolean isSoftInputShow(Activity activity){
        if (activity == null ||activity.isDestroyed()){
            return false;
        }
        int screenHeight = activity.getWindow().getDecorView().getHeight();//获取当前屏幕内容高度
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);//view可见区域的bottom
        return screenHeight - navigationBarHeight(activity.getWindowManager()) > rect.bottom;//考虑到虚拟导航栏的情况，选取screenHeight * 2/3判断 如果vivo手机没有使用底部导航 会出现检测不到的情况
    }

    /**
     * 隐藏软件盘
     * @param activity
     * @return
     */
    public static boolean hideSoftInputFromWindow(Activity activity){
        if (activity == null || activity.isDestroyed()){
            return false;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            return imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),0);
        }
        return false;
    }

    /**
     * 横屏可通过 widthPixels - widthPixels2 > 0 来判断底部导航栏是否存在
     * @param windowManager
     * @return true表示有虚拟导航栏 false没有虚拟导航栏
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int navigationBarHeight(WindowManager windowManager)
    {
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕高度
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(outMetrics);
        int heightPixels = outMetrics.heightPixels;
        //获取内容高度
        DisplayMetrics outMetrics2 = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics2);
        int heightPixels2 = outMetrics2.heightPixels;

        return heightPixels - heightPixels2;
    }

}
