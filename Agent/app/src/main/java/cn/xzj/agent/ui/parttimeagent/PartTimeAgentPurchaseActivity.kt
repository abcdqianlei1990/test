package cn.xzj.agent.ui.parttimeagent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.event.WxPaymentSuccessfulEvent
import cn.xzj.agent.entity.payment.PaymentPostBody
import cn.xzj.agent.entity.payment.PaymentRetInfo
import cn.xzj.agent.iview.IPartTimeAgent
import cn.xzj.agent.presenter.PartTimeAgentPurchasePresenter
import cn.xzj.agent.ui.LoginActivity
import cn.xzj.agent.util.PaymentUtil
import cn.xzj.agent.util.SharedPreferencesUtil
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_parttiime_agent_purchase.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PartTimeAgentPurchaseActivity:MVPBaseActivity<PartTimeAgentPurchasePresenter>(),IPartTimeAgent,View.OnClickListener {

    private var mAmount:Double = 0.0
    private var mPaymentMethod = EnumValue.PAYMENT_TYPE_WX  //默认为微信支付
    companion object {
        val key_amount = "amount"
        fun jump(context: Activity,amount:Double) {
            val intent = Intent(context, PartTimeAgentPurchaseActivity::class.java)
            intent.putExtra(key_amount,amount)
            context.startActivityForResult(intent,Code.RequestCode.PartTimeAgentPurchaseActivity)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_parttiime_agent_purchase
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mAmount = intent.getDoubleExtra(key_amount,0.0)
        activity_parttimeAgentPurchase_amount.text = StringUtils.doubleFormat(mAmount)
    }

    override fun initData() {
        super.initData()
    }

    override fun initViews() {
        com.jaeger.library.StatusBarUtil.setColor(this,resources.getColor(R.color.green29AC3E),0)
        setTitle("加入兼职小职姐")
        setTitleBgGreen()
    }

    override fun setListeners() {
        super.setListeners()
        activity_parttimeAgentPurchase_payment_method_wx_container.setOnClickListener(this)
        activity_parttimeAgentPurchase_btn.setOnClickListener(this)
        setOnBackClickListener(View.OnClickListener {
            gotoLoginActivity()
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            gotoLoginActivity()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun gotoLoginActivity(){
        SharedPreferencesUtil.clear(this)
        LoginActivity.jump(this)
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_parttimeAgentPurchase_payment_method_wx_container -> {
                    activity_parttimeAgentPurchase_payment_cb.isChecked = !activity_parttimeAgentPurchase_payment_cb.isChecked
                    if (activity_parttimeAgentPurchase_payment_cb.isChecked){
                        mPaymentMethod = EnumValue.PAYMENT_TYPE_WX
                    }else{
                        mPaymentMethod = EnumValue.PAYMENT_TYPE_NONE
                    }
                }
                R.id.activity_parttimeAgentPurchase_btn -> {
                    if (mPaymentMethod == EnumValue.PAYMENT_TYPE_NONE){
                        showToast("请先选择支付方式")
                    }else{
                        if (mAmount > 0) {
                            var body = PaymentPostBody()
                            body.paymentMethod = EnumValue.PAYMENT_TYPE_WX
                            mPresenter.partTimeAgentPurchase(body)
                        }
                    }
                }
            }
        }
    }

    override fun onPurchaseSuccess(info: PaymentRetInfo) {
        PaymentUtil.payByWechat(this,info)
    }

    override fun onPurchaseFailure() {
        
    }

    /**
     * 支付成功后的消息消费者
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxPaymentSuccessful(event: WxPaymentSuccessfulEvent){
        //支付成功后查询兼职小职姐支付状态
        showProgressDialog("",false)
        if (event.success) mPresenter.getAgentInfo()
    }

    override fun onAgentProfileInfoGetSuccess(info: AgentInfo) {
        if (info.isCharge && info.chargeStatus == EnumValue.PARTTIME_PAYMENT_STATUS_SUCC){
            closeProgressDialog()
            gotoSuccessPage()
        }else{
            Handler().postDelayed({
                mPresenter.getAgentInfo()
            },3000)
        }
    }

    private fun gotoSuccessPage(){
        PartTimeAgentPurchaseSucActivity.jump(this)
        finish()
    }

    override fun onAgentProfileInfoGetFailure() {
        closeProgressDialog()
    }
}