package cn.xzj.agent.presenter

import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.common.CaptchaPostBody
import cn.xzj.agent.entity.common.SignUpPostBody
import cn.xzj.agent.entity.store.StoreInfo
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.ui.UserRegisterActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRegisterPresenter(var activity:UserRegisterActivity?):BasePresenter() {
    override fun detachView() {
        activity = null
        unSubscribe()
    }

    fun getCaptcha(mobile:String) {
        val apiManager = Client.getInstance(activity).apiManager
        val observable = apiManager.getCaptcha(CaptchaPostBody(mobile))
        val subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :QuickObserver<Any>(activity){
                    override fun onRequestStart() {
                        super.onRequestStart()
                        activity!!.showLoading()
                    }

                    override fun onRequestEnd() {
                        super.onRequestEnd()
                        activity!!.dismissLoading()
                    }
                    override fun onSuccessful(t: BaseResponseInfo<Any>?) {
//                        activity!!.onCaptchaGetSuccess()
                    }

                    override fun onCodeError(t: BaseResponseInfo<Any>) {
                        super.onCodeError(t)
                        activity!!.onCaptchaGetFailure(t.error.message)
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                        activity!!.onCaptchaGetFailure(getErrorMsg(e))
                    }
                })
    }


    fun signUp(body:SignUpPostBody) {
        val apiManager = Client.getInstance(activity).apiManager
        val observable = apiManager.customerSignUp(body)
        val subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :QuickObserver<Any>(activity){
                    override fun onRequestStart() {
                        super.onRequestStart()
                        activity!!.showLoading()
                    }

                    override fun onRequestEnd() {
                        super.onRequestEnd()
                        activity!!.dismissLoading()
                    }
                    override fun onSuccessful(t: BaseResponseInfo<Any>?) {
                        activity!!.onSignUpSuccess()
                    }

                    override fun onCodeError(t: BaseResponseInfo<Any>) {
                        super.onCodeError(t)
                        activity!!.onSignUpFailure(t.error.message)
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                        activity!!.onSignUpFailure(getErrorMsg(e))
                    }
                })

    }

    fun getStores() {
        val apiManager = Client.getInstance(activity).apiManager
        val observable = apiManager.getStores()
        val subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :QuickObserver<List<StoreInfo>>(activity){

                    override fun onRequestStart() {
                        super.onRequestStart()
                        activity!!.showLoading()
                    }

                    override fun onRequestEnd() {
                        super.onRequestEnd()
                        activity!!.dismissLoading()
                    }
                    override fun onSuccessful(t: BaseResponseInfo<List<StoreInfo>>) {
                        activity!!.onStoresGetSuccess(t.content)
                    }

                    override fun onCodeError(t: BaseResponseInfo<List<StoreInfo>>) {
                        super.onCodeError(t)
                        activity!!.onStoresGetFailure(t.error.message)
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                        activity!!.onStoresGetFailure(getErrorMsg(e))
                    }
                })
    }
}