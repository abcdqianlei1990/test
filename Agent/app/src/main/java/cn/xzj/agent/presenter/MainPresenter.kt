package cn.xzj.agent.presenter

import cn.xzj.agent.constants.Config
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.common.PgyAppInfo
import cn.xzj.agent.entity.common.PgyBaseResponseInfo
import cn.xzj.agent.entity.privacy.InstalledAppUploadBody
import cn.xzj.agent.iview.IMainView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.util.LogLevel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainPresenter : BasePresenter<IMainView>() {

    fun getAgentInfo() {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.agentInfo
        val mObserver=object : QuickObserver<AgentInfo>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<AgentInfo>?) {
                mView.onAgentInfoGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onAgentInfoGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<AgentInfo>?) {
                super.onCodeError(t)
                mView.onAgentInfoGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun uploadAllInstallApp(agentUserName: String, body: InstalledAppUploadBody?) {
        Client.getInstance(mView.context()).apiManager.uploadAllInstallApp(agentUserName, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<Any>(){
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Any) {
                        LogLevel.d("uploadAllInstallApp:", t.toString())
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }

    fun getAPPInfoFromPGY() {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getAPPInfoFromPGY("xzjagent", Config.PGY_apiKey)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<PgyBaseResponseInfo<PgyAppInfo>>() {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(info: PgyBaseResponseInfo<PgyAppInfo>) {

                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

}