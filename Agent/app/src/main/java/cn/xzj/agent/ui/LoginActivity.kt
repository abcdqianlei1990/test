package cn.xzj.agent.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.Token
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.iview.ILoginView
import cn.xzj.agent.net.Client
import cn.xzj.agent.presenter.LoginPresenter
import cn.xzj.agent.util.APPUpdateUtil
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.PrivacyUploadManager
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.widget.SimpleBottomSheetDialog
import cn.xzj.agent.widget.SimpleToast
import com.channey.utils.DeviceUtils
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : MVPBaseActivity<LoginPresenter>(), View.OnClickListener, ILoginView {
    private var environmentEventTime: Long = 0
    private var environmentEventNumber: Int = 0
    override fun context(): Context {
        return this
    }

    companion object {
        fun jump(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APPUpdateUtil.checkUpdate(this)
        hideToolbar()
        btn_verification_code.onCreate()
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_LOGIN_PAGE_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initParams() {
    }

    override fun initViews() {
        setTitle("小职姐服务版")
        val agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(this)
        if (agentInfo != null) {
            activity_login_mobile_ed.setText(agentInfo.phone, TextView.BufferType.EDITABLE)
            if (agentInfo.phone != null) activity_login_mobile_ed.setSelection(agentInfo.phone.length)
        }

    }

    override fun initData() {
    }

    override fun setListeners() {
        activity_login_submit_btn.setOnClickListener(this)
        btn_verification_code.setOnClickListener(this)
        activity_login_container.setOnClickListener(this)
        activity_login_banner_img.setOnClickListener(this)
        activity_login_agreement.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_login_submit_btn -> {
                login()
            }
            R.id.btn_verification_code -> {
                if (activity_login_mobile_ed.text!!.isEmpty()) {
                    SimpleToast.showNormal(activity_login_mobile_ed.hint.toString())
                    return
                }
                mPresenter.getVerificationCode(activity_login_mobile_ed.text.toString())
                CommonUtils.statistics(this, Constants.STATISTICS_LOGIN_CAPTCHA_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.activity_login_container -> {
                setEnvironment()
            }
            R.id.activity_login_banner_img -> {
                WebViewActivity.jump(this,"${ Client.H5_RES_HOST}activity/jiaru")
            }
            R.id.activity_login_agreement -> {
                WebViewActivity.jump(this,"https://h5activity.xiaozhijie.com/agreement/ziyou.html")
            }
        }
    }

    private fun login() {
        if (!activity_login_agreement_cb.isChecked){
            showToast("请阅读并同意相关协议")
            return
        }
        val name = activity_login_mobile_ed.text.toString()
        val code = activity_login_code_ed.text.toString()
        if (StringUtils.isEmpty(name)) {
            SimpleToast.showNormal(activity_login_mobile_ed.hint.toString())
            return
        }
        if (StringUtils.isEmpty(code)) {
            SimpleToast.showNormal(activity_login_code_ed.hint.toString())
            return
        }
        mPresenter.login(name, code)
        CommonUtils.statistics(this, Constants.STATISTICS_LOGIN_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    override fun onLoginSuccess(info: Token) {
        SharedPreferencesUtils.saveString(this,"lastLoginAccount",activity_login_mobile_ed.text.toString())
        if (info.roles.contains(EnumValue.ROLE_AGENT_MEMBER)||info.roles.contains(EnumValue.ROLE_AGENT_XX)) {
            SharedPreferencesUtil.setToken(this, info.accessToken)
            mPresenter.getAgentInfo()
        } else {
            SimpleToast.showNormal("此账号暂无登录权限！")
        }
    }

    override fun onLoginFailure(msg: String) {
        SimpleToast.showNormal(msg)
    }

    override fun onAgentInfoGetSuccess(agentInfo: AgentInfo) {
        //保存小职姐信息
        SharedPreferencesUtil.setCurrentAgentInfo(this, agentInfo)
        gotoMainPage()
        //上传用户隐私数据
        val mPrivacyUploadManager = PrivacyUploadManager(this)
        mPrivacyUploadManager.uploadPrivacy()
    }

    override fun onAgentInfoGetFailure() {
        gotoMainPage()
    }

    override fun onVerificationCodeGetSuccess() {
        btn_verification_code.onStart()
        SimpleToast.showNormal("发送成功")
    }

    private fun gotoMainPage() {
        ExcessiveActivity.jump(this)
        this.finish()
    }

    private fun setEnvironment() {
        environmentEventNumber++
        //点击头像切换环境 点击10下触发切换环境的窗口(5秒内点击10下)
        if (System.currentTimeMillis() - environmentEventTime > 5000) {
            environmentEventTime = System.currentTimeMillis()
        } else {
            if (environmentEventNumber == 7) {
                SimpleToast.showNormal("再点击2下触发切换环境界面")
            }
            if (environmentEventNumber >= 10) {
                showEnvironmentDialog()
                environmentEventNumber = 0
            }
        }
    }

    private fun showEnvironmentDialog() {
        SimpleBottomSheetDialog.newBuilder(this)
                .setData("开发环境", "测试环境", "生产环境")
                .setItemClicklistener { v, s, position ->
                    SharedPreferencesUtil.setEnvironment(this, position)
                    showToast("3秒后将重启APP.")
                    Handler().postDelayed(Runnable { DeviceUtils.restartApp(this,SplashActivity::class.java) },3000)
                }
                .show()
    }

}