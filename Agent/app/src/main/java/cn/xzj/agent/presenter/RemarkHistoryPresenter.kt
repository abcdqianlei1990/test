package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.RemarkHistoryInfo
import cn.xzj.agent.iview.IRemarkHistoryView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RemarkHistoryPresenter : BasePresenter<IRemarkHistoryView>() {

    fun getHistories(userId: String, page: Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getHistories(userId, page, EnumValue.PAGE_SIZE)
        val mObserver=object : QuickObserver<CommonListBody<RemarkHistoryInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<RemarkHistoryInfo>>?) {
                mView.onHistoryGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onHistoryGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<RemarkHistoryInfo>>?) {
                super.onCodeError(t)
                mView.onHistoryGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}