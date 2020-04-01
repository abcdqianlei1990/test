package cn.xzj.agent.util;

import android.util.Log;

/**
 * Created by yemao on 2017/1/18.
 *
 */

public class LogLevel {
    private static boolean Debug=true;
    public static void setDebug(boolean b){
        Debug=b;
    }
    public static void d(String tag, String value){
        if (Debug){
            Log.d(tag,value);
        }
    }
    public static void w(String tag, String value){
        if (Debug){
            Log.w(tag,value);
        }
    }
    public static void e(String tag, String value){
        if (Debug){
            Log.e(tag,value);
        }
    }
    public static void i(String tag, String value){
        if (Debug){
            Log.i(tag,value);
        }
    }
}
