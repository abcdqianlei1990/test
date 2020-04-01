package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.agentinfo.NoticeSettingBody
import cn.xzj.agent.entity.customer.NoteRemarkBody
import cn.xzj.agent.iview.INoticeSettingView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoticeSettingPresenter : BasePresenter<INoticeSettingView>() {

    fun addNotice(body: NoticeSettingBody) {

        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.noticeSetting(body)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : QuickObserver<Boolean>(mView.context()) {
                    override fun onRequestStart() {
                        super.onRequestStart()
                        showProgressDialog()
                    }

                    override fun onSuccessful(t: BaseResponseInfo<Boolean>) {
                        mView.onSettingSuccess(t.content)
                    }

                    override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                        super.onCodeError(t)
                        mView.onSettingFailure()
                    }

                    override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                        mView.onSettingFailure()
                    }
                })
    }

    fun commitNoteRemark(comment: String, method: Int, userId: String) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.commitRemark(NoteRemarkBody(comment, method, userId))
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
}