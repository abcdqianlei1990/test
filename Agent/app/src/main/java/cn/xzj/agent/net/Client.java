package cn.xzj.agent.net;


import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.xzj.agent.BuildConfig;
import cn.xzj.agent.MyApplication;
import cn.xzj.agent.net.interceptor.HeaderInterceptor;
import cn.xzj.agent.net.interceptor.RewriteResponseInterceptor;
import cn.xzj.agent.util.LogLevel;
import cn.xzj.agent.util.SharedPreferencesUtil;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by channey on 2016/10/20.
 * version:1.0
 * desc: 获取全局的http client调用api
 */

public class Client {
    private static Client INSTANCE = null;
    // API
    public static String BASE_URL = "https://219.233.221.90:8074/api/";
    //webSocket
    private static String BASE_WEBSOCKET_URL_TEST = "10.100.15.17:9090"; //测试
    private static String BASE_WEBSOCKET_URL_DEV = "10.100.15.17:9009";   //dev
    private static String BASE_WEBSOCKET_URL_PRODUCT = "h5crm.xiaozhijie.com";   //生产环境

    //上传文件，红包分享接口
    private static String BASE_URL_TEST_UPLOAD_FILE = "http://10.100.10.98:8080/"; //测试
    private static String BASE_URL_DEV_UPLOAD_FILE = "http://10.100.10.98:8081/";   //dev
    private static String BASE_URL_PRODUCT_UPLOAD_FILE = "https://mapi.xiaozhijie.com/";   //生产环境

    //api host (AUTH)
    private static String BASE_URL_AUTH = "https://219.233.221.90:8074/";

    //h5 resource
    private static String SAPI_BASE_URL_TEST = "http://h5-test.lxworker.com/"; //测试
    private static String SAPI_BASE_URL_DEV = "http://h5-dev.lxworker.com/";   //dev
    private static String SAPI_BASE_URL_PRODUCT = "http://h5.xiaozhijie.com/";   //生产环境
    private static String STATIC_RES_URL = "https://assets.xiaozhijie.com/xzj/";   //静态资源host

    //埋点域名
    private static String TRACK_URL_DEV = "http://analytics.dev.bnrong.com/";
    private static String TRACK_URL_TEST = "http://analytics.test.bnrong.com/";
    private static String TRACK_URL_PRO = "https://analytics.xiaozhijie.com/";
    //机器人聊天
    private static String BASE_CHATBOT_DEV = "http://10.50.15.146:9001/";//开发
    private static String BASE_CHATBOT_TEST = "http://10.50.15.146:9001/";//测试

    private static String BASE_URL_UPLOAD_FILE;//上传文件
    public static String H5_RES_HOST;   //h5资源地址
    private static String H5_RES_HOST_NEW = STATIC_RES_URL;   //h5资源地址
    private static String TRACK_HOST;//埋点
    public static String BASE_WEBSOCKET_URL;//打电话webSocket地址

    private ApiManager apiManager;
    private ApiManager authApiManager;
    private ApiManager apiManagerStatic;    //use for static res
    private ApiManager apiManagerStaticNew;    //use for static res
    private ApiManager apiManagerUploadFile;
    private ApiManager apiStatisticsManager;
    private OkHttpClient mOkHttpClient;

    public static final int ENV_DEV = 0;
    public static final int ENV_TEST = 1;
    public static final int ENV_PRO = 2;

    //https域名
    private static ArrayList<String> mTrustHostList = new ArrayList<String>();

