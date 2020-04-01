package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.task.*
import cn.xzj.agent.iview.ITaskListView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.widget.SimpleToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskListPresenter : BasePresenter<ITaskListView>() {


    fun getTasks(requestBody: TasksRequestBody2) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getTasks(requestBody)
        val mObserver=object : QuickObserver<CommonListBody<TaskInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<CommonListBody<TaskInfo>>?) {
                mView.onTasksGetSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<CommonListBody<TaskInfo>>?) {
                super.onCodeError(t)
                mView.onTasksGetFailure("")
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onTasksGetFailure("")
            }

        }
         observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }


    fun makeTaskDone(requestBody: TaskCompleteDTO) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.makeTaskDone(requestBody)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.makeTaskDoneSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.makeTaskDoneFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.makeTaskDoneFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getCustomerDetailInfo(map: Map<String, String>, number: String, action: Int) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getCustomerDetailInfo(map)
        val mObserver=object :QuickObserver<CustomerDetailInfo>(mView.context()){
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<CustomerDetailInfo>?) {
                mView.onCustomerDetailInfoGetSuccess(t!!.content, number, action)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onCustomerDetailInfoGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<CustomerDetailInfo>?) {
                super.onCodeError(t)
                mView.onCustomerDetailInfoGetFailure()
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}