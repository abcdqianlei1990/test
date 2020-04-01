package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.QiandaoRecordInfo

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/27
 * @ Des
 */
interface IQiandaoRecordsView : BaseView{
    fun onRecordsGetSuccess(info: CommonListBody<QiandaoRecordInfo>)
    fun onRecordsGetFailure(msg:String)

}
