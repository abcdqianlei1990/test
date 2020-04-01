package cn.xzj.agent.ui.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.entity.ScanLoginBody
import cn.xzj.agent.iview.IScanLoginView
import cn.xzj.agent.presenter.ScanLoginPresenter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.SimpleToast
import kotlinx.android.synthetic.main.activity_scan_login.*
import org.greenrobot.eventbus.EventBus

/**
 * 扫码登录页面
 */
class ScanLoginActivity : MVPBaseActivity<ScanLoginPresenter>(), IScanLoginView {
    private lateinit var id: String

    companion object {
        val LOGIN_SUCCESS_CODE=200
        val ID = "id"
        fun jump(context: Context, id: String) {
            val mIntent = Intent()
            mIntent.setClass(context, ScanLoginActivity::class.java)
            mIntent.putExtra(ID, id)
            context.startActivity(mIntent)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        id = intent.getStringExtra(ID)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_scan_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLifeBack()
        setTitle("扫描登录")
    }

    override fun initViews() {
        btn_login.setOnClickListener {
            mPresenter.scanLogin(ScanLoginBody(id))
            CommonUtils.statistics(this, Constants.STATISTICS_SCAN_LOGIN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        }
    }

    override fun onScanLoginSuccess() {
        SimpleToast.showNormal("登录成功")
        EventBus.getDefault().post(DefaultEventMessage(LOGIN_SUCCESS_CODE,"登录成功",null))
        finish()
    }

    override fun onScanLoginFial() {
    }

}
