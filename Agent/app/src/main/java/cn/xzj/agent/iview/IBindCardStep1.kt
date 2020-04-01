package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView


interface IBindCardStep1:BaseView {
    fun onBindSuccess(success:Boolean)
    fun onBindFailure()
}