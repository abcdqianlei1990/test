package cn.xzj.agent.presenter

import android.app.ProgressDialog
import android.content.DialogInterface
import cn.xzj.agent.R
import cn.xzj.agent.constants.Config
import cn.xzj.agent.iview.IRealNameIdentity
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.*
import cn.xzj.agent.entity.certificate.IDCardInfo
import cn.xzj.agent.entity.certificate.IdCardOCRBody
import cn.xzj.agent.entity.certificate.RealNamePostBody
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import com.alibaba.fastjson.JSON
import com.channey.utils.StringUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

import java.util.ArrayList


class RealNameIdentityPresenter : BasePresenter<IRealNameIdentity>() {
    private var mProgressDialog: ProgressDialog? = null
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
    fun idCardOCR(fileName:String,byteArrayInputStream: ByteArray) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayInputStream)
        val appKey = RequestBody.create(
                MediaType.parse("multipart/form-data"), Config.FACE_API_KEY
        )
        val secret = RequestBody.create(
                MediaType.parse("multipart/form-data"), Config.FACE_API_SECRET
        )
        val body = MultipartBody.Part.createFormData("image", fileName, requestFile)
        val observable = apiManager.idCardOCR(appKey, secret,body)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<IDCardInfo>() {
                    override fun onComplete() {
                        closeProgressDialog()
                    }

                    override fun onSubscribe(d: Disposable) {
                        showProgressDialog("", false)
                    }

                    override fun onNext(info: IDCardInfo) {
                        closeProgressDialog()
                        mView.onOCRSuccess(info)
                    }

                    override fun onError(e: Throwable) {
                        closeProgressDialog()
                        if (e is HttpException) {
                            val response = e.response()
                            val json = response.errorBody()!!.string()
                            val res = JSON.parseObject(json, TokenResp::class.java)
                            val errorMsg = res.message
                            val msg = if (StringUtils.isEmpty(errorMsg)) mView.context().getString(R.string.connection_to_server_failed) else errorMsg
                            mView.onOCRFailure(msg)
                        } else {
                            mView.onOCRFailure(mView.context().getString(R.string.connection_to_server_failed))
                        }
                    }
                })
    }

    fun showProgressDialog(message: String, isCancelable: Boolean) {
        mProgressDialog = ProgressDialog(mView.context())
        mProgressDialog!!.setCancelable(isCancelable)
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.show()
        mProgressDialog!!.setOnCancelListener(DialogInterface.OnCancelListener { mProgressDialog!!.cancel() })
    }

    private fun closeProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
                mProgressDialog!!.cancel()
            }
        }
    }

    fun realNameCertificate(body: RealNamePostBody) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.realNameCertificate(body)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onCommitCardInfoSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onCommitCardInfoFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onCommitCardInfoFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }
}
