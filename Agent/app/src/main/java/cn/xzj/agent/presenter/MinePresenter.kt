package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.share.WechatHongbaoShareInfo
import cn.xzj.agent.iview.IMineView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/14
 * @Des
 */
class MinePresenter : BasePresenter<IMineView>() {
    fun getAgentInfo() {
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
        Client.getInstance(mView.context()).apiManager.agentInfo
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getGoldenBeansCount() {
        val observer=object : QuickObserver<Int>(mView.context()) {
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

    fun getWechatHongbaoInfo() {
        val observer= object : QuickObserver<WechatHongbaoShareInfo>(mView.context()) {
                    override fun onRequestStart() {
                        super.onRequestStart()
                        showProgressDialog(true)
                    }
                    override fun onSuccessful(t: BaseResponseInfo<WechatHongbaoShareInfo>?) {
                        mView.onGetWechatHongbaoSuccess(t!!.content)
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                        mView.onGetWechatHongbaoFail()
                    }

                    override fun onCodeError(t: BaseResponseInfo<WechatHongbaoShareInfo>?) {
                        super.onCodeError(t)
                        mView.onGetWechatHongbaoFail()
                    }

                }
        Client.getInstance(mView.context())
                .uploadFileApiManager
                .weiXinHongBaoShareMessage
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}