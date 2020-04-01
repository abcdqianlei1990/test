package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.RemarkHistoryInfo
import cn.xzj.agent.entity.customer.RemarkVO
import cn.xzj.agent.entity.customer.WorkingRecordInfo
import cn.xzj.agent.iview.ICustomerDetailView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class CustomerDetailPresenter : BasePresenter<ICustomerDetailView>() {

    fun getRemarkInfo(userId: String) {
        val observer = object : QuickObserver<RemarkVO>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<RemarkVO>) {
                mView.onRemarkGetSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<RemarkVO>?) {
                super.onCodeError(t)
                mView.onRemarkGetFailure(t!!.error.message)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRemarkGetFailure(e!!.message.toString())
            }
        }
        Client.getInstance(mView.context()).apiManager.getUserInfoRemark(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getCustomerDetailInfo(userId: String) {
        val map = HashMap<String, String>()
        map.put("user_id", userId)
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerDetailInfo(map)
        val mObserver = object : QuickObserver<CustomerDetailInfo>(mView.context()) {

            override fun onSuccessful(t: BaseResponseInfo<CustomerDetailInfo>?) {
                mView.onCustomerDetailInfoGetSuccess(t!!.content)

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

    fun getWorkingRecords(userId:String,page:Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getWorkingRecords(userId,page,EnumValue.PAGE_SIZE)
        val mObserver=object : QuickObserver<CommonListBody<WorkingRecordInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<WorkingRecordInfo>>?) {
                mView.onWorkingRecordsGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onWorkingRecordsGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<WorkingRecordInfo>>?) {
                super.onCodeError(t)
                mView.onWorkingRecordsGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun deleteWorkingRecord(userId:String,id:String) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.deleteWorkingRecord(userId,id)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onDeleteWorkingRecordSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onDeleteWorkingRecordFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onDeleteWorkingRecordFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}