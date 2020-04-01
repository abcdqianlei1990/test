package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.agentinfo.AgentInfo

interface IExcessive :BaseView{
    fun onAgentProfileInfoGetSuccess(info:AgentInfo)
    fun onAgentProfileInfoGetFailure()
}