package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.QiandaoRecordInfo
import cn.xzj.agent.iview.IQiandaoRecordsView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QiandaoRecordsPresenter:BasePresenter<IQiandaoRecordsView>() {

    fun getCustomerQiandaoRecords(userId:String,pageNo:Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerQiandaoRecords(userId,pageNo,EnumValue.PAGE_SIZE)
        val mObserver=object : QuickObserver<CommonListBody<QiandaoRecordInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
            }
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<QiandaoRecordInfo>>?) {
                mView.onRecordsGetSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<QiandaoRecordInfo>>?) {
                mView.onRecordsGetFailure("")
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRecordsGetFailure(e!!.message.toString())
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}