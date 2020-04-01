package cn.xzj.agent.net.interceptor;

import android.text.TextUtils;

import java.io.IOException;

import cn.xzj.agent.net.HttpConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/29
 * @ Des
 */
public class RewriteResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse;
        originalResponse = chain.proceed(request);
        Response response = null;
        String cacheControl = originalResponse.header("Cache-Control");
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            Response.Builder newBuilder = originalResponse.newBuilder();
            String isCache = request.header("isCache");
            if (!TextUtils.isEmpty(isCache) && HttpConfig.IS_CACHE.equals(isCache)) {
                newBuilder.header("Cache-Control", "public, max-age=" + HttpConfig.CACHE_MAX_AGE)
                        .removeHeader("Pragma");
            }
            Response res = newBuilder.build();
            response = res;
        } else {
            response = originalResponse;
        }
        return response;
    }
}
