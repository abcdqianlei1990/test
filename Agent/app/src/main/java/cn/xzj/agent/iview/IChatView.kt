package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.RobotChatInfo

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/19
 * @Des
 */

interface IChatView :BaseView{
    fun onGetProblemResultSuccess(robotChatInfo: RobotChatInfo)
    fun onGetProblemResultFail()
}