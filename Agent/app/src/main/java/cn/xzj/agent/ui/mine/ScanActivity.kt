package cn.xzj.agent.ui.mine

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Vibrator
import android.text.TextUtils
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.xzj.agent.R
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.util.PermissionsUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : QuickActivity(), QRCodeView.Delegate {
    override fun onScanQRCodeSuccess(result: String?) {
        vibrator()
        if (!TextUtils.isEmpty(result)) {
            ScanLoginActivity.jump(this, result!!)
        } else {
            SimpleToast.showNormal("$result")
            zxingview.startSpot()
        }
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
    }

    override fun onScanQRCodeOpenCameraError() {
        SimpleToast.showNormal("打开相机出错")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_scan
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        zxingview.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        requestCameraPermission()
    }

    override fun onResume() {
        super.onResume()
        zxingview.startSpot()
    }

    override fun onDestroy() {
        super.onDestroy()
        zxingview.onDestroy()
    }

    override fun initViews() {
        btn_back.setOnClickListener {
            finish()
        }
    }

    private fun vibrator() {
        val validator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        validator.vibrate(200)
    }

    override fun onDefaultEvent(event: DefaultEventMessage) {
        super.onDefaultEvent(event)
        if (event.code == ScanLoginActivity.LOGIN_SUCCESS_CODE) {
            //登录成功关闭扫描页面
            finish()
        }
    }

    @SuppressLint("CheckResult")
    private fun requestCameraPermission() {
        val mRxPermissions = RxPermissions(this)
        mRxPermissions.requestEach(
                Manifest.permission.CAMERA)
                .subscribe { permission ->
                    if (permission.granted) {
                        zxingview.startCamera()
                        zxingview.showScanRect()
                        zxingview.startSpot()
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        CommonDialog.newBuilder(this)
                                .setTitle("权限请求")
                                .setMessage("拒绝权限将无法使用相机功能，是否允许？")
                                .setNegativeButton("取消") {
                                    finish()
                                }
                                .setPositiveButton("确定") {
                                    it.cancel()
                                    requestCameraPermission()
                                }
                                .create()
                                .show()
                    } else {
                        CommonDialog.newBuilder(this)
                                .setTitle("权限请求")
                                .setMessage("您已始终拒绝打开" + PermissionsUtil.permissionNames(Manifest.permission.CAMERA) + "权限您已拒绝权限请去设置页面授予权限\n步骤:->设置->应用管理->小职姐服务版->权限\"")
                                .setNegativeButton("取消") {
                                    finish()
                                }
                                .setPositiveButton("确定") {
                                    it.cancel()
                                    PermissionsUtil.getAppDetailSettingIntent(this)
                                    finish()
                                }
                                .create()
                                .show()
                    }
                }
    }

}
