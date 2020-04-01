package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customerres.*
import cn.xzj.agent.iview.IResPurchaseRecord
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResPurchaseRecordPresenter : BasePresenter<IResPurchaseRecord>() {

    fun getResPurchaseRecords(page:Int) {
        val observer=object : QuickObserver<CommonListBody<ResPurchaseRecordInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<ResPurchaseRecordInfo>>?) {
                mView.onRecordsGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRecordsGetFail()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<ResPurchaseRecordInfo>>?) {
                super.onCodeError(t)
                mView.onRecordsGetFail()
            }

        }
        Client.getInstance(mView.context()).apiManager.getResPurchaseRecords(page,EnumValue.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

}