package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.core_customer.CoreCustomerInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.iview.ICoreCustomerView
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
class CoreCustomerPresenter : BasePresenter<ICoreCustomerView>() {
    fun getCoreCustomers(pageNo: Int = 1, sort: String? = null, status: Int? = null,wish:String?=null,notContacted:Int?=null, term: String? = null) {
        val mapQuery = HashMap<String, Any>()
        if (term != null)
            mapQuery["user_name"] = term
        if (status != null)
            mapQuery["status"] = status
        if (wish != null)
            mapQuery["wish"] = wish
        if (sort != null)
            mapQuery["sort"] = sort
        if (notContacted!=null)
            mapQuery["not_contacted"] = notContacted
        mapQuery["page_no"] = pageNo
        mapQuery["page_size"] = EnumValue.PAGE_SIZE
        mapQuery["order"] = 1//顺序: 0 - 升序, 1 - 降序

        val observer = object : QuickObserver<CommonListBody<CoreCustomerInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<CoreCustomerInfo>>?) {
                mView.getCoreCustomersSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<CoreCustomerInfo>>?) {
                super.onCodeError(t)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.getCoreCustomersFailure(isNetWorkError)
            }
        }
        Client.getInstance(mView.context()).apiManager.getCoreCustomers(mapQuery)
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