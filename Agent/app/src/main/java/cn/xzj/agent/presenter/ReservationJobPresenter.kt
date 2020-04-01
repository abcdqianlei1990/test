package cn.xzj.agent.presenter

import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.job.PickLocationVO
import cn.xzj.agent.entity.job.ReservationDTO
import cn.xzj.agent.iview.IReservationJobView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/16
 * @ Des 预报名
 */
class ReservationJobPresenter(var iReservationJobView: IReservationJobView) : BasePresenter() {
    override fun detachView() {
        super.detachView()
        unSubscribe()
    }


    /**
     * 获取预约时间
     */
    fun getInterviewDate(positionId: String, pick_up_address: String) {
        val apiManager = Client.getInstance(iReservationJobView.getContext()).apiManager
        val observable = apiManager.getReservationDate(positionId, pick_up_address)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : QuickObserver<List<Long>>(iReservationJobView.getContext()) {
                    override fun onRequestStart() {
                        super.onRequestStart()
                        showProgressDialog(true)
                    }

                    override fun onSuccessful(t: BaseResponseInfo<List<Long>>?) {
                        iReservationJobView.getReservationDate(t!!)
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    }
                })
    }

    /**
     * 获取接站地址
     *
     */
    fun getReservationLocations(positionId: String) {
        val apiManager = Client.getInstance(iReservationJobView.getContext()).apiManager
        val observable = apiManager.getReservationLocations(positionId)
        val subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : QuickObserver<List<PickLocationVO>>(iReservationJobView.getContext()) {
                    override fun onRequestStart() {
                        super.onRequestStart()
                        showProgressDialog(true)
                    }

                    override fun onSuccessful(t: BaseResponseInfo<List<PickLocationVO>>?) {
                        iReservationJobView.getReservationLocations(t!!)
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    }
                })
    }

    fun commitJobReservation(reservationDTO: ReservationDTO) {
        val apiManager = Client.getInstance(iReservationJobView.getContext()).apiManager
        val observable = apiManager.jobReservation(reservationDTO)
        val mObserver=object : QuickObserver<Any>(iReservationJobView.getContext()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<Any>?) {
                iReservationJobView.jobReservationSuccess(t!!)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                iReservationJobView.jobReservationError(e, isNetWorkError)
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
    }
}