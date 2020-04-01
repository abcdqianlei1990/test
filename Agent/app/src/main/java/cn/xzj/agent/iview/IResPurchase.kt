package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customerres.PurchasbleCustomerResResp
import cn.xzj.agent.entity.customerres.PurchasbleResInfo
import cn.xzj.agent.entity.customerres.ResPurchaseResp

interface IResPurchase : BaseView {
    fun onPurchasbleResGetSuccess(info: PurchasbleCustomerResResp)
    fun onPurchasbleResGetFailure()

    fun onGoldenBeansCountGetSuccess(count: Int)
    fun onGoldenBeansCountGetFail()

    fun onResPurchaseSuccess(resp: ResPurchaseResp)
    fun onResPurchaseFail()
}