    //    /**
//     * 接口是否需要加签
//     * 1-yes 0-no
//     */
//    private static final String NEED_SIGN = "1";
//    private boolean mNeedSign = false;
    public static Client getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Client.class) {
                if (INSTANCE == null) {
//                    mHeaders = headers;
                    INSTANCE = new Client(context);
                }
            }
        }
        return INSTANCE;
    }

    static {
        int environment = SharedPreferencesUtil.getEnvironment(MyApplication.application);
        if (environment < 0) {
            //如果没有设置缓存使用默认配置
            environment = BuildConfig.ENVIRONMENT;
        }
        LogLevel.w("environment", environment + "");
        switch (ENV_PRO) {
            case ENV_TEST:
                H5_RES_HOST = SAPI_BASE_URL_TEST;
                TRACK_HOST = TRACK_URL_TEST;
                BASE_URL_UPLOAD_FILE = BASE_URL_TEST_UPLOAD_FILE;
                BASE_WEBSOCKET_URL = BASE_WEBSOCKET_URL_TEST;
                break;
            case ENV_DEV:
                H5_RES_HOST = SAPI_BASE_URL_DEV;
                TRACK_HOST = TRACK_URL_DEV;
                BASE_URL_UPLOAD_FILE = BASE_URL_DEV_UPLOAD_FILE;
                BASE_WEBSOCKET_URL = BASE_WEBSOCKET_URL_DEV;
                break;
            case ENV_PRO:
                H5_RES_HOST = SAPI_BASE_URL_PRODUCT;
                TRACK_HOST = TRACK_URL_PRO;
                BASE_URL_UPLOAD_FILE = BASE_URL_PRODUCT_UPLOAD_FILE;
                BASE_WEBSOCKET_URL = BASE_WEBSOCKET_URL_PRODUCT;
                break;
            default:
                H5_RES_HOST = SAPI_BASE_URL_PRODUCT;
                TRACK_HOST = TRACK_URL_PRO;
                BASE_URL_UPLOAD_FILE = BASE_URL_PRODUCT_UPLOAD_FILE;
                BASE_WEBSOCKET_URL = BASE_WEBSOCKET_URL_PRODUCT;
                break;
        }
        mTrustHostList.add("mapi.xiaozhijie.com");
        mTrustHostList.add("h5.xiaozhijie.com");
        mTrustHostList.add("analytics.xiaozhijie.com");
        mTrustHostList.add("assets.xiaozhijie.com");
        mTrustHostList.add("h5crm.xiaozhijie.com");
        mTrustHostList.add("crm.xiaozhijie.com");
        mTrustHostList.add("aip.baidubce.com");//百度开放API域名
        mTrustHostList.add("api.megvii.com");//face
        mTrustHostList.add("219.233.221.90");
        mTrustHostList.add("219.233.221.90");
    }


    private Client(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        File cacheFile = new File(MyApplication.application.getCacheDir(), "dxdCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //10Mb
        builder.retryOnConnectionFailure(true)
                .connectTimeout(HttpConfig.TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(HttpConfig.TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HttpConfig.TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor(context))
                .addNetworkInterceptor(new RewriteResponseInterceptor());
        builder.cache(cache);
        builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory());
        builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return mTrustHostList.contains(hostname);
//            }
//        });
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
        mOkHttpClient = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NoBodyConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(BASE_URL_AUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NoBodyConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();

        Retrofit staticRetrofit = new Retrofit.Builder()
                .baseUrl(H5_RES_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NoBodyConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();

        Retrofit staticRetrofitNew = new Retrofit.Builder()
                .baseUrl(H5_RES_HOST_NEW)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NoBodyConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        Retrofit uploadFileRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_UPLOAD_FILE)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NoBodyConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        Retrofit statisticsRetrofit = new Retrofit.Builder()
                .baseUrl(TRACK_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NoBodyConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        apiManager = retrofit.create(ApiManager.class);
        authApiManager = retrofit2.create(ApiManager.class);
        apiManagerStatic = staticRetrofit.create(ApiManager.class);
        apiManagerStaticNew = staticRetrofitNew.create(ApiManager.class);
        apiManagerUploadFile = uploadFileRetrofit.create(ApiManager.class);
        apiStatisticsManager = statisticsRetrofit.create(ApiManager.class);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public ApiManager getApiManager() {
        return apiManager;
    }

    public ApiManager getAuthApiManager() {
        return authApiManager;
    }

    public ApiManager getStaticApiManager() {
        return apiManagerStatic;
    }

    public ApiManager getStaticApiManagerNew() {
        return apiManagerStaticNew;
    }

    public ApiManager getUploadFileApiManager() {
        return apiManagerUploadFile;
    }

    public ApiManager getApiStatisticsManager() {
        return apiStatisticsManager;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // 无法知道压缩后的数据大小
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
