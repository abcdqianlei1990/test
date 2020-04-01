package cn.xzj.agent.ui.parttimeagent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.iview.IPartTimeAgentPaymentSuc
import cn.xzj.agent.presenter.PartTimeAgentPaymentSucPresenter
import cn.xzj.agent.ui.MainActivity
import cn.xzj.agent.ui.agent.AgentProfileActivity
import kotlinx.android.synthetic.main.activity_parttime_agent_payment_suc.*

class PartTimeAgentPurchaseSucActivity:MVPBaseActivity<PartTimeAgentPaymentSucPresenter>(), IPartTimeAgentPaymentSuc,View.OnClickListener {

    companion object {
        fun jump(context: Activity) {
            val intent = Intent(context, PartTimeAgentPurchaseSucActivity::class.java)
            context.startActivityForResult(intent,Code.RequestCode.PartTimeAgentPurchaseSucActivity)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_parttime_agent_payment_suc
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
    }

    override fun initData() {
        super.initData()
    }

    override fun initViews() {
        setTitle("支付成功")
    }

    override fun setListeners() {
        activity_parttimeAgentPaymentSuc_btn.setOnClickListener(this)
        setOnBackClickListener(View.OnClickListener {
            MainActivity.jump(this)
            finish()
        })
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_parttimeAgentPaymentSuc_btn -> mPresenter.getAgentInfo()
            }
        }
    }
    override fun onAgentProfileInfoGetSuccess(info: AgentInfo) {

        AgentProfileActivity.jump(this,info,false)
        finish()
    }

    override fun onAgentProfileInfoGetFailure() {

    }

}