package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.AgentInfo

interface IMyTeamView : BaseView {
    fun onAgentInfoGetSuccess(agentInfo: AgentInfo)
    fun onAgentInfoGetFailure()

    fun onLowerAgentsGetSuccess(list: CommonListBody<AgentInfo>)
    fun onLowerAgentsGetFailure()
}