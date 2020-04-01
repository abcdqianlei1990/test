package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.JiezhanCancelBody
import cn.xzj.agent.entity.customer.JiezhanRecordInfo
import cn.xzj.agent.iview.IMeetStationRecordsView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class JiezhanRecordsPresenter:BasePresenter<IMeetStationRecordsView>() {

    fun getCustomerJiezhanRecords(userId:String,pageNo:Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val mObserver=object :QuickObserver<CommonListBody<JiezhanRecordInfo>>(mView.context()){
            override fun onRequestStart() {
                super.onRequestStart()
            }
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<JiezhanRecordInfo>>?) {
                mView.onRecordsGetSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<JiezhanRecordInfo>>?) {
                super.onCodeError(t)
                mView.onRecordsGetFailure("")
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRecordsGetFailure(e!!.message.toString())
            }

        }
        val observable = apiManager.getCustomerJiezhanRecords(userId,pageNo,EnumValue.PAGE_SIZE)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun jiezhanCancel(body: JiezhanCancelBody) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.jiezhanCancel(body)
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

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.cancelFailure("")
            }

        }
         observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}