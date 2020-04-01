package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.JobListBody
import cn.xzj.agent.iview.IJobListView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class JobListPresenter : BasePresenter<IJobListView>() {

    fun getJobList(cityId: String?, pageNo: Int, positionListType: String?, userId: String, term: String? = null, features: ArrayList<String>? = null) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val body = JobListBody(cityId = cityId,
                pageNo = pageNo,
                pageSize = EnumValue.PAGE_SIZE,
                positionListType = positionListType,
                userId = userId, term = term, features = features)
        val observable = apiManager.getJobList(body)
        val mObserver = object : QuickObserver<CommonListBody<JobInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog(true)
            }

            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<JobInfo>>) {
                mView.onJobListGetSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<JobInfo>>?) {
                super.onCodeError(t)
                mView.onJobListGetFailure()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onJobListGetFailure()
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getCities() {
        val mObserver = object : QuickObserver<List<CityInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<List<CityInfo>>?) {
                mView.onCityListGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }

        }
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.cities
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun positionRequestPermissionCheck(userId: String, position: Int) {
        val map = HashMap<String, String>()
        map.put("user_id", userId)
        map.put("position_type", "ZX")
        val mObserver = object : QuickObserver<PositionRequestPermissionInfo>(mView.context()) {

            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog(true)
            }

            override fun onSuccessful(t: BaseResponseInfo<PositionRequestPermissionInfo>) {
                mView.onPermissionGetSuccess(t.content, position)
            }

            override fun onCodeError(t: BaseResponseInfo<PositionRequestPermissionInfo>?) {
                super.onCodeError(t)

            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }
        }
        Client.getInstance(mView.context()).apiManager.positionRequestPermissionCheck(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getJobFeatures() {
        val mObserver = object : QuickObserver<List<JobFeature>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog(true)
            }

            override fun onSuccessful(t: BaseResponseInfo<List<JobFeature>>?) {
                mView.onGetJobFeaturesSuceess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onGetJobFeaturesFailure()
            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .jobFeatures
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}