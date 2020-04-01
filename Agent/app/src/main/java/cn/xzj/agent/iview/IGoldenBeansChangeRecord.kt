package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.goldenbeans.GoldenBeansChangeRecordInfo

interface IGoldenBeansChangeRecord : BaseView {
    fun onRecordsGetSuccess(info: CommonListBody<GoldenBeansChangeRecordInfo>)
    fun onRecordsGetFailure()
}