package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.common.QrCodeTemplateInfo

interface IMyQRCode : BaseView {
    fun onQRCodeTemplateGetSuccess(list: ArrayList<QrCodeTemplateInfo>)
    fun onQRCodeTemplateGetFailure()
}