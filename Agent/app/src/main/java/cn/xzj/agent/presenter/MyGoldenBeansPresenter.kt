package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.goldenbeans.GoldenBeansProductListInfo
import cn.xzj.agent.iview.IMyGoldenBeans
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyGoldenBeansPresenter : BasePresenter<IMyGoldenBeans>() {

    fun getGoldenBeansProductList() {
        val observer=object : QuickObserver<GoldenBeansProductListInfo>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<GoldenBeansProductListInfo>?) {
                mView.onProductListGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onProductListGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<GoldenBeansProductListInfo>?) {
                super.onCodeError(t)
                mView.onProductListGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.goldenBeansProductList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getGoldenBeansCount() {
        val observer=object : QuickObserver<Int>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<Int>?) {
                mView.onGoldenBeansCountGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onGoldenBeansCountGetFail()
            }

            override fun onCodeError(t: BaseResponseInfo<Int>?) {
                super.onCodeError(t)
                mView.onGoldenBeansCountGetFail()
            }

        }
        Client.getInstance(mView.context()).apiManager.goldenBeansCount
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}