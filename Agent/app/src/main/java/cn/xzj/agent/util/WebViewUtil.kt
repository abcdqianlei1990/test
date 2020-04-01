package cn.xzj.agent.util

import android.content.Context
import android.os.Build
import android.webkit.*
import cn.xzj.agent.MyApplication
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue

object WebViewUtil {
    fun commonSettingInit(context: MyApplication, webView:WebView){
        val webSetting = webView.settings
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(false)
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // make sure your pinch zoom is enabled
        webSetting.builtInZoomControls = true

// don't show the zoom controls
        webSetting.displayZoomControls = false
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true)
        webSetting.setJavaScriptEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webView.settings.setAppCacheMaxSize((1024 * 1024 * 8).toLong())
        webSetting.setAppCachePath(context.getDir("appcache", 0).path)
        webSetting.databasePath = context.getDir("databases", 0).path
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).path)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
//        mWebView.loadUrl(mUrl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        //http https混合请求
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }
}