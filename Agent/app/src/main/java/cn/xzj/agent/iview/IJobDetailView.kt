package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.job.JobInfo

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/7
 * @Des
 */
interface IJobDetailView :BaseView {
    fun onDetailGetFailure(msg: String?)
    fun onDetailGetSuccess(info: JobInfo?)
}