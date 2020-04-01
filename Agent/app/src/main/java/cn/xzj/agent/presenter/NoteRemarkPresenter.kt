package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.customer.NoteRemarkBody
import cn.xzj.agent.entity.customer.NoteRemarkBodyNew
import cn.xzj.agent.entity.task.TaskCompleteDTO
import cn.xzj.agent.entity.task.TaskInfo
import cn.xzj.agent.iview.INoteRemarkView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoteRemarkPresenter : BasePresenter<INoteRemarkView>() {
    fun commitNoteRemark(body:NoteRemarkBodyNew) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.commitRemark(body)
        val mObserver = object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onRemarkSuccess(t!!.content)
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onRemarkFailure(t!!.content.toString())
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRemarkFailure("网络错误")
            }
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getRegisterTask(userId: String) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getRegisterTask(userId)
        val mObserver = object : QuickObserver<TaskInfo>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                mView.showLoading()
            }

            override fun onSuccessful(t: BaseResponseInfo<TaskInfo>) {
                //新注册任务data不为空
                mView.onGetRegisterTaskSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<TaskInfo>) {
                super.onCodeError(t)
                mView.onGetRegisterTaskFailure(t.error.toString())
            }

            override fun onRequestEnd() {
                super.onRequestEnd()
                mView.showContent()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.showError()
                mView.onGetRegisterTaskFailure(e!!.message.toString())
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
        val mObserver = object : QuickObserver<Boolean>(mView.context()) {
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
}