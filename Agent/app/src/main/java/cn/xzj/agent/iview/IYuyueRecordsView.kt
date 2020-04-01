package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.YuyueRecordInfo

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/27
 * @ Des
 */
interface IYuyueRecordsView:BaseView {

    fun onRecordsGetFailure(msg:String)
    fun onRecordsGetSuccess(info: CommonListBody<YuyueRecordInfo>)
    fun cancelSuccess(info:Boolean)
    fun cancelFailure(msg:String)
}