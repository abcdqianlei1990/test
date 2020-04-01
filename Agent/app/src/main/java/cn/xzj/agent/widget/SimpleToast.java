package cn.xzj.agent.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.xzj.agent.MyApplication;
import cn.xzj.agent.util.DisplayUtil;


/**
 * Created by yemao on 2017/1/18.
 * 提示框
 */

public class SimpleToast {

    @SuppressLint("StaticFieldLeak")
    private static Context mContent = MyApplication.application;
    private static Toast _toast;

    public static void show(String value, @Duration int duration, int gravity) throws Exception {
        if (_toast != null) {
            _toast.cancel();
        }
        try {
            _toast = Toast.makeText(mContent, value, duration);
            if (gravity == Gravity.TOP)
                _toast.setGravity(gravity, 0, DisplayUtil.dip2px(50));
            _toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            _toast = Toast.makeText(mContent, value, duration);
            if (gravity == Gravity.TOP)
                _toast.setGravity(Gravity.TOP, 0, DisplayUtil.dip2px(50));
            _toast.show();
        }
    }

    public static void showLong(String value) {
        try {
            show(value, Toast.LENGTH_LONG, Gravity.TOP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShort(int valueId){
        showShort(mContent.getString(valueId));
    }

    public static void showShort(String value) {
        try {
            show(value, Toast.LENGTH_SHORT, Gravity.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNormal(String value) {
        try {
            show(value, Toast.LENGTH_SHORT, Gravity.BOTTOM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}
}
