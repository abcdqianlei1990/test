package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.payment.PaymentRetInfo
import cn.xzj.agent.entity.payment.PaymentStatusInfo

interface IGoldenBeansPurchase : BaseView {
    fun onPurchaseSuccess(info: PaymentRetInfo)
    fun onPurchaseFailure()

    fun onPurchaseStatusGetSuccess(info: PaymentStatusInfo)
    fun onPurchaseStatusGetFailure()
}