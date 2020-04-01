package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.goldenbeans.GoldenBeansProductListInfo

interface IMyGoldenBeans : BaseView {
    fun onPurchasableGetSuccess()
    fun onPurchasableGetFailure()

    fun onProductListGetSuccess(info:GoldenBeansProductListInfo)
    fun onProductListGetFailure()

    fun onGoldenBeansCountGetSuccess(count: Int)
    fun onGoldenBeansCountGetFail()
}