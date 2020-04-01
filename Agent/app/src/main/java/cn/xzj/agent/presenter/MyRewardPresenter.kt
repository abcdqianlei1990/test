package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.reward.FirstRewardInfo
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.iview.IMyRewardView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyRewardPresenter : BasePresenter<IMyRewardView>() {

    fun getEntryRewardInfo(map:HashMap<String,String>) {
        val observer=object : QuickObserver<CommonListBody<HierarchicalRewardInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>?) {
                mView.onEntryRewardGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onEntryRewardGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>?) {
                super.onCodeError(t)
                mView.onEntryRewardGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getAgentReward(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getCurLvRewardInfo(map:HashMap<String,String>) {
        val observer=object : QuickObserver<CommonListBody<HierarchicalRewardInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>?) {
                mView.onCurLvRewardGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onCurLvRewardGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>?) {
                super.onCodeError(t)
                mView.onCurLvRewardGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getCurLvRewardInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getLowerLvRewardInfo(map:HashMap<String,String>) {
        val observer=object : QuickObserver<CommonListBody<HierarchicalRewardInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>?) {
                mView.onLowerLvRewardGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onLowervRewardGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<HierarchicalRewardInfo>>?) {
                super.onCodeError(t)
                mView.onLowervRewardGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getLowerLvRewardInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getFirstRewardInfo(map:HashMap<String,String>) {
        val observer=object : QuickObserver<CommonListBody<FirstRewardInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<FirstRewardInfo>>?) {
                mView.onFirstRewardGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onFirstRewardGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<FirstRewardInfo>>?) {
                super.onCodeError(t)
                mView.onFirstRewardGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getFirstRewardInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun getTotalRewardAmount(rewardType:Int,from:Long,to:Long) {
        val observer=object : QuickObserver<Double>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<Double>?) {
                mView.onTotalRewardGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onTotalRewardGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Double>?) {
                super.onCodeError(t)
                mView.onTotalRewardGetFailure()
            }

        }
        Client.getInstance(mView.context()).apiManager.getTotalRewardAmount(rewardType,from, to)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}