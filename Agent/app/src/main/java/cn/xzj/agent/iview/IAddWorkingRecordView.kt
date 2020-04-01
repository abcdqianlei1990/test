package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo

interface IAddWorkingRecordView : BaseView {
    fun onAddWorkingRecordSuccess()
    fun onAddWorkingRecordFailure()
}