package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.agentinfo.AgentInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
interface IMainView : BaseView {
    fun onAgentInfoGetSuccess(agentInfo: AgentInfo)
    fun onAgentInfoGetFailure()
}