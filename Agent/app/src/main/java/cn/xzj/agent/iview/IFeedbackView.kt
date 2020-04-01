package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.FileUploadVO

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/3/7
 * @Des
 */
interface IFeedbackView:BaseView {
    fun onFeedbackSuccess()
    fun onImagesUploadSuccess(data: List<FileUploadVO>)
    fun onImagesUploadFail()
}