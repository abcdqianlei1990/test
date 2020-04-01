package cn.xzj.agent.presenter

import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.company.CompanyJobDTO
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.RewardConditionInfo
import cn.xzj.agent.iview.IJobListView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CompanyJobPresenter : BasePresenter<IJobListView>() {

    fun getJobList(cityId: String?, pageNo: Int, term: String? = null,features: ArrayList<String>?=null) {
        var observer=object : QuickObserver<CommonListBody<JobInfo>>(mView.context()) {
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
//        val body = JobListBody(cityId = cityId,
//                pageNo = pageNo,
//                pageSize = EnumValue.PAGE_SIZE,
//                positionListType = positionListType,
//                userId = userId, positionName = term,features = features)
        val companyJobDTO=CompanyJobDTO()
        companyJobDTO.cityId=cityId
        companyJobDTO.pageNo=pageNo
        companyJobDTO.pageSize= EnumValue.PAGE_SIZE.toInt()
        companyJobDTO.features=features
        companyJobDTO.companyShortName=term
        Client.getInstance(mView.context()).apiManager.getCompanyJobList(companyJobDTO).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getCities() {
        var observer=object : QuickObserver<List<CityInfo>>(mView.context()) {
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
        val observable = apiManager.companyCityList
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }


    fun getJobFeatures() {
        val observer=object : QuickObserver<List<JobFeature>>(mView.context()) {
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
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getRewardCondition(recruitId:String) {
        val observer=object : QuickObserver<List<RewardConditionInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog(true)
            }
            override fun onSuccessful(t: BaseResponseInfo<List<RewardConditionInfo>>?) {
                mView.onRewardConditionGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRewardConditionGetFailure()
            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .getRewardCondition(recruitId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}