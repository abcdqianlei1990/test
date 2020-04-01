package cn.xzj.agent.util;

import com.alibaba.fastjson.JSON;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.xzj.agent.BuildConfig;
import cn.xzj.agent.MyApplication;
import cn.xzj.agent.constants.Constants;
import cn.xzj.agent.constants.EnumValue;
import cn.xzj.agent.entity.websocket.BaseSocketEntity;
import cn.xzj.agent.entity.websocket.BaseSocketResponseEntity;
import cn.xzj.agent.entity.websocket.CallStatus;
import cn.xzj.agent.net.Client;
import cn.xzj.agent.service.PhoneListenerService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/21
 * @Des
 */
public class WebSocketManager {
    private static String TAG = WebSocketManager.class.getSimpleName();
    public WebSocket mWebSocket;
    public String id;
    public PhoneListenerService mContext;
    private OkHttpClient okHttpClient;
    private Request mRequest;
    private MyWebSocketListener mMyWebSocketListener;
    private boolean isInvalidToken = false;
    private String mToken;
    private int invalidTokenRetryConnectionNum = 0;//token失效连接断开次数
    private static final int MAX_INVALID_TOKEN_CONNECTION_NUM = 10;//token失效连接最大次数

    public WebSocketManager(PhoneListenerService context, String token) {
        mContext = context;
        id = CommonUtils.buildUUID();
        this.mToken = token;
        init();
    }

    private void init() {
        int environment = SharedPreferencesUtil.getEnvironment(MyApplication.application);
        if (environment < 0) {
            //如果没有设置缓存使用默认配置
            environment = BuildConfig.ENVIRONMENT;
        }
        String url;
        switch (environment) {
            case Client.ENV_DEV:
                url = "ws://" + Client.BASE_WEBSOCKET_URL + "/websocket";
                break;
            case Client.ENV_TEST:
                url = "ws://" + Client.BASE_WEBSOCKET_URL + "/websocket";
                break;
            case Client.ENV_PRO:
                url = "wss://" + Client.BASE_WEBSOCKET_URL + "/websocket";
                break;
            default:
                url = "wss://" + Client.BASE_WEBSOCKET_URL + "/websocket";
        }
        mRequest = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.retryOnConnectionFailure(true)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogLevel.w("httpLoggingInterceptor", message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        okHttpClient = builder.build();
        mMyWebSocketListener = new MyWebSocketListener();
    }

    public void run() {
        if (isInvalidToken && invalidTokenRetryConnectionNum > MAX_INVALID_TOKEN_CONNECTION_NUM) {
            LogLevel.w(TAG, "我已经超过了失效最大重试次数 " + MAX_INVALID_TOKEN_CONNECTION_NUM);
            LogLevel.w(TAG, "登录已失效，关闭socket连接");
        } else {
            if (okHttpClient != null) {
                LogLevel.w(TAG, "正在建立socket 连接");
                mWebSocket = okHttpClient.newWebSocket(mRequest, mMyWebSocketListener);

            } else {
                LogLevel.w(TAG, "我已经被kill不可用 ");
            }
            if (isInvalidToken) {
                invalidTokenRetryConnectionNum++;
            } else {
                invalidTokenRetryConnectionNum = 0;
            }
        }

    }

    public void init1() {
        mRequest = new Request.Builder()
                .url("ws://172.16.19.69:8080/webSocketServer")
                .build();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.retryOnConnectionFailure(true)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        okHttpClient = builder.build();
        mWebSocket = okHttpClient.newWebSocket(mRequest, new MyWebSocketListener());
    }

    public void sendPhoneStateMessage(int status) {
        if (mWebSocket == null)
            return;
        BaseSocketEntity<CallStatus> mSocketCallStatus = new BaseSocketEntity<>();
        mSocketCallStatus.setId(id);
        mSocketCallStatus.setPath("/app-dialing-exchanging");
        CallStatus mCallStatus = new CallStatus();
        mCallStatus.setStatus(status);
        mSocketCallStatus.setArguments(mCallStatus);
        String statusMessage = JSON.toJSONString(mSocketCallStatus);
        LogLevel.w(TAG, "sendMessage:" + statusMessage);
        mWebSocket.send(statusMessage);//发送通话状态消息
    }

    class MyWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            LogLevel.w(TAG, "onOpen");
            //***发送授权信息start****//
            BaseSocketEntity<String> mAuthorizationBody = new BaseSocketEntity<>();
            mAuthorizationBody.setId(id);
            mAuthorizationBody.setPath("/authorize");
            mAuthorizationBody.setArguments(mToken);
            String authorizationMessage = JSON.toJSONString(mAuthorizationBody);
            webSocket.send(authorizationMessage);
            LogLevel.w(TAG, "sendMessage:" + authorizationMessage);
            //***发送授权信息end****//

            //***发送手机状态信息start****//
            sendPhoneStateMessage(mContext.call_phone_status);
            //***发送手机状态信息end****//
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            LogLevel.w(TAG, "onMessage " + text);
            try {
                BaseSocketResponseEntity mBaseSocketEntity = JSON.parseObject(text, BaseSocketResponseEntity.class);
                if (mBaseSocketEntity.getResult() != null) {
                    if (mBaseSocketEntity.getResult().getContent() != null) {
                        String phone = mBaseSocketEntity.getResult().getContent();
                        CommonUtils.directCallPhone(mContext, phone);
                        CommonUtils.statistics(mContext, Constants.STATISTICS_PCCLIENT_CALL_PHONE_CALL_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK);
                    }
                    isInvalidToken = mBaseSocketEntity.getResult().isInvalidToken();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            LogLevel.w(TAG, "onMessage bytes");
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            LogLevel.e(TAG, "onClosing" + " code:" + code + " reason:" + reason);
            boolean isClose = mWebSocket.close(1000, null);
            if (isClose) {
                LogLevel.i(TAG, "onClosing：主动断线成功");
            }

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            LogLevel.e(TAG, "onClosed" + " code:" + code + " reason:" + reason);
            run();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            LogLevel.e(TAG, "onFailure" + "t:" + t.getMessage());
            run();
        }
    }

    public void onDestroy() {
        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
        okHttpClient = null;
        mMyWebSocketListener = null;
        mWebSocket = null;
    }
}
