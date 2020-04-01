package cn.xzj.agent.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import cn.xzj.agent.constants.Constants;
import cn.xzj.agent.constants.EnumValue;
import cn.xzj.agent.constants.Keys;
import cn.xzj.agent.util.CommonUtils;
import cn.xzj.agent.util.LogLevel;
import cn.xzj.agent.util.WebSocketManager;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/24
 * @Des
 */
public class PhoneListenerService extends Service {
    private static final String TAG = "PhoneListenService";
    private MyPhoneStateListener mMyPhoneStateListener;
    private TelephonyManager mTelephonyManager;
    private WebSocketManager mWebSocketManager;
    private Timer mTimer;
    private TimerTask mTimerTask;
    public int call_phone_status = 0;
    private final int CALL_PHONE_NORMAL = 0;//闲置
    private final int CALL_PHONE_ING = 2;//拨打中
    private String mToken;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogLevel.w(TAG, "onBind()");
        mToken = intent.getStringExtra(Keys.TOKEN);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mMyPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //修复进程间通信数据不一致问题由token Activity传进来
        mWebSocketManager = new WebSocketManager(this, mToken);
        return new PhoneListenServiceBinder();
    }

    public class PhoneListenServiceBinder extends Binder {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "onCreate()");
    }

    public class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    LogLevel.i(TAG, "响铃:" + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    call_phone_status = CALL_PHONE_ING;
                    LogLevel.i(TAG, "通话中");
                    if (mWebSocketManager != null) {
                        if (mWebSocketManager.mWebSocket == null) {
                            mWebSocketManager.run();
                        } else {
                            mWebSocketManager.sendPhoneStateMessage(call_phone_status);//通话中状态
                            setTimer();
                            mTimer.schedule(mTimerTask, 30 * 1000);
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    call_phone_status = CALL_PHONE_NORMAL;
                    if (mWebSocketManager != null) {
                        if (mWebSocketManager.mWebSocket == null) {
                            mWebSocketManager.run();
                        } else {
                            clearTimer();
                            mWebSocketManager.sendPhoneStateMessage(call_phone_status);//空闲状态
                            CommonUtils.statistics(PhoneListenerService.this, Constants.STATISTICS_PCCLIENT_CALL_PHONE_KILL_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK);
                        }
                    }
                    LogLevel.i(TAG, "闲置中");
                    break;
            }
        }
    }

    private void setTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mWebSocketManager.sendPhoneStateMessage(CALL_PHONE_ING);
            }
        };
    }

    private void clearTimer() {
        if (mTimerTask != null)
            mTimerTask.cancel();
        if (mTimer != null)
            mTimer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogLevel.w("onDestroy", "我销毁了服务");
        if (mWebSocketManager != null)
            mWebSocketManager.onDestroy();
        if (mTelephonyManager != null && mMyPhoneStateListener != null) {
            mTelephonyManager.listen(mMyPhoneStateListener, MyPhoneStateListener.LISTEN_NONE);
        }
    }
}
