package cn.xzj.agent.presenter

import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.MsgInfo
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.core.common.mvp.BaseView
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.iview.IMsgListView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.ui.message.MsgListActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MsgListPresenter : BasePresenter<IMsgListView>() {
    fun getMsgList(page: Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getMsgList(page, EnumValue.PAGE_SIZE)
        val mObserver=object : QuickObserver<CommonListBody<MsgInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<MsgInfo>>) {
                mView.onMsgListGetSuccess(t.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onMsgListGetFailure("")
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getCustomerDetailInfo(map: Map<String, String>) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerDetailInfo(map)
        val mObserver=object : QuickObserver<CustomerDetailInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<CustomerDetailInfo>?) {
                mView.onCustomerDetailInfoGetSuccess(t!!.content)

            }

            override fun onCodeError(t: BaseResponseInfo<CustomerDetailInfo>?) {
                super.onCodeError(t)
                mView.onCustomerDetailInfoGetFailure()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onCustomerDetailInfoGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}