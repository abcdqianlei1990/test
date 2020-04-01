package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customerres.PurchasbleCustomerResResp
import cn.xzj.agent.entity.customerres.PurchasbleResInfo
import cn.xzj.agent.entity.customerres.ResPurchaseRecordInfo
import cn.xzj.agent.entity.customerres.ResPurchaseResp

interface IResPurchaseRecord : BaseView {

    fun onRecordsGetSuccess(body: CommonListBody<ResPurchaseRecordInfo>)
    fun onRecordsGetFail()
}