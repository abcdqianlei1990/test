package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.certificate.BindCardPostBody
import cn.xzj.agent.iview.IAgentProfile
import cn.xzj.agent.iview.IBindCardStep1
import cn.xzj.agent.iview.IExcessive
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BindCardStep1Presenter : BasePresenter<IBindCardStep1>() {

    fun bindCard(body:BindCardPostBody) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.bindCard(body)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onBindSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onBindFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onBindFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}