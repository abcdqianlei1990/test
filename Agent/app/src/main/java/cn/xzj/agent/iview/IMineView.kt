package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.share.WechatHongbaoShareInfo

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/14
 * @Des
 */
interface IMineView : BaseView {
    fun onAgentInfoGetSuccess(agentInfo: AgentInfo)
    fun onAgentInfoGetFailure()
    fun onGetWechatHongbaoSuccess(wechatHongbaoShareInfo: WechatHongbaoShareInfo)
    fun onGetWechatHongbaoFail()

    fun onGoldenBeansCountGetSuccess(count: Int)
    fun onGoldenBeansCountGetFail()
}