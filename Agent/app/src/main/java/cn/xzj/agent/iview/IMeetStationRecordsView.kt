package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.JiezhanRecordInfo

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/27
 * @ Des
 */
interface IMeetStationRecordsView:BaseView {
    fun cancelFailure(msg:String)
    fun cancelSuccess(info:Boolean)
    fun onRecordsGetSuccess(info: CommonListBody<JiezhanRecordInfo>)
    fun onRecordsGetFailure(msg:String)
}
