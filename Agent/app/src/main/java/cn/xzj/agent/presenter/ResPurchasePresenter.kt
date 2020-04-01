package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customerres.PurchasbleCustomerResResp
import cn.xzj.agent.entity.customerres.PurchasbleResInfo
import cn.xzj.agent.entity.customerres.ResPurchasePostBody
import cn.xzj.agent.entity.customerres.ResPurchaseResp
import cn.xzj.agent.iview.IResPurchase
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResPurchasePresenter : BasePresenter<IResPurchase>() {

    fun getPurchasbleCustomerRes(page:Int) {
        val observer=object : QuickObserver<PurchasbleCustomerResResp>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<PurchasbleCustomerResResp>?) {
                mView.onPurchasbleResGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onPurchasbleResGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<PurchasbleCustomerResResp>?) {
                super.onCodeError(t)
                mView.onPurchasbleResGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getPurchasbleCustomerRes(page,EnumValue.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getGoldenBeansCount() {
        val observer=object : QuickObserver<Int>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<Int>?) {
                mView.onGoldenBeansCountGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onGoldenBeansCountGetFail()
            }

            override fun onCodeError(t: BaseResponseInfo<Int>?) {
                super.onCodeError(t)
                mView.onGoldenBeansCountGetFail()
            }

        }
        Client.getInstance(mView.context()).apiManager.goldenBeansCount
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun customerResPurchase(users:ArrayList<String>) {
        val observer=object : QuickObserver<ResPurchaseResp>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<ResPurchaseResp>?) {
                mView.onResPurchaseSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onResPurchaseFail()
            }

            override fun onCodeError(t: BaseResponseInfo<ResPurchaseResp>?) {
                super.onCodeError(t)
                mView.onResPurchaseFail()
            }

        }
        Client.getInstance(mView.context()).apiManager.customerResPurchase(ResPurchasePostBody(users))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

}