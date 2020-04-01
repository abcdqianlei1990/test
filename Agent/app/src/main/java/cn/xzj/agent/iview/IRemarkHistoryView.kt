package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.RemarkHistoryInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/9
 * @ Des
 */
interface IRemarkHistoryView :BaseView{
    fun onHistoryGetSuccess(info: CommonListBody<RemarkHistoryInfo>)
    fun onHistoryGetFailure()
}