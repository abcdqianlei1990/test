package cn.xzj.agent.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author yemao
 * @date 2017/4/11
 * @description 自定义的button用于点击获取验证码!
 */
@SuppressLint("AppCompatCustomView")
public class TimeButton extends TextView {
    private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "秒后重试";
    private String textbefore = "获取验证码";
    private Timer t;
    private TimerTask tt;
    private boolean isStart = false;
    private long time;

    public TimeButton(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (isStart) {
                TimeButton.this.setText(Html.fromHtml(+time / 1000 + "<font color=\"#888888\">" + textafter + "</font>"));
                time -= 1000;
                if (time < 0) {
                    reSet();
                } else {
                    setEnabled(false);
                }
            }
        }

        ;
    };

    private void initTimer() {
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                han.sendEmptyMessage(1000);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    /**
     * 开始倒计时
     */
    public void onStart() {
        isStart = true;
    }

    public void setIsStart(boolean flag) {
        isStart = flag;
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        clearTimer();
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate() {
        reSet();
        initTimer();
        t.schedule(tt, 0, 1000);
    }

    /**
     * 设置计时时候显示的文本
     */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param lenght 时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }

    public TimeButton reSet() {
        isStart = false;
        time = lenght;
        TimeButton.this.setEnabled(true);
        TimeButton.this.setText(textbefore);
        return this;
    }
}