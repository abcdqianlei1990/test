package cn.xzj.agent.presenter

import android.app.ProgressDialog
import android.content.DialogInterface
import cn.xzj.agent.R
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.Token
import cn.xzj.agent.entity.TokenResp
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.iview.ILoginView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import com.alibaba.fastjson.JSON
import com.channey.utils.StringUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class LoginPresenter : BasePresenter<ILoginView>() {
    private var mProgressDialog: ProgressDialog? = null
    fun login(phone: String, code: String) {
        val apiManager = Client.getInstance(mView.context()).authApiManager
        val observable = apiManager.phoneLogin(phone, code)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<Token>() {
                    override fun onComplete() {
                        closeProgressDialog()
                    }

                    override fun onSubscribe(d: Disposable) {
                        showProgressDialog("", false)
                    }

                    override fun onNext(info: Token) {
                        closeProgressDialog()
                        if (info.error == null)
                            mView.onLoginSuccess(info)
                        else
                            mView.onLoginFailure(info.error.message)
                    }

                    override fun onError(e: Throwable) {
                        closeProgressDialog()
                        if (e is HttpException) {
                            val response = e.response()
                            val json = response.errorBody()!!.string()
                            val res = JSON.parseObject(json, TokenResp::class.java)
                            val errorMsg = res.message
                            val msg = if (StringUtils.isEmpty(errorMsg)) mView.context().getString(R.string.connection_to_server_failed) else errorMsg
                            mView.onLoginFailure(msg)
                        } else {
                            mView.onLoginFailure(mView.context().getString(R.string.connection_to_server_failed))
                        }
                    }
                })
    }

    fun getVerificationCode(phone: String) {
        val apiManager = Client.getInstance(mView.context()).authApiManager
        val observable = apiManager.getVerificationCode(phone)
        val mObserver = object : QuickObserver<Any>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Any>?) {
                mView.onVerificationCodeGetSuccess()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getAgentInfo() {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.agentInfo
        val mObserver = object : QuickObserver<AgentInfo>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<AgentInfo>?) {
                mView.onAgentInfoGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onAgentInfoGetFailure()
            }

            override fun onCodeError(t: BaseResponseInfo<AgentInfo>?) {
                super.onCodeError(t)
                mView.onAgentInfoGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
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
}