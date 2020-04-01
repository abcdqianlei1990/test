package cn.xzj.agent.ui.agent

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.FileUploadDTO
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.certificate.FaceVerifyPostBody
import cn.xzj.agent.iview.IAgentProfile
import cn.xzj.agent.iview.IPartTimeAgentPaymentSuc
import cn.xzj.agent.presenter.AgentProfilePresenter
import cn.xzj.agent.presenter.PartTimeAgentPaymentSucPresenter
import cn.xzj.agent.ui.LoginActivity
import cn.xzj.agent.ui.MainActivity
import cn.xzj.agent.ui.certificate.BindCardStep1Activity
import cn.xzj.agent.ui.certificate.RealNameIdentityActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.DialogUtil
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import com.megvii.licensemanager.Manager
import com.megvii.livenessdetection.LivenessLicenseManager
import com.megvii.livenesslib.LivenessActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_parttime_agent_payment_suc.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.File
import java.util.HashMap

class AgentProfileActivity:MVPBaseActivity<AgentProfilePresenter>(), IAgentProfile,View.OnClickListener {

    private var mAgentInfo:AgentInfo? = null
    private var mComeFromUserCenter = true
    companion object {
        val key_agentinfo = "agentinfo"
        val key_comeFromUserCenter = "comeFromUserCenter"
        val FaceBitmap_BEST = "image_best"
        val FaceBitmap_ENV = "image_env"
        fun jump(context: Activity,agentInfo: AgentInfo,comeFromUserCenter:Boolean = true) {
            val intent = Intent(context, AgentProfileActivity::class.java)
            intent.putExtra(key_agentinfo,agentInfo)
            intent.putExtra(key_comeFromUserCenter,comeFromUserCenter)
            context.startActivityForResult(intent,Code.RequestCode.PartTimeAgentPurchaseSucActivity)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_user_profile
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mAgentInfo = intent.getSerializableExtra(key_agentinfo) as AgentInfo?
        mComeFromUserCenter = intent.getBooleanExtra(key_comeFromUserCenter,true)
    }

    override fun initData() {
        super.initData()
    }

    override fun initViews() {
        setTitle("个人资料")
        setOnBackClickListener(View.OnClickListener {
            exit()
        })
        if (mAgentInfo != null){
            injectViews(mAgentInfo!!)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (mComeFromUserCenter){
            finish()
        }else{
            MainActivity.jump(this@AgentProfileActivity)
            finish()
        }
    }

    private fun injectViews(info:AgentInfo){
        activity_userProfile_name_tv.text = info.nick
        activity_userProfile_phone_tv.text = info.phone
        activity_userProfile_wx_tv.text = info.wechat
        //实名信息
        if (info.isRealName){
            activity_userProfile_realNameBtn_container.visibility = View.GONE
            activity_userProfile_realNameInfo_container.visibility = View.VISIBLE
            var sex = if (info.sex == 0) "男" else "女"
            activity_userProfile_realName_info_tv.text = "${info.name} ${sex} 身份证号：${info.addStarCardNo}"
        }else{
            activity_userProfile_realNameBtn_container.visibility = View.VISIBLE
            activity_userProfile_realNameInfo_container.visibility = View.GONE
        }
        //人脸认证
        if (info.isFace){
            activity_userProfile_faceBtn_container.visibility = View.GONE
            activity_userProfile_face_container.visibility = View.VISIBLE
        }else{
            activity_userProfile_faceBtn_container.visibility = View.VISIBLE
            activity_userProfile_face_container.visibility = View.GONE
        }
        //银行卡信息
        if (info.isBindCard){
            activity_userProfile_bankcardBtn_container.visibility = View.GONE
            activity_userProfile_bankcardInfo_container.visibility = View.VISIBLE
            activity_userProfile_bankcard_name_tv.text = "持卡人：${info.bankCardHolder}"
            activity_userProfile_bankcard_num_tv.text = "银行卡：${info.addStarBankCard}"
            activity_userProfile_bankcard_bankName_tv.text = "开户行：${info.bank}"
        }else{
            activity_userProfile_bankcardBtn_container.visibility = View.VISIBLE
            activity_userProfile_bankcardInfo_container.visibility = View.GONE
        }
    }
    override fun setListeners() {
        activity_userProfile_realNameBtn_container.setOnClickListener(this)
        activity_userProfile_faceBtn_container.setOnClickListener(this)
        activity_userProfile_bankcardBtn_container.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_userProfile_realNameBtn_container -> {
                    RealNameIdentityActivity.jump(this)
                }
                R.id.activity_userProfile_faceBtn_container -> {
                    if (mAgentInfo != null){
                        if (mAgentInfo!!.isRealName){
                            faceVerify()
                        }else{
                            showToast("请先进行实名认证")
                        }
                    }
                }
                R.id.activity_userProfile_bankcardBtn_container -> {
                    if (mAgentInfo != null){
                        if (mAgentInfo!!.isRealName){
                            BindCardStep1Activity.jump(this,mAgentInfo!!)
                        }else{
                            showToast("请先进行实名认证")
                        }
                    }
                }
            }
        }
    }

