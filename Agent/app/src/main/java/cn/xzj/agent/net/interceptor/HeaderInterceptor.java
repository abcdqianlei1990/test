package cn.xzj.agent.net.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.channey.utils.SharedPreferencesUtils;
import com.channey.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.xzj.agent.BuildConfig;
import cn.xzj.agent.MyApplication;
import cn.xzj.agent.entity.DeviceInfo;
import cn.xzj.agent.net.Client;
import cn.xzj.agent.util.CommonUtils;
import cn.xzj.agent.util.NetworkUtil;
import cn.xzj.agent.util.SharedPreferencesUtil;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class HeaderInterceptor implements Interceptor {
    private Context context;
    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        Response response;
        String path = request.url().encodedPath();
        long timeMillis = System.currentTimeMillis();
        addCommonHeader(builder, path);

        builder.addHeader(HeaderName.SIGN, md5(request, builder, timeMillis));

        request = builder.build();
        response = chain.proceed(request);
        Headers headers = request.headers();
        Set<String> names = headers.names();
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String value = headers.get(name);
            sb.append(name + " => " + value + "\n");
        }
        return response;
    }

    /**
     * 添加公共请求头
     */
    private void addCommonHeader(Request.Builder builder, String path) {
        DeviceInfo mDeviceInfo = SharedPreferencesUtil.getDeviceInfo(MyApplication.application);
        if (mDeviceInfo == null) {
            mDeviceInfo = new DeviceInfo();
        }
        builder.addHeader(HeaderName.CLIENT_TYPE, "app");
        builder.addHeader(HeaderName.VERSION_NAME, BuildConfig.VERSION_NAME);
        builder.addHeader(HeaderName.VERSION_CODE, String.valueOf(BuildConfig.VERSION_CODE));
        builder.addHeader(HeaderName.CHANNEL, "android");
        builder.addHeader(HeaderName.Xsource, CommonUtils.getChannel(context));
        builder.addHeader(HeaderName.XFROM, "2");
        builder.addHeader(HeaderName.SOURCE, CommonUtils.getChannel(context));
        builder.addHeader(HeaderName.DEVICE_OS_TYPE, mDeviceInfo.getOsType());
        builder.addHeader(HeaderName.NETWORK_TYPE, NetworkUtil.GetNetworkType(MyApplication.application));
        builder.addHeader(HeaderName.MAC_ADDRESS, NetworkUtil.getMacAddress());
        builder.addHeader(HeaderName.DEVICE_MODEL, mDeviceInfo.getDeviceModel());
        builder.addHeader(HeaderName.DEVICE_OS_VERSION, mDeviceInfo.getDeviceOSVersion());
        String deviceToken = SharedPreferencesUtils.INSTANCE.getString(context,"device_token");
        if (StringUtils.INSTANCE.isNotEmpty(deviceToken)){
            builder.addHeader(HeaderName.DEVICE_TOKEN, deviceToken);
        }
        String deviceId = mDeviceInfo.getId();
        if (!StringUtils.INSTANCE.isEmpty(mDeviceInfo.getId())) {
            builder.addHeader(HeaderName.DEVICE_ID, deviceId);
        } else {
            builder.addHeader(HeaderName.DEVICE_ID, NetworkUtil.getMacAddress());
        }

//        String[] strings = path.split("/");
//        if("pvt".equals(strings[2]) && !TextUtils.isEmpty(GlobalVariable.TOKEN)){
//            builder.addHeader(HeaderName.AUTHORIZATION, GlobalVariable.TOKEN);
//        }
        String token = SharedPreferencesUtil.getToken(MyApplication.application);
        if (!StringUtils.INSTANCE.isEmpty(token)) {
            builder.addHeader(HeaderName.AUTHORIZATION, token);
        }

//        //经度
//        String longitude = SampleApplicationLike.getSampleApplicationLike().longitude;
//        if(!StringUtils.INSTANCE.isEmpty(longitude)){
//            builder.addHeader(HeaderName.LONGITUDE, StringUtils.INSTANCE.doubleFormat(Double.parseDouble(longitude),5));
//        }
//
//        //纬度
//        String latitude = SampleApplicationLike.getSampleApplicationLike().latitude;
//        if(!StringUtils.INSTANCE.isEmpty(latitude)){
//            builder.addHeader(HeaderName.LATITUDE, StringUtils.INSTANCE.doubleFormat(Double.parseDouble(latitude),5));
//        }
    }

    private synchronized String md5(Request request, Request.Builder b, long timeMillis) {
        String method = request.method();
        String path = request.url().encodedPath();
        switch (method) {
            case "PATCH":
            case "POST": {
                ArrayList<String> names = new ArrayList<>();
                RequestBody requestBody = request.body();
                if (requestBody instanceof FormBody) {
                    FormBody body = (FormBody) requestBody;
                    for (int i = 0; i < body.size(); i++) {
                        StringBuilder sb = new StringBuilder();
                        String encodedValue = body.encodedValue(i);
                        String valueDecoded = StringUtils.INSTANCE.getValueDecoded(encodedValue);
                        if (!TextUtils.isEmpty(valueDecoded)) {
                            sb.append(body.encodedName(i));
                            sb.append("=");
                            sb.append(valueDecoded);
                            names.add(sb.toString());
                        }
                    }
                } else {
                    Buffer buffer = new Buffer();
                    try {
                        requestBody.writeTo(buffer);
                        buffer.flush();
                        String utf8 = buffer.readUtf8();
                        if (!TextUtils.isEmpty(utf8)) {
                            names.add("body" + "=" + utf8);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return packageParams(names, b, timeMillis, path);
            }
            case "GET": {
                ArrayList<String> names = new ArrayList<>();
                HttpUrl url = request.url();
                int querySize = url.querySize();
                for (int i = 0; i < querySize; i++) {
                    StringBuilder sb = new StringBuilder();
                    String value = url.queryParameterValue(i);
                    String valueDecoded = StringUtils.INSTANCE.getValueDecoded(value);
                    if (!TextUtils.isEmpty(valueDecoded)) {
                        String queryParameterName = url.queryParameterName(i);
                        sb.append(queryParameterName);
                        sb.append("=");
                        sb.append(valueDecoded);
                        names.add(sb.toString());
                    }
                }
                //有占位符的path
                Headers headers = request.headers();
                int size = headers.size();
                String templateHeader = "";
                for (int i = 0; i < size; i++) {
                    String name = headers.name(i);
                    if ("template".equals(name)) {
                        templateHeader = headers.value(i);
                    } else {
                        continue;
                    }
                }
                if (!StringUtils.INSTANCE.isEmpty(templateHeader)) {
                    getPathPlaceHolder(url, templateHeader, names);
                }
//                SignOriginalBean bean = new SignOriginalBean();

                String md5 = packageParams(names, b, timeMillis, path);
//                Log.d("qian","current thread wechatId => "+Thread.currentThread().getWechatId()+",path=>"+path+",md5=>"+md5);
//                bean.setOriginalData(md5);
//                originalBeanMap.put(path,bean);
                return md5;
            }
            default:
                return "";
        }
//        return md5;
    }

    /**
     * 获取path中占位字段
     */
    private void getPathPlaceHolder(HttpUrl url, String template, ArrayList<String> names) {
        List<String> list = url.encodedPathSegments();
        String[] strings = template.split("/");
        for (int i = 0; i < list.size(); i++) {
            String pathSegmentStr = list.get(i);
            String templateStr = strings[i];
            if (!pathSegmentStr.equals(templateStr)) {
                names.add(templateStr + "=" + pathSegmentStr);
            }
        }
    }

    /**
     * 将参数排序后转为字符串
     */
    private synchronized String packageParams(ArrayList<String> names, Request.Builder b, long timeMillis, String path) {
//        names.add(HeaderName.MERCHANT_NO+"="+Configuration.params.getMerchantNo());
//        names.add(HeaderName.API_KEY+"="+Configuration.params.getApiKey());
//        names.add(HeaderName.TIMESTAMP+"="+timeMillis);
//        String randomString = StringUtils.INSTANCE.getRandomString(32);
//        names.add(HeaderName.NONCE+"="+randomString);
//        b.addHeader(HeaderName.NONCE,randomString);
//        b.addHeader(HeaderName.TIMESTAMP,String.valueOf(timeMillis));
//        Collections.sort(names);
//        StringBuilder sb = new StringBuilder();
//        int size = names.size();
//        for (int i = 0; i < size; i++){
//            sb.append(names.get(i));
//            if((size-1) != i){
//                sb.append("&");
//            }
//        }
////        Log.d("qian","签名原始数据 = "+sb.toString());
//        String md5 = StringUtil.stringToMD5(sb.toString());
////        Log.d("qian","md5 = "+md5);
        return "";
    }

    /**
     * name of the headers
     */
    public interface HeaderName {
        String CHANNEL = "x-channel";
        String LONGITUDE = "longitude";
        String LATITUDE = "latitude";
        String BROWSER = "browser";
        String DEVICE_ID = "device-wechatId";
        String DEVICE_MODEL = "device-model";
        String DEVICE_OS_VERSION = "device-os-version";
        String PROVINCE = "province";
        String CITY = "city";
        String ADDRESS = "address";
        String DEVICE_OS_TYPE = "device-os-type";
        String NETWORK_TYPE = "network-type";
        String MAC_ADDRESS = "mac-address";
        String AUTHORIZATION = "Authorization";
        String CONTENT_TYPE = "Content-Type";
        String TIMESTAMP = "timestamp";
        String NONCE = "nonce";
        String SIGN = "sign";
        String API_KEY = "api_key";
        String SOURCE = "source";
        String Xsource = "x-source";
        String DEVICE_TOKEN = "device-token";
        String VERSION_NAME = "version-name";
        String VERSION_CODE = "version-code";
        String CLIENT_TYPE = "x-client-type";
        String APP_VERSION = "app-version";
        String APP_NAME = "app-name";
        String XFROM = "x-from";//来源: 1 - WAP网页, 2 - 安卓, 3 - IOS
    }
}
