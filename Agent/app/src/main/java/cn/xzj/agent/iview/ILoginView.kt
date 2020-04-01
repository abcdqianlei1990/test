package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.Token
import cn.xzj.agent.entity.agentinfo.AgentInfo

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/14
 * @ Des
 */
interface ILoginView : BaseView {
    fun onLoginFailure(msg: String)
    fun onLoginSuccess(info: Token)
    fun onAgentInfoGetSuccess(agentInfo: AgentInfo)
    fun onAgentInfoGetFailure()
    fun onVerificationCodeGetSuccess()
}
