package cn.xzj.agent

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import cn.xzj.agent.constants.Config
import cn.xzj.agent.util.*
import com.channey.utils.DeviceUtils
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import com.channey.utils.ToastUtils
import com.iflytek.cloud.SpeechUtility
import com.kk.taurus.exoplayer.ExoMediaPlayer
import com.kk.taurus.ijkplayer.IjkPlayer
import com.kk.taurus.playerbase.config.PlayerConfig
import com.kk.taurus.playerbase.config.PlayerLibrary
import com.kk.taurus.playerbase.entity.DecoderPlan
import com.kk.taurus.playerbase.record.PlayRecordManager
import com.meiqia.core.callback.OnInitCallback
import com.meiqia.meiqiasdk.util.MQConfig
import com.mob.MobSDK
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.ye.widget.StatusLayout
import com.ye.widget.status.BasicStatusLayout
import com.ye.widget.status.DefaultStatusCreator
import io.objectbox.android.AndroidObjectBrowser

class MyApplication : Application() {
    val PLAN_ID_IJK = 1
    val PLAN_ID_EXO = 2
    open var FLAVOR = "xzjagent"
    var deviceToken:String? = null

    companion object {
        lateinit var application: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        FLAVOR = CommonUtils.getChannel(this)
//        Log.d("qian","&&&&&&&& $FLAVOR")
        //设置日志模式
        LogLevel.setDebug(BuildConfig.DEBUG)
        if (BuildConfig.DEBUG) {
            //初始化内存泄漏检测工具
//            LeakCanary.install(this)
        }
        //初始化智能刷新控件的默认风格
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)//启用矢量图兼容
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.commonBackground, R.color.black333333)//全局设置主题颜色
            val mClassicsHeader = ClassicsHeader(context)
            mClassicsHeader.setTimeFormat(DynamicTimeFormat("更新于 %s"))
            mClassicsHeader.setEnableLastTime(false)
            mClassicsHeader
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) as RefreshFooter }
        //友盟统计
        initPushService()
        //mob分享初始化
        MobSDK.init(this)

        //科大讯飞
        SpeechUtility.createUtility(this, "appid=${Config.KDXF_APPID}")
        //配置全局状态布局
        StatusLayout.setDefaultStatusCreator(object : DefaultStatusCreator {
            override fun loadErrorLayout(): BasicStatusLayout {
                return object : BasicStatusLayout(application) {
                    override fun getLayoutId(): Int {
                        return R.layout.view_error
                    }

                    override fun getRetryId(): Int {
                        return R.id.tv_try
                    }
                }
            }

            override fun loadingLayout(): BasicStatusLayout {
                return object : BasicStatusLayout(application) {
                    override fun getLayoutId(): Int {
                        return R.layout.view_loading
                    }

                    override fun getRetryId(): Int {
                        return 0
                    }
                }
            }

            override fun emptyLayout(): BasicStatusLayout {
                return object : BasicStatusLayout(application) {
                    override fun getLayoutId(): Int {
                        return R.layout.view_empty
                    }

                    override fun getRetryId(): Int {
                        return 0
                    }
                }
            }

            override fun networkErrorLayout(): BasicStatusLayout {
                return object : BasicStatusLayout(application) {
                    override fun getLayoutId(): Int {
                        return R.layout.view_network_error
                    }

                    override fun getRetryId(): Int {
                        return R.id.tv_try
                    }
                }
            }

        })

        //ijkplayer视频播放相关
        initVideoPlay()

        //objectBox
//        mBoxStore=MyObjectBox.builder().androidContext(this).build()
//        mDownloadInfoBox=mBoxStore.boxFor(DownloadInfo::class.java)
        DB.init(this)
        if (BuildConfig.DEBUG) {
            AndroidObjectBrowser(DB.getDB().boxStore).start(this)
        }
        initBugly()
        initMeiQia()
    }

    private fun initVideoPlay() {
        try {

//            //如果您想使用默认的网络状态事件生产者，请添加此行配置。
//            //并需要添加权限 android.permission.ACCESS_NETWORK_STATE
//            PlayerConfig.setUseDefaultNetworkEventProducer(true)
//            //初始化库
//            PlayerLibrary.init(this)
//
//            //-------------------------------------------
//
//            //如果添加了'cn.jiajunhui:exoplayer:xxxx'该依赖
//            ExoMediaPlayer.init(this)
//
//            //如果添加了'cn.jiajunhui:ijkplayer:xxxx'该依赖
//            IjkPlayer.init(this)
//
//
//            //播放记录的配置
//            //开启播放记录
//            PlayerConfig.playRecord(true)
//            PlayRecordManager.setRecordConfig(
//                    PlayRecordManager.RecordConfig.Builder()
//                            .setMaxRecordCount(100)
//                            //.setRecordKeyProvider()
//                            //.setOnRecordCallBack()
//                            .build())
            PlayerConfig.addDecoderPlan(DecoderPlan(PLAN_ID_IJK, IjkPlayer::class.java.name, "IjkPlayer"))
            PlayerConfig.addDecoderPlan(DecoderPlan(PLAN_ID_EXO, ExoMediaPlayer::class.java.name, "ExoPlayer"))
            PlayerConfig.setDefaultPlanId(PLAN_ID_IJK)

            //use default NetworkEventProducer.
            PlayerConfig.setUseDefaultNetworkEventProducer(true)

            PlayerConfig.playRecord(true)

            PlayRecordManager.setRecordConfig(
                    PlayRecordManager.RecordConfig.Builder()
                            .setMaxRecordCount(100).build())

            PlayerLibrary.init(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initBugly() {
        val packageName = packageName
        // 获取当前进程名
        val processName = DeviceUtils.getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(applicationContext)
        strategy.isUploadProcess = processName == null || processName == packageName
        //参数设置
        CrashReport.setAppChannel(applicationContext, FLAVOR)
        CrashReport.setUserId(applicationContext, NetworkUtil.getMacAddress())
        var phone = SharedPreferencesUtils.getString(this,"lastLoginAccount")
        CrashReport.setAppVersion(applicationContext, BuildConfig.VERSION_NAME)
        CrashReport.putUserData(applicationContext,"phone",phone)

        // 初始化Bugly
        CrashReport.initCrashReport(applicationContext, "b70f7a20d6", BuildConfig.DEBUG, strategy);
    }

    private fun initPushService() {
        UMConfigure.init(this, Config.UM_APP_KEY, "pgy", UMConfigure.DEVICE_TYPE_PHONE, Config.UM_PUSH_SECRET)//"810a3218cd4daff42b8678e693466b42")
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        val mPushAgent = PushAgent.getInstance(this)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(token: String) {
//                Log.d("qian","友盟 deviceToken => "+token)
                deviceToken = token
                SharedPreferencesUtils.saveString(applicationContext, "device_token",token)
            }

            override fun onFailure(code: String, msg: String) {
//                Log.d("qian","友盟 onFailure => "+code+":"+msg);
            }
        })
    }

    private fun initMeiQia(){
        MQConfig.init(this, Config.MEIQIA_APP_KEY, object : OnInitCallback {
            override fun onSuccess(p0: String?) {
//                Log.d("qian","美洽 init success")
            }

            override fun onFailure(p0: Int, p1: String?) {
//                Log.d("qian","美洽 init failure")
            }
        });
    }
}