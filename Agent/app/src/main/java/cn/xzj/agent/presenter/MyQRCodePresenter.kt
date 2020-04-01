package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.common.QrCodeTemplateInfo
import cn.xzj.agent.iview.IMyQRCode
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyQRCodePresenter : BasePresenter<IMyQRCode>() {

    fun getQRCodeTemplates() {
        val observer=object : QuickObserver<ArrayList<QrCodeTemplateInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<ArrayList<QrCodeTemplateInfo>>?) {
                mView.onQRCodeTemplateGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onQRCodeTemplateGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<ArrayList<QrCodeTemplateInfo>>?) {
                super.onCodeError(t)
                mView.onQRCodeTemplateGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.qrCodeTemplate
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}