package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.active_customer.ActiveCustomerInfo
import cn.xzj.agent.entity.active_customer.CustomerActiveEventInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/27
 * @Des
 */
interface IActiveCustomerView : BaseView {
    fun getActiveCustomersSuccess(data: CommonListBody<ActiveCustomerInfo>)
    fun getActiveCustomersFailure(isNetWorkError: Boolean)
    fun getCustomerActiveEventsSuccess(data: List<CustomerActiveEventInfo>)
    fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo, userId:String)
}