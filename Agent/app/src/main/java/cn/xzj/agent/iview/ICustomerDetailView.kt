package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.RemarkHistoryInfo
import cn.xzj.agent.entity.customer.RemarkVO
import cn.xzj.agent.entity.customer.WorkingRecordInfo

/**
 *
 * @ Author MarkYe
 * @ Date 2018/9/27
 * @ Des
 */
interface ICustomerDetailView :BaseView{
    fun onRemarkGetSuccess(info: RemarkVO)
    fun onRemarkGetFailure(msg: String)
    fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo)
    fun onCustomerDetailInfoGetFailure()
    fun onHistoryGetSuccess(info: CommonListBody<RemarkHistoryInfo>)
    fun onHistoryGetFailure()
    fun onWorkingRecordsGetSuccess(info: CommonListBody<WorkingRecordInfo>)
    fun onWorkingRecordsGetFailure()

    fun onDeleteWorkingRecordSuccess(boolean: Boolean)
    fun onDeleteWorkingRecordFailure()
}