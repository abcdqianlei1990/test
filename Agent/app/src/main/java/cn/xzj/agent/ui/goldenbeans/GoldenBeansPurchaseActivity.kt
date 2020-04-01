package cn.xzj.agent.ui.goldenbeans

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Message
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.payment.PaymentRetInfo
import cn.xzj.agent.entity.event.WxPaymentSuccessfulEvent
import cn.xzj.agent.entity.goldenbeans.GoldenBeansPaymentPostBody
import cn.xzj.agent.entity.payment.PaymentPostBody
import cn.xzj.agent.entity.payment.PaymentStatusInfo
import cn.xzj.agent.iview.IGoldenBeansPurchase
import cn.xzj.agent.presenter.GoldenBeansPurchasePresenter
import cn.xzj.agent.util.PaymentUtil
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_goldenbeans_payment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GoldenBeansPurchaseActivity: MVPBaseActivity<GoldenBeansPurchasePresenter>(), IGoldenBeansPurchase, View.OnClickListener {

    private var mAmount:Double = 0.0
    private var mCount:Int = 0
    private var mPaymentRetInfo:PaymentRetInfo? = null
    private var mPaymentMethod = EnumValue.PAYMENT_TYPE_WX  //默认为微信支付
    companion object {
        val key_amount = "amount"
        val key_count = "count"
        fun jump(context: Activity,count:Int,amount:Double,requestCode:Int=Code.RequestCode.GoldenBeansPurchaseActivity) {
            val intent = Intent(context, GoldenBeansPurchaseActivity::class.java)
            intent.putExtra(key_amount,amount)
            intent.putExtra(key_count,count)
            context.startActivityForResult(intent,requestCode)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_goldenbeans_payment
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mAmount = intent.getDoubleExtra(key_amount,0.0)
        mCount = intent.getIntExtra(key_count,0)
        activity_payment_count_tv.text = "购买金豆 ${mCount} 个"
        activity_payment_amount_tv.text = StringUtils.doubleFormat(mAmount)
        activity_payment_actualAmount_tv.text = "￥${StringUtils.doubleFormat(mAmount)}"
    }

    override fun initViews() {
        com.jaeger.library.StatusBarUtil.setColor(this,resources.getColor(R.color.green29AC3E),0)
        setTitle("支付")
        setTitleBgGreen()
        var colors = intArrayOf(resources.getColor(R.color.yellowFED966),resources.getColor(R.color.yellowFECE5C))
        ShapeUtil.setShape(
                activity_payment_yellow_bg,
                radius = resources.getDimension(R.dimen.dp3),
                gradientColors = colors,
                orientation = GradientDrawable.Orientation.TOP_BOTTOM
        )
    }

    override fun setListeners() {
        super.setListeners()
        activity_payment_method_wx_container.setOnClickListener(this)
        activity_payment_pay_btn.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_payment_method_wx_container -> {
                    activity_payment_cb.isChecked = !activity_payment_cb.isChecked
                    if (activity_payment_cb.isChecked){
                        mPaymentMethod = EnumValue.PAYMENT_TYPE_WX
                    }else{
                        mPaymentMethod = EnumValue.PAYMENT_TYPE_NONE
                    }
                }
                R.id.activity_payment_pay_btn -> {
                    if (mPaymentMethod == EnumValue.PAYMENT_TYPE_NONE){
                        showToast("请先选择支付方式")
                    }else{
                        if (mCount > 0 || mAmount > 0) {
                            var body = GoldenBeansPaymentPostBody()
                            body.gold = mCount
                            body.paymentMethod = mPaymentMethod
                            mPresenter.goldenBeanPurchase(body)
                        }
                    }
                }
            }
        }
    }

    override fun onPurchaseSuccess(info: PaymentRetInfo) {
        mPaymentRetInfo = info
        PaymentUtil.payByWechat(this,info)
    }

    override fun onPurchaseFailure() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxPaymentSuccessful(event: WxPaymentSuccessfulEvent){
        showProgressDialog("",false)
        if (mPaymentRetInfo != null){
            mPresenter.getPaymentStatus(mPaymentRetInfo!!.orderId)
        }else{
            showToast("订单id为空")
        }

    }

    override fun onPurchaseStatusGetSuccess(info: PaymentStatusInfo) {
        when(info.status){
            PaymentStatusInfo.PAYMENT_STATUS_PAYING-> {
                Handler().postDelayed({
                    mPresenter.getPaymentStatus(info.orderId)
                },3000)
            }
            PaymentStatusInfo.PAYMENT_STATUS_FAILURE -> {
                closeProgressDialog()
                showToast("支付失败")
            }
            PaymentStatusInfo.PAYMENT_STATUS_SUCCESS -> {
                closeProgressDialog()
                showToast("支付成功")
                setResult(Code.ResultCode.OK)
                finish()
            }
        }
    }

    override fun onPurchaseStatusGetFailure() {
        closeProgressDialog()
    }
}