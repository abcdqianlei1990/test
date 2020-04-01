package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.task.TaskTypeInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
interface IHomeView : BaseView {
    fun onTaskTypesGetSuccess(content: List<TaskTypeInfo>)
    fun onTaskTypesGetFailure(isNetWorkError:Boolean)
    fun onUnreadMsgGetSuccess(content: Int)
    fun onUnreadMsgGetFailure()
}