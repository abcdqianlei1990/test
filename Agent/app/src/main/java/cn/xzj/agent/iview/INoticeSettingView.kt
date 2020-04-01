package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
interface INoticeSettingView : BaseView {
    fun onSettingSuccess(isSuccess: Boolean)
    fun onRemarkSuccess(isSuccess: Boolean)
    fun onSettingFailure()
    fun onRemarkFailure(msg:String)
}