package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.FileUploadDTO
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.certificate.FaceVerifyPostBody
import cn.xzj.agent.iview.IAgentProfile
import cn.xzj.agent.iview.IExcessive
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class AgentProfilePresenter : BasePresenter<IAgentProfile>() {

    fun getAgentInfo() {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.agentInfo
        val mObserver=object : QuickObserver<AgentInfo>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<AgentInfo>?) {
                mView.onAgentProfileInfoGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onAgentProfileInfoGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<AgentInfo>?) {
                super.onCodeError(t)
                mView.onAgentProfileInfoGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun uploadFile(list: ArrayList<FileUploadDTO>) {
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

    fun uploadDelta(list: ArrayList<FileUploadDTO>) {
        val apiManager = Client.getInstance(mView.context()).uploadFileApiManager
        val observable = apiManager.uploadFiles(list)
        val mObserver=object : QuickObserver<List<FileUploadVO>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<List<FileUploadVO>>?) {
                mView.onDeltaUploadSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onDeltaUploadFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<List<FileUploadVO>>?) {
                super.onCodeError(t)
                mView.onDeltaUploadFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun faceVerify(body: FaceVerifyPostBody) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.faceVerify(body)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }
            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onFaceVerifySuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onFaceVerifyFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onFaceVerifyFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}