package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.iview.IJobDetailView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class JobDetailPresenter:BasePresenter<IJobDetailView>() {

    fun getPositionDetails(id:String,user_id:String?=null) {
        val mObserver=object : QuickObserver<JobInfo>(mView.context()){

            override fun onRequestStart() {
                super.onRequestStart()
            }

            override fun onSuccessful(t: BaseResponseInfo<JobInfo>) {
                mView!!.onDetailGetSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<JobInfo>) {
                super.onCodeError(t)
                mView!!.onDetailGetFailure(t.error.message)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView!!.onDetailGetFailure(getErrorMsg(e))
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