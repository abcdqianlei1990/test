package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.FileUploadDTO
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.customer.BadgeUploadRequestBody
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo
import cn.xzj.agent.iview.IAddWorkExp
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddWorkExpPresenter : BasePresenter<IAddWorkExp>() {

    fun getWorkExpCompany(userId:String) {
        val observer=object : QuickObserver<ArrayList<WorkExperienceCompanyInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<ArrayList<WorkExperienceCompanyInfo>>?) {
                mView.onWorkExpCompanyGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onWorkExpCompanyGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<ArrayList<WorkExperienceCompanyInfo>>?) {
                super.onCodeError(t)
                mView.onWorkExpCompanyGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getWorkExpCompany(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun uploadWorkCardImg(list: ArrayList<FileUploadDTO>) {
        val apiManager = Client.getInstance(mView.context()).uploadFileApiManager
        val observable = apiManager.uploadFiles(list)
        val mObserver=object : QuickObserver<List<FileUploadVO>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<List<FileUploadVO>>?) {
                mView.uploadFileSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.uploadFileFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<List<FileUploadVO>>?) {
                super.onCodeError(t)
                mView.uploadFileFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun uploadBadge(body:BadgeUploadRequestBody) {
        val apiManager = Client.getInstance(mView.context()).uploadFileApiManager
        val observable = apiManager.uploadBadge(body)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.uploadBadgeSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.uploadBadgeFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.uploadBadgeFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}