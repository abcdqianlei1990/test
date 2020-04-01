package cn.xzj.agent.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.iview.IExcessive
import cn.xzj.agent.iview.IPartTimeAgent
import cn.xzj.agent.presenter.ExcessivePresenter
import cn.xzj.agent.presenter.PartTimeAgentPurchasePresenter
import cn.xzj.agent.ui.parttimeagent.PartTimeAgentPurchaseActivity
import cn.xzj.agent.util.SharedPreferencesUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_excessive.*

class ExcessiveActivity:MVPBaseActivity<ExcessivePresenter>(), IExcessive {

    companion object {
        fun jump(context: Activity) {
            val intent = Intent(context, ExcessiveActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_excessive
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
    }

    override fun initData() {
        super.initData()
        mPresenter.getAgentInfo()
    }

    override fun initViews() {
        hideToolbar()
        activity_excessive_progressbar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(this,R.color.green29AC3E), PorterDuff.Mode.MULTIPLY);
    }

    fun jump() {
        val context = this
        Thread(Runnable {
            Thread.sleep(500)
            MainActivity.jump(context)
            finish()
        }).start()
    }

    override fun onAgentProfileInfoGetSuccess(info: AgentInfo) {
//        info.isCharge = true
//        info.chargeAmount = 299.99
        if (info.isCharge && info.chargeStatus != EnumValue.PARTTIME_PAYMENT_STATUS_SUCC){
            PartTimeAgentPurchaseActivity.jump(this,info.chargeAmount)
            finish()
        }else{
            jump()
        }
    }

    override fun onAgentProfileInfoGetFailure() {

    }
}