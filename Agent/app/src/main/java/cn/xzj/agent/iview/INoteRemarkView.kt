package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.task.TaskInfo

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/11/8
 * @ Des
 */
interface INoteRemarkView : BaseView {
    fun onRemarkSuccess(success: Boolean)
    fun onRemarkFailure(msg: String)
    fun onGetRegisterTaskSuccess(taskInfo: TaskInfo?)
    fun onGetRegisterTaskFailure(msg: String)
    fun makeTaskDoneSuccess(content: Boolean)
    fun makeTaskDoneFailure()
}
