package cn.xzj.agent.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.Gravity
import cn.xzj.agent.R
import cn.xzj.agent.entity.DeviceInfo
import cn.xzj.agent.util.*
import cn.xzj.agent.widget.CommonDialog
import com.channey.utils.DeviceUtils
import com.channey.utils.StringUtils
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 *
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/16
 * @ Des
 */
class SplashActivity : Activity() {
    private lateinit var mCommonDialog: CommonDialog
    private var requestPermissionNumber = 0//请求权限次数
    private val requestPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        StatusBarUtil.darkMode(this)
        mCommonDialog = CommonDialog.newBuilder(this)
                .setContentGravity(Gravity.LEFT)
                .setCancelable(false)
                .setMessage("确保程序能正常运行，需获取" + PermissionsUtil.permissionNames(*requestPermission) + "权限,\n拒绝权限程序将无法继续")
                .setPositiveButton("确定") { dialog ->
                    dialog.dismiss()
                    RxPermissions(this).request(*requestPermission)
                            .subscribe { granted ->
                                if (granted) {
                                    initParams()
                                } else {
                                    requestPermissionNumber++
                                    if (requestPermissionNumber < 3) {
                                        mCommonDialog.show()
                                    } else {
                                        //可能是总是拒绝，提示用户去设置界面运行
                                        jumpToSetting()

                                    }
                                }
                            }

                }
                .create()
        requestPermissions()
    }


    fun initLayout(): Int {
        return R.layout.activity_splash
    }

    fun initParams() {
        initDeviceInfo(this)
        jump()
    }

    fun jump() {
        fun hasLogin(): Boolean {
            val token = SharedPreferencesUtil.getToken(this)
            return !StringUtils.isEmpty(token)
        }

        val context = this
        Thread(Runnable {
            Thread.sleep(500)
            if (hasLogin()) {
                ExcessiveActivity.jump(this)
                finish()
            } else {
                LoginActivity.jump(context)
                finish()
            }
        }).start()
    }

    private fun requestPermissions() {
        if (!PermissionsUtil.allowPermissions(this, *requestPermission)) {
            mCommonDialog.show()
        } else {
            initParams()
        }
    }

    @SuppressLint("HardwareIds")
    fun initDeviceInfo(activity: Activity) {
        var mDeviceInfo = SharedPreferencesUtil.getDeviceInfo(this)
        if (mDeviceInfo == null) {
            mDeviceInfo = DeviceInfo()
            mDeviceInfo.deviceModel = Build.MODEL
            mDeviceInfo.deviceOSVersion = Build.VERSION.RELEASE
            val deviceRooted = DeviceUtils.isDeviceRooted()
            val rooted = if (deviceRooted) "1" else "0"
            mDeviceInfo.escape = rooted
            val totalRam = Util.getTotalRam(activity)
            mDeviceInfo.memory = totalRam
            mDeviceInfo.deviceName = ""
            mDeviceInfo.osType = "android"
            mDeviceInfo.macAddress = NetworkUtil.getMacAddress()
            var deviceId: String? = null
            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                    val telephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    deviceId = telephonyManager.deviceId
                }

                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = NetworkUtil.getMacAddress()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mDeviceInfo.id = deviceId

            SharedPreferencesUtil.setDeviceInfo(this, mDeviceInfo)
        }
    }

    private fun jumpToSetting() {
        CommonDialog.newBuilder(this)
                .setMessage("您已拒绝权限请去设置页面授予权限\n步骤:->设置->应用管理->小职姐服务版->权限")
                .setPositiveButton(resources.getString(R.string.confirm)) { dialog ->
                    dialog!!.cancel()
                    PermissionsUtil.getAppDetailSettingIntent(this)
                    finish()
                }
                .create()
                .show()
    }

}