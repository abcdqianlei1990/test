package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.RegionJsonPickerBean
import cn.xzj.agent.entity.customer.City
import cn.xzj.agent.entity.customer.RemarkVO

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/6
 * @ Des
 */
interface IUserInfoRemarkView : BaseView {
    fun getRegionsSuccess(cityList: ArrayList<City>)
    fun getRegionsFail()
    fun onRemarkFailure()
    fun onRemarkSuccess()
    fun onRemarkGetFailure()
    fun onRemarkGetSuccess(info: RemarkVO)
    fun getLocationRegionSuccess(region: RegionJsonPickerBean)
}
