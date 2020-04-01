package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.RequestCancelBody
import cn.xzj.agent.entity.customer.RequestRecordInfo
import cn.xzj.agent.entity.customer.RequestRecordsBodySimple
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.iview.IRequestRecordsView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RequestRecordsPresenter:BasePresenter<IRequestRecordsView>() {

    fun getCustomerRequestRecords(body: RequestRecordsBodySimple) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerRequestRecords(body)
        val mObserver=object :QuickObserver<CommonListBody<RequestRecordInfo>>(mView.context()){
            override fun onRequestStart() {
                super.onRequestStart()
            }
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<RequestRecordInfo>>?) {
                mView.onRecordsGetSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<RequestRecordInfo>>?) {
                mView.onRecordsGetFailure("")
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

    fun cancelRequest(body: RequestCancelBody) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.requestCancel(body)
        val mObserver=object :QuickObserver<Boolean>(mView.context()){
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }

            override fun onSuccessful(t: BaseResponseInfo<Boolean>) {
                mView.cancelSuccess(t.content)
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getPositionDetails(id:String,user_id:String?=null,position:Int) {
        val mObserver=object : QuickObserver<JobInfo>(mView.context()){

            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<JobInfo>) {
                mView!!.onJobDetailGetSuccess(t.content,position)
            }

            override fun onCodeError(t: BaseResponseInfo<JobInfo>) {
                super.onCodeError(t)
            }
            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }
        }
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getPositionDetails(id,user_id)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}