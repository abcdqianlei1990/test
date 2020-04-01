package cn.xzj.agent.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by sikong on 16/4/21.
 * 强制关闭软件盘的工具类
 */
public class KeyBoardUtil {
    /*
    强制关闭软件盘并清空内容
     */
    public static boolean close(Context context, EditText Ev) {
        if (Ev != null) {
            try {
                InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                return manager.hideSoftInputFromWindow(Ev.getWindowToken(), 0);
            } catch (Exception e) {
                Log.e("EditTextUtil------", "closeEdit: 关闭失败");
                return false;
            }


        }
        return false;
    }

    /**
     * 关闭软键盘
     *
     */
    public static void close(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
