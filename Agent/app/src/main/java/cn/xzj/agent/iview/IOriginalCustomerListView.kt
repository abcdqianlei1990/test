package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.OriginalCustomerInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
interface IOriginalCustomerListView :BaseView{
    fun onCustomersGetSuccess(content: CommonListBody<OriginalCustomerInfo>)
    fun onCustomersGetFailure()
    fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo, number: String, action: Int, position: Int)
    fun onCustomerDetailInfoGetFailure()
}