package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.customer.WorkingRecordPostBody
import cn.xzj.agent.iview.IAddWorkingRecordView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddWorkingRecordPresenter : BasePresenter<IAddWorkingRecordView>() {

    fun addWorkingRecord(body:WorkingRecordPostBody) {
        val observer=object : QuickObserver<Boolean>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onAddWorkingRecordSuccess()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onAddWorkingRecordFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onAddWorkingRecordFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.addWorkingRecord(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}