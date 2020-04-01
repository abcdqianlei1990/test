package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo

interface IAddWorkExp : BaseView {
    fun onWorkExpCompanyGetSuccess(list: ArrayList<WorkExperienceCompanyInfo>)
    fun onWorkExpCompanyGetFailure()

    fun uploadFileSuccess(content:List<FileUploadVO>)
    fun uploadFileFailure()

    fun uploadBadgeSuccess(success:Boolean)
    fun uploadBadgeFailure()
}