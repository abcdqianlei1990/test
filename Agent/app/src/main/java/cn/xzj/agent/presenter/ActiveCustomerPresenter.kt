package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.active_customer.ActiveCustomerInfo
import cn.xzj.agent.entity.active_customer.CustomerActiveEventInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.iview.IActiveCustomerView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/27
 * @Des
 */
class ActiveCustomerPresenter : BasePresenter<IActiveCustomerView>() {
    fun getActiveCustomers(pageNo: Int = 1, sort: String? = null,order:String?=null,wish:String?=null, notContacted: Int? = null, term: String? = null) {
        val mapQuery = HashMap<String, Any>()
        if (term != null)
            mapQuery["term"] = term
        if (notContacted != null)
            mapQuery["not_contacted"] = notContacted
        if (sort != null)
            mapQuery["sort"] = sort
        if (wish != null)
            mapQuery["wish"] = wish
        if (order!=null)
            mapQuery["order"] = order
        mapQuery["page_no"] = pageNo
        mapQuery["page_size"] = EnumValue.PAGE_SIZE


        val observer = object : QuickObserver<CommonListBody<ActiveCustomerInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<ActiveCustomerInfo>>?) {
                mView.getActiveCustomersSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<ActiveCustomerInfo>>?) {
                super.onCodeError(t)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.getActiveCustomersFailure(isNetWorkError)
            }
        }
        Client.getInstance(mView.context()).apiManager.getActiveCustomers(mapQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getCustomerAvtiveEvents(userId: String) {
        val observer = object : QuickObserver<List<CustomerActiveEventInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog(true)
            }

            override fun onSuccessful(t: BaseResponseInfo<List<CustomerActiveEventInfo>>?) {
                mView.getCustomerActiveEventsSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }
        }
        Client.getInstance(mView.context()).apiManager.getCustomerActiveEvents(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
    fun getCustomerDetailInfo(userId: String) {
        val map = HashMap<String, String>()
        map["user_id"] = userId
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerDetailInfo(map)
        val mObserver = object : QuickObserver<CustomerDetailInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<CustomerDetailInfo>?) {
                mView.onCustomerDetailInfoGetSuccess(t!!.content,userId)

            }

            override fun onCodeError(t: BaseResponseInfo<CustomerDetailInfo>?) {
                super.onCodeError(t)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}