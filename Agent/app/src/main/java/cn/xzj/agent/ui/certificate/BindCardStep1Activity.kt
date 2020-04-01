package cn.xzj.agent.ui.certificate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.certificate.BindCardPostBody
import cn.xzj.agent.iview.IBindCardStep1
import cn.xzj.agent.presenter.BindCardStep1Presenter
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.agent.AgentProfileActivity
import cn.xzj.agent.util.DialogUtil
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import com.idcard.*
import com.turui.bank.ocr.CaptureActivity
import kotlinx.android.synthetic.main.activity_bind_card.*

import java.util.ArrayList

/**
 * Created by channey on 2016/11/8.
 * version:1.0
 * desc:绑定银行卡第一步
 */

class BindCardStep1Activity : MVPBaseActivity<BindCardStep1Presenter>(), IBindCardStep1, View.OnClickListener {
    private var mAgentInfo:AgentInfo? = null
    private var mBankName:String? = null    //银行名称

    companion object {
        val TITLE = "添加银行卡"
        private val CAMERA_PERMISSION_REQUEST_CODE = 0x01
        val RESULT_GET_CARD_OK = 0x02
        val key_agentinfo = "agentinfo"
        fun jump(context: Activity, agentInfo: AgentInfo, requestCode: Int = Code.RequestCode.BindCardStep1Activity) {
            val intent = Intent(context, BindCardStep1Activity::class.java)
            intent.putExtra(key_agentinfo,agentInfo)
            context.startActivityForResult(intent, requestCode)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_bind_card
    }

    override fun context(): Context {
        return this
    }

    private var mEngine: TRECAPIImpl? = null
    private var mCardNumber: String? = null

    override fun initParams() {
        mAgentInfo = intent.getSerializableExtra(key_agentinfo) as AgentInfo?
        checkPermission()
    }

    override fun initViews() {
        setTitle(TITLE)
        bind_card_ownername_tv.text = mAgentInfo!!.name
    }

    override fun setListeners() {
        bind_card_number_icon.setOnClickListener(this)
        bind_card_btn.setOnClickListener(this)
    }

    override fun initData() {
        initScanEngine()
    }

    private fun initScanEngine() {
        mEngine = TRECAPIImpl() //初始化图睿扫描引擎
        val s = mEngine!!.TR_GetUseTimeString()
        val tStatus = mEngine!!.TR_StartUP(this, mEngine!!.TR_GetEngineTimeKey())
        if (tStatus == TStatus.TR_TIME_OUT) {

        } else if (tStatus == TStatus.TR_FAIL) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mEngine!!.TR_ClearUP()
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            DialogUtil.showNoticeDialog(
                context = this,
                title = "提示",
                content = "请允许程序打开摄像头权限，否则程序将不能正常工作，" + "如果系统权限申请菜单未能弹出，请自行前往设置->应用权限管理中设置",
                listener = View.OnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                    }
                    DialogUtil.dismissNoticeDialog()
                })
        }
    }

    override fun onClick(view: View) {
        val i = view.id
        when (i) {
            R.id.bind_card_number_icon -> {
                CaptureActivity.tengineID = TengineID.TIDBANK
                val intent = Intent(this, CaptureActivity::class.java)
                intent.putExtra("engine", mEngine)
                startActivityForResult(intent, RESULT_GET_CARD_OK)
            }
            R.id.bind_card_btn -> {
                if (inputCheck() && mAgentInfo != null && mBankName != null) {
                    val cardNumber = StringUtils.deleteSpaces(bind_card_number.text.toString())
                    mPresenter.bindCard(BindCardPostBody(mBankName!!,mAgentInfo!!.name,cardNumber))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_GET_CARD_OK -> if (data != null) {
                val bundle = data.extras
                // 处理银行卡扫描结果（在界面上显示）
                //            String result = bundle.getString("result");
                val number = mEngine!!.TR_GetOCRFieldStringBuf(TFieldID.TBANK_NUM)
                mBankName = mEngine!!.TR_GetOCRFieldStringBuf(TFieldID.TBANK_NAME)
                if (!StringUtils.isEmpty(number)) {
                    mCardNumber = StringUtils.deleteSpaces(number)
                    bind_card_number.setText(number)
                    bind_card_number.setSelection(number.length)
                }
            }
        }
    }

    private fun inputCheck(): Boolean {
        val cardNo = bind_card_number.text.toString()
        if (TextUtils.isEmpty(cardNo)) {
            showToast("请输入银行卡卡号")
            return false
        } else {
            val s = StringUtils.deleteSpaces(cardNo)
            if (s.length < 14 || s.length > 20) {
                showToast("您输入的卡号位数不正确，请确认")
                return false
            }
        }
        return true
    }

    override fun onBindSuccess(success: Boolean) {
        if (success){
            showToast("绑卡成功")
            setResult(Code.ResultCode.OK)
            finish()
        }else{

        }
    }

    override fun onBindFailure() {

    }
}
