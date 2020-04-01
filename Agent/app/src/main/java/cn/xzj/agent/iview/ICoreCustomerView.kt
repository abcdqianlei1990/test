package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.core_customer.CoreCustomerInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/27
 * @Des
 */
interface ICoreCustomerView : BaseView {
    fun getCoreCustomersSuccess(data: CommonListBody<CoreCustomerInfo>)
    fun getCoreCustomersFailure(isNetWorkError: Boolean)
    fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo,userId:String)
}