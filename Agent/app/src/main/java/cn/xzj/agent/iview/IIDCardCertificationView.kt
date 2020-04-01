package cn.xzj.agent.iview

import cn.xzj.agent.core.common.mvp.BaseView

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/17
 * @Des
 */
interface IIDCardCertificationView : BaseView {
    fun onIDCardCertificationSuccess()
    fun onIDCardCertificationFail(content: String)
}