package cn.xzj.agent.iview


import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.certificate.IDCardInfo

interface IRealNameIdentity:BaseView {
    fun uploadFileSuccess(content:List<FileUploadVO>)
    fun uploadFileFailure()

    fun onOCRSuccess(info: IDCardInfo)
    fun onOCRFailure(msg:String)

    fun onCommitCardInfoSuccess(success: Boolean)
    fun onCommitCardInfoFailure()

}