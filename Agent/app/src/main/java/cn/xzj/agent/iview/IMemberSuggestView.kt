package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView

interface IMemberSuggestView : BaseView {
    fun onSuggestFailure(msg: String)
    fun onSuggestSuccess(success:Boolean)
}
