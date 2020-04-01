package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.agentinfo.AgentInfo

interface IAgentProfile :BaseView{
    fun onAgentProfileInfoGetSuccess(info:AgentInfo)
    fun onAgentProfileInfoGetFailure()

    fun uploadFileSuccess(content:List<FileUploadVO>)
    fun uploadFileFailure()

    fun onDeltaUploadSuccess(content:List<FileUploadVO>)
    fun onDeltaUploadFailure()

    fun onFaceVerifySuccess(success:Boolean)
    fun onFaceVerifyFailure()
}