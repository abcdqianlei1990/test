package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.goldenbeans.GoldenBeansPaymentPostBody
import cn.xzj.agent.entity.payment.PaymentRetInfo
import cn.xzj.agent.entity.payment.PaymentPostBody
import cn.xzj.agent.entity.payment.PaymentStatusInfo
import cn.xzj.agent.iview.IGoldenBeansPurchase
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GoldenBeansPurchasePresenter : BasePresenter<IGoldenBeansPurchase>() {

    fun goldenBeanPurchase(param: GoldenBeansPaymentPostBody) {
        val observer=object : QuickObserver<PaymentRetInfo>(mView.context()) {
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
        Client.getInstance(mView.context()).apiManager.goldenBeanPurchase(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getPaymentStatus(orderId:String) {
        val observer=object : QuickObserver<PaymentStatusInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<PaymentStatusInfo>?) {
                mView.onPurchaseStatusGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onPurchaseStatusGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<PaymentStatusInfo>?) {
                super.onCodeError(t)
                mView.onPurchaseStatusGetFailure()
            }
        }
        Client.getInstance(mView.context()).apiManager.getPaymentStatus(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}