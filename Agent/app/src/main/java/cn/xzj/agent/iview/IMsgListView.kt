package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.MsgInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.RewardConditionInfo

interface IMsgListView : BaseView {
    fun onMsgListGetSuccess(info: CommonListBody<MsgInfo>)
    fun onMsgListGetFailure(msg: String)

    fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo)
    fun onCustomerDetailInfoGetFailure()
}