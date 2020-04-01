package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.AgentWechatAccountInfo
import cn.xzj.agent.entity.FileUploadVO

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/23
 * @ Des
 */
interface IUploadWxScreenshotView : BaseView {
    fun wxScreenshotUploadSuccess(data: List<FileUploadVO>)
    fun wxScreenshotUploadFail(isNetError: Boolean)
    fun wechatFriendsUploadSuccess()
    fun wechatFriendsUploadFail()
    fun getWechatFriendsUploadRecordSuccess(data: List<Int>)
    fun getWechatFriendsUploadRecordFail(isNetError: Boolean)
    fun getAgentWechatAccountInfoSuccess(data:List<AgentWechatAccountInfo>)
    fun getCustomTemplateTextRecognitionSuccess(number:String?)

}