package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.goldenbeans.GoldenBeansChangeRecordInfo
import cn.xzj.agent.iview.IGoldenBeansChangeRecord
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GoldenBeansChangeRecordPresenter : BasePresenter<IGoldenBeansChangeRecord>() {

    fun getGoldenBeansChangeRecords(page:Int) {
        val observer=object : QuickObserver<CommonListBody<GoldenBeansChangeRecordInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<GoldenBeansChangeRecordInfo>>?) {
                mView.onRecordsGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRecordsGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<GoldenBeansChangeRecordInfo>>?) {
                super.onCodeError(t)
                mView.onRecordsGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getGoldenBeansChangeRecords(page,EnumValue.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

}