    /**
     * 联网授权
     */
    private fun startLicense() {
        Thread(Runnable {
            val manager = Manager(this)
            val licenseManager = LivenessLicenseManager(
                    this)
            manager.registerLicenseManager(licenseManager)

            manager.takeLicenseFromNetwork(CommonUtils.getUUIDString(this))
            if (licenseManager.checkCachedLicense() > 0)
                mHandler.sendEmptyMessage(MSG_LICENSE_SUCC)
            else
                mHandler.sendEmptyMessage(MSG_LICENSE_FAIL)
        }).start()
    }
    private val MSG_LICENSE_SUCC = 0X001
    private val MSG_LICENSE_FAIL = 0X002
    internal var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_LICENSE_SUCC -> {
                    startActivityForResult(Intent(this@AgentProfileActivity, LivenessActivity::class.java),
                            Code.RequestCode.FACE_IDENTITY)
                }
                MSG_LICENSE_FAIL -> showToast("联网授权失败！请检查网络或找服务商")
            }
        }
    }

    private fun faceVerify(){
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            DialogUtil.showNoticeDialog(this,"提示", "请允许程序打开摄像头权限，否则程序将不能正常工作，" + "如果系统权限申请菜单未能弹出，请自行前往设置->应用权限管理中设置", View.OnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
                }
                DialogUtil.dismissNoticeDialog()
            })
        } else {
            val rxPermissions = RxPermissions(this)
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe{ permission ->
                        when (permission.name) {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE ->{
                                if (permission.granted) {
                                    startLicense()
                                }else{
                                    showToast("请打开读写内存的权限")
                                }
                            }
                        }
                    }
        }
    }

    private var mDelta:String ?= null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Code.RequestCode.RealNameIdentityActivity,Code.RequestCode.BindCardStep1Activity -> {
                if (resultCode == Code.ResultCode.OK){
                    mPresenter.getAgentInfo()
                }
            }
            Code.RequestCode.FACE_IDENTITY -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data!!.getStringExtra("result")
                    mDelta = data.getStringExtra("delta")
                    var images = data!!.getSerializableExtra("images")
                    if (images != null){
                        //活体检测完成
                        uploadImages(images as HashMap<String, ByteArray>)
                    }else{
                        showToast("人脸检测未完成，请重试")
                    }
                } else {
                    showToast("人脸检测未完成，请重试")
                }
            }
        }
    }

    private fun uploadImages(map: HashMap<String,ByteArray>) {
        val bytes1 = map[FaceBitmap_BEST]
        val envBytes = map[FaceBitmap_ENV]

        val list = ArrayList<FileUploadDTO>()
        val bestBase64 = CommonUtils.byteArrayToBase64(bytes1)
        val envBase64 = CommonUtils.byteArrayToBase64(envBytes)
        var file1 = FileUploadDTO()
        file1.content = bestBase64
        file1.name = "$FaceBitmap_BEST.png"
        file1.size = bestBase64!!.length

        var file2 = FileUploadDTO()
        file2.content = envBase64
        file2.name = "$FaceBitmap_ENV.png"
        file2.size = envBase64!!.length
        list.add(file1)
        list.add(file2)
        mPresenter.uploadFile(list)
    }

    override fun onAgentProfileInfoGetSuccess(info: AgentInfo) {
        mAgentInfo = info
        injectViews(info)
    }

    override fun onAgentProfileInfoGetFailure() {

    }

    private var mImageUrl:String ?= null    //人像图path
    private var mBgImageUrl:String ?= null  //带背景的人像图path
    override fun uploadFileSuccess(info: List<FileUploadVO>) {
        mImageUrl = info[0].url
        mBgImageUrl = info[1].url
        //上传delta
        val list = ArrayList<FileUploadDTO>()
        var file = FileUploadDTO()
        var delta = CommonUtils.strToBase64(mDelta)
        file.content = delta
        file.name = "delta.txt"
        file.size = delta.length
        list.add(file)
        mPresenter.uploadDelta(list)
    }

    override fun uploadFileFailure() {

    }

    override fun onDeltaUploadSuccess(content: List<FileUploadVO>) {
        mPresenter.faceVerify(FaceVerifyPostBody(delta = content[0].url,faceImageUrl = mImageUrl!!,faceWithBgImageUrl = mBgImageUrl!!))
    }

    override fun onDeltaUploadFailure() {

    }

    override fun onFaceVerifySuccess(success: Boolean) {
        showToast("刷脸认证成功")
        mPresenter.getAgentInfo()
    }

    override fun onFaceVerifyFailure() {

    }
}