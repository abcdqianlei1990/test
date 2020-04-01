package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.ScanLoginBody
import cn.xzj.agent.iview.IScanLoginView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/11/13
 * @ Des
 */
class ScanLoginPresenter : BasePresenter<IScanLoginView>() {
    fun scanLogin(scanLoginBody: ScanLoginBody) {
        val mObserver=object : QuickObserver<Any>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Any>) {
                mView.onScanLoginSuccess()
            }

            override fun onCodeError(t: BaseResponseInfo<Any>?) {
                super.onCodeError(t)
                mView.onScanLoginFial()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onScanLoginFial()
            }
        }
        Client.getInstance(mView.context()).apiManager.scanLogin(scanLoginBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}