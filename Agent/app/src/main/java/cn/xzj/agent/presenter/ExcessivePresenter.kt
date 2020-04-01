package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.iview.IExcessive
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExcessivePresenter : BasePresenter<IExcessive>() {

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