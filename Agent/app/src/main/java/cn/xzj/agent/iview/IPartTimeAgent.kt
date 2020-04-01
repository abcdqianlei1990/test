package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.RobotChatInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.payment.PaymentRetInfo

interface IPartTimeAgent :BaseView{
    fun onPurchaseSuccess(info: PaymentRetInfo)
    fun onPurchaseFailure()

    fun onAgentProfileInfoGetSuccess(info: AgentInfo)
    fun onAgentProfileInfoGetFailure()
}