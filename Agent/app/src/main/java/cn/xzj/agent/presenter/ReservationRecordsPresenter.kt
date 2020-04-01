package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.YuyueCancelBody
import cn.xzj.agent.entity.customer.YuyueRecordInfo
import cn.xzj.agent.iview.IYuyueRecordsView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReservationRecordsPresenter:BasePresenter<IYuyueRecordsView>() {

    fun getCustomerYuyueRecords(userId:String,pageNo:Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerYuyueRecords(userId,pageNo,EnumValue.PAGE_SIZE)
        val mObserver=object :QuickObserver<CommonListBody<YuyueRecordInfo>>(mView.context()){
            override fun onRequestStart() {
                super.onRequestStart()
            }
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<YuyueRecordInfo>>) {
                mView.onRecordsGetSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<YuyueRecordInfo>>?) {
                super.onCodeError(t)
                mView.onRecordsGetFailure(t!!.error.message)
            }

            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                mView.onRecordsGetFailure(e.message.toString())
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun yuyueCancel(body: YuyueCancelBody) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.yuyueCancel(body)
        val mObserver=object :QuickObserver<Boolean>(mView.context()){
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.cancelSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.cancelFailure("")
            }

            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                mView.cancelFailure(e.message.toString())
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}