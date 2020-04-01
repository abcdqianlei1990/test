package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.task.TaskInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
interface ITaskListView:BaseView {
    fun onTasksGetSuccess(content: CommonListBody<TaskInfo>)
    fun onTasksGetFailure(s: String)
    fun makeTaskDoneSuccess(content: Boolean)
    fun makeTaskDoneFailure()
    fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo,number: String, action: Int)
    fun onCustomerDetailInfoGetFailure()
}