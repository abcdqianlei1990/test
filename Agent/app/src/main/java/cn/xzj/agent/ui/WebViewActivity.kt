package cn.xzj.agent.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.net.Uri
import android.net.http.SslCertificate
import android.net.http.SslError
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import cn.xzj.agent.MyApplication
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.common.ShareInfo
import cn.xzj.agent.iview.IWebView
import cn.xzj.agent.presenter.WebViewPresenter
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.util.WebViewUtil
import cn.xzj.agent.widget.MyWebViewClient
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import com.google.gson.Gson


/**
 * Created by channey on 2016/12/5.
 * version:1.0
 * desc:
 */

class WebViewActivity : MVPBaseActivity<WebViewPresenter>(), MyWebViewClient.WebCall,
    MyWebViewClient.ProgressChangeListener, IWebView, View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun context(): Context {
        return this
    }


    private var mWebView: WebView? = null
    private var mProgressBar: ProgressBar? = null

    private var mUrl: String? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    var mUploadMessageForAndroid5: ValueCallback<Array<Uri>>? = null
    private val mDialog: Dialog? = null
    private var mShowTitle = false
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_GOBACK -> goBack()
                MSG_GOTO_LOGIN -> LoginActivity.jump(this@WebViewActivity)
            }
        }
    }
    var mBackAction = BACK_ACTION_INDEX
    private var mNumber: String? = null

    private val webViewClient = object : WebViewClient() {

        override fun shouldInterceptRequest(
            view: WebView,
            url: String
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(view, url)
        }


        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            //                Log.d("qian","onPageFinished url => "+url);
            mUrl = url
            var title = view.title
            if (!TextUtils.isEmpty(title)) {
                if (title.length > 11) {
                    val substring = title.substring(0, 11)
                    title = "$substring..."
                }
            } else {
                title = ""
            }
            setTitle(title)
        }

        override fun onPageStarted(webView: WebView, url: String, bitmap: Bitmap?) {
            //                Log.d("qian","onPageStarted url => "+url);
        }

        override fun onReceivedSslError(webView: WebView, sslErrorHandler: SslErrorHandler, sslError: SslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError)
            val builder = AlertDialog.Builder(this@WebViewActivity)
            val sslCertificate = sslError.certificate
            val primaryError = sslError.primaryError
            val msg = ""
            builder.setTitle("SSL证书错误")
            builder.setMessage("SSL错误码：$primaryError,$msg")
            builder.setPositiveButton("继续") { dialog, which -> sslErrorHandler.proceed() }
            builder.setNegativeButton("取消") { dialog, which -> sslErrorHandler.cancel() }
            builder.create().show()
        }

        override fun onReceivedError(
            webView: WebView,
            webResourceRequest: WebResourceRequest,
            webResourceError: WebResourceError
        ) {
            super.onReceivedError(webView, webResourceRequest, webResourceError)
            //                loadingError(webResourceError.toString());
        }

        override fun shouldOverrideUrlLoading(webView: WebView, s: String): Boolean {
            //                Log.d("qian","shouldOverrideUrlLoading url => "+s);
            return false
        }
    }

    override fun initParams() {
        mUrl = intent.getStringExtra(key_url)
        mBackAction = intent.getStringExtra(key_back_action)
        if (StringUtils.isEmpty(mBackAction)) mBackAction = BACK_ACTION_FINISH
        window.setFormat(PixelFormat.TRANSLUCENT)
        mShowTitle = intent.getBooleanExtra("show_title", false)
    }

    override fun initViews() {
        mWebView = findViewById<View>(R.id.activity_webview) as WebView
        mProgressBar = findViewById<View>(R.id.activity_webview_progressBar) as ProgressBar
        //adjustPan
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //adjustResize
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initWebView()
        if (!TextUtils.isEmpty(mUrl)) {
            val url: String
            if (mUrl!!.contains("?")) {
                url = mUrl!! + "&noTab=true"   //通知h5不显示h5自带的标题
            } else {
                url = mUrl!! + "?noTab=true"
            }
            mWebView!!.loadUrl(url)
        }
    }

    public override fun initData() {

    }

    //    public void loadJsFunction(){
    //        String url = "javascript:JSInterfaceInstance.setToken("+1123+");";
    //        mWebView.loadUrl(url);
    //    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        if (newProgress == 100) {
            mProgressBar!!.visibility = View.GONE
        } else {
            mProgressBar!!.visibility = View.VISIBLE
            mProgressBar!!.progress = newProgress
        }
    }

    override fun onClick(v: View) {

    }

    fun onAppShareInfoGetSuccess(shareInfo: ShareInfo) {
        shareApp(shareInfo)
    }

    fun onAppShareInfoGetFailure(msg: String) {
        showToast(msg)
    }

    private fun shareApp(shareInfo: ShareInfo) {
        //        showSharePopWindow(new ShareMenuListAdapter.ShareMenuItemClickListener(){
        //            @Override
        //            public void onClick(View view, int position, String platform) {
        //                UIUtil.performClickAnimator(view);
        //                shareInfo.setTargetPlatform(platform);
        //                ShareUtil.INSTANCE.share(WebViewActivity.this,shareInfo, new ShareUtil.ShareSuccessListener() {
        //                    @Override
        //                    public void onShareSuccess(String shareType) {
        //                        dismissShareMenuDialog();
        //                    }
        //                });
        //            }
        //
        //        });
    }

    private inner class JSInterface {

        /**
         * 获取token
         */
        val token: String
            @JavascriptInterface
            get() = SharedPreferencesUtil.getToken(this@WebViewActivity)

//        /**
//         * 获取渠道，如baidu，应用宝等
//         * @return
//         */
//        val source: String?
//            @JavascriptInterface
//            get() = app.source
//
//        /**
//         * 获取app运行环境，如android或者ios
//         * @return
//         */
//        val channel: String
//            @JavascriptInterface
//            get() = app.channel

        /**
         * 返回
         */
        @JavascriptInterface
        fun goBack() {
            mHandler.sendEmptyMessage(MSG_GOBACK)
        }

        @JavascriptInterface
        fun login() {
            //            Log.d("qian","login()........");
            mHandler.sendEmptyMessage(MSG_GOTO_LOGIN)
        }

        /**
         * 实名认证
         */
        @JavascriptInterface
        fun goCertification() {
            mHandler.sendEmptyMessage(MSG_GOTO_REALNAME)
        }

        /**
         * 分享
         * @param json like:{"type":"packet","pageId":"task-share"}
         */
        @JavascriptInterface
        fun shareWithParam(json: String) {
            if (hasLogin()) {
                getShareInfo(json, SHARE_ACTION_TYPE_1)
            } else {
                login()
            }
        }

        /**
         * 分享
         * @param json like:{"type":"packet","pageId":"task-share"}
         */
        @JavascriptInterface
        fun shareTargetPlatformWithParam(json: String) {
            if (hasLogin()) {
                getShareInfo(json, SHARE_ACTION_TYPE_2)
            } else {
                login()
            }
        }

        /**
         * 全平台分享
         * @param json 分享需要的参数json字符串
         */
        @JavascriptInterface
        fun shareSupportSmallProgramWithParam(json: String) {
            val gson = Gson()
            val shareInfo = gson.fromJson(json, ShareInfo::class.java)
            val targetPlatform = shareInfo.targetPlatform
            if (StringUtils.isEmpty(targetPlatform)) {
                //让用户选择分享平台
                showSharePopWindow(shareInfo)
            } else {
                //指定平台分享
                doShare(shareInfo)
            }
        }


        /**
         * 拨打电话
         * @param number
         */
        @JavascriptInterface
        fun call(number: String) {
            if (StringUtils.isNotEmpty(number)) {
                mNumber = number
//                makePhoneCall(number)
            }
        }
    }

    /**
     * 老接口使用。前端传个别参数，然后其他参数通过接口获取
     */
    private fun getShareInfo(json: String, actionType: Int) {
        val gson = Gson()
        val shareInfo = gson.fromJson(json, ShareInfo::class.java)
        //        mPresenter.getShareInfo(shareInfo,actionType);
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView!!.canGoBack()) {
                mWebView!!.goBack()
                return true
            } else {
                goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun goBack() {
        when (mBackAction) {
            BACK_ACTION_FINISH -> finish()
            BACK_ACTION_INDEX -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initWebView() {
        val myWebViewClient = MyWebViewClient()
        myWebViewClient.setWebCall(this)
        myWebViewClient.setProgressChangeListener(this)
        mWebView!!.webChromeClient = myWebViewClient
        mWebView!!.webViewClient = webViewClient
        WebViewUtil.commonSettingInit(application as MyApplication, mWebView!!)
        mWebView!!.addJavascriptInterface(JSInterface(), "JSInterfaceInstance")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            FILECHOOSER_RESULTCODE -> {
                if (null == mUploadMessage)
                    return
                val result = if (intent == null || resultCode != Activity.RESULT_OK)
                    null
                else
                    intent.data
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
            FILECHOOSER_RESULTCODE_FOR_ANDROID_5 -> {
                if (null == mUploadMessageForAndroid5)
                    return
                val result2 = if (intent == null || resultCode != Activity.RESULT_OK)
                    null
                else
                    intent.data
                if (result2 != null) {
                    mUploadMessageForAndroid5!!.onReceiveValue(arrayOf(result2))
                } else {
                    mUploadMessageForAndroid5!!.onReceiveValue(arrayOf())
                }
                mUploadMessageForAndroid5 = null
            }
            Code.RequestCode.LOGIN -> if (hasLogin()) mWebView!!.reload() else showToast("请先登录")

        }
    }

    override fun fileChose(uploadMsg: ValueCallback<Uri>) {
        openFileChooserImpl(uploadMsg)
    }

    override fun fileChose5(uploadMsg: ValueCallback<Array<Uri>>) {
        openFileChooserImplForAndroid5(uploadMsg)
    }

    private fun openFileChooserImpl(uploadMsg: ValueCallback<Uri>) {
        mUploadMessage = uploadMsg
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(
            Intent.createChooser(i, "File Chooser"),
            FILECHOOSER_RESULTCODE
        )
    }

    private fun openFileChooserImplForAndroid5(uploadMsg: ValueCallback<Array<Uri>>) {
        mUploadMessageForAndroid5 = uploadMsg
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"

        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")

        startActivityForResult(
            chooserIntent,
            FILECHOOSER_RESULTCODE_FOR_ANDROID_5
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.CALL_PHONE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    makePhoneCall(mNumber)
                } else {
                    showToast("已禁用拨打电话权限")
                }
            }
        }
    }

    fun onShareInfoGetSuccess(shareInfo: ShareInfo, actionType: Int) {
        when (actionType) {
            SHARE_ACTION_TYPE_1 -> showSharePopWindow(shareInfo)
            SHARE_ACTION_TYPE_2 -> doShare(shareInfo)
        }
    }

    private fun showSharePopWindow(shareInfo: ShareInfo) {
        //        showSharePopWindow(new ShareMenuListAdapter.ShareMenuItemClickListener(){
        //            @Override
        //            public void onClick(View view,int position, String platform) {
        //                shareInfo.setTargetPlatform(platform);
        //                doShare(shareInfo);
        //            }
        //
        //        });
    }

    /**
     * 直接分享到某平台
     * @param shareInfo
     */
    private fun doShare(shareInfo: ShareInfo) {
        //        ShareUtil.INSTANCE.share(this,shareInfo, new ShareUtil.ShareSuccessListener() {
        //            @Override
        //            public void onShareSuccess(String shareType) {
        //                mPresenter.track(WebViewActivity.this
        //                        , TrackUtil.INSTANCE.track(TrackUtil.INSTANCE.getTYPE_PAGE()
        //                                ,TrackUtil.INSTANCE.getMETRIC_SHARE()
        //                                , shareInfo.getPageId()+":"+shareType));
        //                dismissShareMenuDialog();
        //            }
        //        });
    }

    fun onShareInfoGetFailure(msg: String) {
        showToast(msg)
    }

    companion object {
        val REQUEST_SELECT_FILE = 100
        //    private final static int FILECHOOSER_RESULTCODE = 2;

        val FILECHOOSER_RESULTCODE = 1
        val FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2
        private val MSG_GOBACK = 0
        private val MSG_GOTO_LOGIN = 1
        private val MSG_GOTO_REALNAME = 2
        private val SHARE_ACTION_TYPE_1 = 1
        private val SHARE_ACTION_TYPE_2 = 2
        val BACK_ACTION_INDEX = "back_action_index" //点击返回时返回到首页
        val BACK_ACTION_FINISH = "back_action_finish" //点击返回时finish()
        val PARAM_KEY_SHOW_SHARE = "show_share_btn"
        val key_url = "url"
        val key_back_action = "back_action"

        fun jump(activity: Activity, url: String, backAction:String = BACK_ACTION_FINISH,requestCode: Int = Code.RequestCode.WebViewActivity) {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(key_url, url)
            intent.putExtra(key_back_action, backAction)
            activity.startActivityForResult(intent, requestCode)
        }
    }
}
