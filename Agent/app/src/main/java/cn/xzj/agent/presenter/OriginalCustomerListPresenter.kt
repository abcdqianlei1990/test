package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.OriginalCustomerInfo
import cn.xzj.agent.iview.IOriginalCustomerListView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OriginalCustomerListPresenter : BasePresenter<IOriginalCustomerListView>() {

    fun getOriginalCustomers(queryMap: HashMap<String, String>) {
        queryMap.put("page_size", EnumValue.PAGE_SIZE)
        queryMap.put("self", "true")
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getOriginalCustomers(queryMap)
        val mObserver=object : QuickObserver<CommonListBody<OriginalCustomerInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
            }

            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<OriginalCustomerInfo>>?) {
                mView.onCustomersGetSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<OriginalCustomerInfo>>?) {
                super.onCodeError(t)
                mView.onCustomersGetFailure()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onCustomersGetFailure()
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getCustomerDetailInfo(map: Map<String, String>, number: String, action: Int, position: Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerDetailInfo(map)
        val mObserver=object : QuickObserver<CustomerDetailInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<CustomerDetailInfo>?) {
                mView.onCustomerDetailInfoGetSuccess(t!!.content, number, action, position)

            }

            override fun onCodeError(t: BaseResponseInfo<CustomerDetailInfo>?) {
                super.onCodeError(t)
                mView.onCustomerDetailInfoGetFailure()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onCustomerDetailInfoGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}