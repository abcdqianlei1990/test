package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.payment.PaymentRetInfo
import cn.xzj.agent.entity.payment.PaymentPostBody
import cn.xzj.agent.iview.IPartTimeAgent
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PartTimeAgentPurchasePresenter : BasePresenter<IPartTimeAgent>() {

    fun partTimeAgentPurchase(param: PaymentPostBody) {
        val observer = object : QuickObserver<PaymentRetInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<PaymentRetInfo>?) {
                mView.onPurchaseSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onPurchaseFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<PaymentRetInfo>?) {
                super.onCodeError(t)
                mView.onPurchaseFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.partTimeAgentPurchase(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    fun getAgentInfo() {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.agentInfo
        val mObserver=object : QuickObserver<AgentInfo>(mView.context()) {

            override fun onSuccessful(t: BaseResponseInfo<AgentInfo>?) {
                mView.onAgentProfileInfoGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onAgentProfileInfoGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<AgentInfo>?) {
                super.onCodeError(t)
                mView.onAgentProfileInfoGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}