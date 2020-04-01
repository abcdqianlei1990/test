package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.newyearreservation.NewYearReservation
import cn.xzj.agent.iview.INewYearReserveView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/28
 * @Des
 */
class NewYearReservePresenter : BasePresenter<INewYearReserveView>() {
    fun getNewYearReservation(pageNo: Int,agentId:String,term:String?) {
        var observer=object : QuickObserver<CommonListBody<NewYearReservation>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<NewYearReservation>>) {
                mView.onGetNewYearReservationSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<NewYearReservation>>?) {
                super.onCodeError(t)
                mView.onGetNewYearReservationFail()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onGetNewYearReservationFail()
            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .getNewYearReservation(pageNo, EnumValue.PAGE_SIZE.toInt(), agentId,term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
    fun getCustomerDetailInfo(map: Map<String, String>, number: String, action: Int, position: Int) {
        val observer = object : QuickObserver<CustomerDetailInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<CustomerDetailInfo>?) {
                mView.onCustomerDetailInfoGetSuccess(t!!.content, number, action, position)

            }

            override fun onCodeError(t: BaseResponseInfo<CustomerDetailInfo>?) {
                super.onCodeError(t)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }

        }
        Client.getInstance(mView.context()).apiManager.getCustomerDetailInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

}