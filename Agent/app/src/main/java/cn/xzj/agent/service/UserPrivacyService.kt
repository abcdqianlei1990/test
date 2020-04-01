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
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.privacy.CallLogInfo
import cn.xzj.agent.entity.privacy.CallLogUploadBody
import cn.xzj.agent.entity.privacy.SmsInfo
import cn.xzj.agent.entity.privacy.SmsUploadBody
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.util.PrivacyUtils
import cn.xzj.agent.util.SharedPreferencesUtil
import com.channey.utils.SharedPreferencesUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 上传用户隐私数据的服务,该服务不包含微信相关功能
 */
class UserPrivacyService : Service() {
    private val DATA_UPLOAD_PERIOD = 15 * 60 * 1000L
    private var agentInfo: AgentInfo? = null


    private var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
           Client.getInstance(this@UserPrivacyService).apiManager
                    .getLastUploadDate(agentInfo!!.agentId, "CALL_PHONE")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer2<BaseResponseInfo<Long>>() {

                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: BaseResponseInfo<Long>) {
                            PrivacyUtils.getUserCallLogs(this@UserPrivacyService, t!!.content) { list -> uploadCallLog(list) }
                        }


                        override fun onError(e: Throwable) {
                            PrivacyUtils.getUserCallLogs(this@UserPrivacyService, 0) { list -> uploadCallLog(list) }
                        }
                    })
           Client.getInstance(this@UserPrivacyService).apiManager
                    .getLastUploadDate(agentInfo!!.agentId, "SMS_RECORD")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer2<BaseResponseInfo<Long>>() {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: BaseResponseInfo<Long>) {
                            PrivacyUtils.getUserSms(this@UserPrivacyService, t!!.content) { list -> uploadSms(list) }
                        }


                        override fun onError(e: Throwable) {
                            PrivacyUtils.getUserSms(this@UserPrivacyService, 0) { list -> uploadSms(list) }
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

    override fun onCreate() {
        super.onCreate()
        agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(this)
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
        startForeground(10087, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "com.xzj.agent.UserPrivacyService"
        val channelName = "UserPrivacyService"
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


    private fun uploadCallLog(callLog: List<CallLogInfo>?) {
         Client.getInstance(this@UserPrivacyService).apiManager!!.uploadCallLog(agentInfo!!.agentId, CallLogUploadBody(callLog!!)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<BaseResponseInfo<Boolean>>() {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {}

                    override fun onNext(info: BaseResponseInfo<Boolean>) {
                        if (info.isSuccess()) setCallLogUploadedFlag()
                    }
                })
    }

    private fun uploadSms(smsList: List<SmsInfo>) {
        Client.getInstance(this@UserPrivacyService).apiManager!!.uploadSms(agentInfo!!.agentId, SmsUploadBody(smsList)).subscribeOn(Schedulers.io())
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

    private fun setCallLogUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(this, Keys.ALREADY_UPLOAD_CALL_LOG, true, Activity.MODE_MULTI_PROCESS)
    }

    private fun setSmsUploadedFlag() {
        SharedPreferencesUtils.saveBoolean(this, Keys.ALREADY_UPLOAD_SMS, true, Activity.MODE_MULTI_PROCESS)
    }
}