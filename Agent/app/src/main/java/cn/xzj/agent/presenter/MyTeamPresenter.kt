package cn.xzj.agent.presenter

import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.iview.IMyTeamView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyTeamPresenter : BasePresenter<IMyTeamView>() {
    fun getAgentInfo(agentId:String) {
        val observer=object : QuickObserver<AgentInfo>(mView.context()) {
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
        Client.getInstance(mView.context()).apiManager.getAgentInfo(agentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getLowerAgents(agentId:String,pageNo:Int) {
        val observer=object : QuickObserver<CommonListBody<AgentInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<AgentInfo>>?) {
                mView.onLowerAgentsGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onLowerAgentsGetFailure()
//                mView.onLowerAgentsGetSuccess(initTestData())
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<AgentInfo>>?) {
                super.onCodeError(t)
                mView.onLowerAgentsGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getLowerAgents(agentId,pageNo, EnumValue.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    private fun initTestData():CommonListBody<AgentInfo>{
        var list = ArrayList<AgentInfo>()
        for (i in 0 until 21){
            var agent = AgentInfo()
            agent.nick = "nick$i"
            agent.name = "name$i"
            agent.phone = "13162810796"
            list.add(agent)
        }
        var body = CommonListBody<AgentInfo>(list,1,20,40,0,0)
        return body
    }
}