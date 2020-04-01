package cn.xzj.agent.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;


import java.util.List;

import cn.xzj.agent.MyApplication;


/**
 * Created by yemao on 16/12/29.
 */

public class DisplayUtil {

    //将dip转换为px 保证尺寸大小不变
    public static int dip2px(int dipValues) {
        Context context = MyApplication.application;
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dipValues + 0.5);
    }

    /**
     * 转dp
     *
     * @param px
     * @return
     */
    public static float todip(float px) {
        Context context = MyApplication.application;
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
        return dp;
    }

    //将px转换为dip 保证尺寸大小不变
    public static int px2dip(float pxValues) {
        Context context = MyApplication.application;
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValues / density + 0.5);

    }

    //将sp转换为px 保证尺寸大小不变
    public static int sp2px(float spValues) {
        Context context = MyApplication.application;
        float scaled = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValues * scaled + 0.5);
    }

    //将px转换为sp 保证尺寸大小不变
    public static int px2sp(float pxValues) {
        Context context = MyApplication.application;
        float scaled = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValues / scaled + 0.5);
    }

    //得到屏幕宽度(像素)
    public static int getWindowWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    //得到屏幕高度(像素)
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
    public static int getScreenW(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    public static int getScreenH(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
    public static boolean isTopActivity(Activity activity){
        return activity!=null && isTopActivity(activity, activity.getClass().getName());
    }
    public static boolean isTopActivity(Context context, String activityName){
        return isForeground(context, activityName);
    }
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
