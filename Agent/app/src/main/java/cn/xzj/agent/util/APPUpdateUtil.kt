package cn.xzj.agent.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import cn.xzj.agent.BuildConfig
import cn.xzj.agent.constants.Config
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.entity.AppVersionInfo
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.common.PgyAppInfo
import cn.xzj.agent.entity.common.PgyBaseResponseInfo
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.widget.CommonDialog
import com.channey.utils.SharedPreferencesUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.content.Intent
import android.net.Uri



/**
 *
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/23
 * @ Des
 */
class APPUpdateUtil {
    companion object {
        fun checkUpdate(context: Context) {
            when(BuildConfig.ENVIRONMENT){
                Client.ENV_DEV,Client.ENV_TEST -> if (!BuildConfig.DEBUG) getAPPInfoFromPGY(context)
                Client.ENV_PRO -> checkUpdatePro(context)
                else -> checkUpdatePro(context)
            }
        }

        private fun checkUpdatePro(context: Context) {
            try {
                val apiManager = Client.getInstance(context).apiManager
                val observable = apiManager.getAppVersionInfo()
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : QuickObserver<AppVersionInfo>(context) {
                            override fun onSuccessful(t: BaseResponseInfo<AppVersionInfo>?) {
                                if (!t!!.isSuccess())
                                    return
                                if (BuildConfig.VERSION_CODE < t.content.androiD_VERSION.toInt()) {
                                    //版本不是最新版本需要更新
                                    showUpdateDialog(context, versionInfo = t.content)
                                }else{
//                                    val mAppVersionInfo=AppVersionInfo()
//                                    mAppVersionInfo.androiD_FORCE_UPDATE="1"
//                                    mAppVersionInfo.androiD_UPDATE_PROMPT="测试更新"
//                                    mAppVersionInfo.androiD_APP_URL="http://openbox.mobilem.360.cn/index/d/sid/3205661"
//                                    showUpdateDialog(context, versionInfo = mAppVersionInfo)
                                }
                            }

                            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                            }
                        })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 使用短链接获取App信息
         */
        private fun getAPPInfoFromPGY(context: Context) {
            val apiManager = Client.getInstance(context).apiManager
            val observable = apiManager.getAPPInfoFromPGY("xzjagent", Config.PGY_apiKey)
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer2<PgyBaseResponseInfo<PgyAppInfo>>() {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(info: PgyBaseResponseInfo<PgyAppInfo>) {
                            var lastPgyBuildVer = SharedPreferencesUtils.getLong(context,Keys.appBuildVersioin)
                            if (lastPgyBuildVer <= 0L)  {
                                SharedPreferencesUtils.saveLong(context,Keys.appBuildVersioin,info.data.appBuildVersion!!.toLong())
                                lastPgyBuildVer = info.data.appBuildVersion!!.toLong()
                            }
//                            ToastUtils.showToast(context,"当前appBuildVersion:$lastPgyBuildVer,最新appBuildVersion：${info.data.appBuildVersion}")
                            if (lastPgyBuildVer > 0 && lastPgyBuildVer < info.data.appBuildVersion!!){
                                DialogUtil.showNoticeDialogWithCancelConfirm(context,title = "测试版本有更新了",content = "当前$lastPgyBuildVer,最新${info.data.appBuildVersion},${info.data.appUpdateDescription}",confirmListener = View.OnClickListener {
                                    DialogUtil.dismissCancelConfirmDialog()
                                    SharedPreferencesUtils.saveLong(context,Keys.appBuildVersioin,info.data.appBuildVersion!!.toLong())
                                    pgyInstallApp(context,appKey = info.data.appKey!!)
                                })
                            }
                        }

                        override fun onError(e: Throwable) {

                        }
                    })
        }

        private fun pgyInstallApp(context: Context,appKey:String,installPwd:String?=null) {
            try {
                var url = "http://www.pgyer.com/apiv1/app/install?_api_key=${Config.PGY_apiKey}&aKey=$appKey"
                if (installPwd != null) url = "$url&password=$installPwd"
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun showUpdateDialog(context: Context, versionInfo: AppVersionInfo) {

            if (!versionInfo.isForced) {
                CommonDialog.newBuilder(context)
                        .setTitle("版本更新")
                        .setContentGravity(Gravity.LEFT)
                        .setMessage(versionInfo.androiD_UPDATE_PROMPT)
                        .setPositiveButton("立即更新") { dialog: CommonDialog? ->
                            dialog!!.dismiss()
                            downApkAndInstall(context, versionInfo)
                        }
                        .setNegativeButton("下次再说") {
                        }
                        .create()
                        .show()
            } else {
                CommonDialog.newBuilder(context)
                        .setCancelable(false)
                        .setContentGravity(Gravity.LEFT)
                        .setTitle("版本更新")
                        .setMessage(versionInfo.androiD_UPDATE_PROMPT)
                        .setPositiveButton("立即更新") { dialog ->
                            dialog.dismiss()
                            downApkAndInstall(context, versionInfo)
                        }
                        .create()
                        .show()


            }

        }

        @SuppressLint("CheckResult")
        private fun downApkAndInstall(context: Context, versionInfo: AppVersionInfo) {
            RxPermissions(context as Activity)
                    .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe {
                        when (it.name) {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                                if (it.granted) {
                                    try {
                                        val mDownloadManagerUtil = DownloadManagerUtil(context, versionInfo.androiD_APP_URL)
                                        mDownloadManagerUtil.download(versionInfo.androiD_APP_URL, "agent_update.apk")
                                    } catch (e: Exception) {
                                        CommonUtils.browserAgentUpdate(context, versionInfo.androiD_APP_URL)
                                    }

                                } else {
                                }
                            }
                        }
                    }
        }

    }
}