package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.RequestRecordInfo
import cn.xzj.agent.entity.job.JobInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/27
 * @ Des
 */
interface IRequestRecordsView :BaseView {
    fun onRecordsGetSuccess(info: CommonListBody<RequestRecordInfo>)
    fun onRecordsGetFailure(msg:String)
    fun cancelSuccess(info:Boolean)
    fun onJobDetailGetSuccess(info: JobInfo,position:Int)
}