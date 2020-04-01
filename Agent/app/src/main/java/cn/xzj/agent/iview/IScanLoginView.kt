package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/11/13
 * @ Des
 */
interface IScanLoginView : BaseView {
    fun onScanLoginSuccess()
    fun onScanLoginFial()
}