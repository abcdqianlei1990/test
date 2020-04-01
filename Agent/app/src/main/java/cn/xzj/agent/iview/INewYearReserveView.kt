package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.OriginalCustomerInfo
import cn.xzj.agent.entity.newyearreservation.NewYearReservation

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/28
 * @Des
 */
interface INewYearReserveView : BaseView {
    fun onGetNewYearReservationSuccess(data: CommonListBody<NewYearReservation>)
    fun onGetNewYearReservationFail()
    fun onCustomerDetailInfoGetSuccess(info: CustomerDetailInfo, number: String, action: Int, position: Int)

}