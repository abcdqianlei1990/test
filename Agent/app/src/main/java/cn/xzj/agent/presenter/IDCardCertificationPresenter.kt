package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.customer.RealNameVerificationBody
import cn.xzj.agent.iview.IIDCardCertificationView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/17
 * @Des
 */
class IDCardCertificationPresenter : BasePresenter<IIDCardCertificationView>() {
    fun commit(realNameVerificationBody: RealNameVerificationBody) {
        val observer = object : QuickObserver<Int>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Int>?) {
//                实名认证; 返回: 1 - 通过, 0 - 不通过, 3 - 查询无结果, 2 - 用户已经实名，但是参数中信息和实名信息不一致, 4 - 身份信息被其他用户实名

                when (t!!.content) {
                    1 -> {
                        mView.onIDCardCertificationSuccess()
                    }
                    0 -> {
                        mView.onIDCardCertificationFail("不通过")
                    }
                    2 -> {
                        mView.onIDCardCertificationFail("用户已经实名，但是参数中信息和实名信息不一致")
                    }
                    3 -> {
                        mView.onIDCardCertificationFail("查询无结果")
                    }
                    4 -> {
                        mView.onIDCardCertificationFail("身份信息被其他用户实名")
                    }
                    else->
                        mView.onIDCardCertificationFail("实名失败：${t.content}")
                }
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }
        }
        Client.getInstance(mView.context()).apiManager
                .realNameVerification(realNameVerificationBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

}
