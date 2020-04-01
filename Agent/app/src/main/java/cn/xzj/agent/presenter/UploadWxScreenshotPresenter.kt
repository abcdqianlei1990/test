package cn.xzj.agent.presenter

import android.app.ProgressDialog
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.*
import cn.xzj.agent.entity.baidu.BaiDuORCResoult
import cn.xzj.agent.iview.IUploadWxScreenshotView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.util.BitmapUtil
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.LogLevel
import cn.xzj.agent.util.RecognizeService
import com.alibaba.fastjson.JSON
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/22
 * @ Des
 */
class UploadWxScreenshotPresenter : BasePresenter<IUploadWxScreenshotView>() {
    fun getWechatFriendsUploadRecord(month: Long) {
        val mObserver = object : QuickObserver<List<Int>>(mView.context()) {
            @Throws(Exception::class)
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            @Throws(Exception::class)
            override fun onSuccessful(t: BaseResponseInfo<List<Int>>) {
                mView.getWechatFriendsUploadRecordSuccess(t.content)

            }

            @Throws(Exception::class)
            override fun onCodeError(t: BaseResponseInfo<List<Int>>) {
                super.onCodeError(t)
                mView.getWechatFriendsUploadRecordFail(false)

            }

            @Throws(Exception::class)
            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                mView.getWechatFriendsUploadRecordFail(isNetWorkError)
            }
        }
        Client.getInstance(mView.context()).apiManager.getWechatFriendsUploadRecord(month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun wechatFriendsUpload(body: WechatFriendsUploadDTO?) {
        val mObserver = object : QuickObserver<Any>(mView.context()) {
            @Throws(Exception::class)
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            @Throws(Exception::class)
            override fun onSuccessful(t: BaseResponseInfo<Any>) {
                mView.wechatFriendsUploadSuccess()
            }

            @Throws(Exception::class)
            override fun onCodeError(t: BaseResponseInfo<Any>) {
                super.onCodeError(t)
                mView.wechatFriendsUploadFail()
            }

            @Throws(Exception::class)
            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                mView.wechatFriendsUploadFail()
            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .wechatFriendsUpload(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun wxScreenshotUpload(filePath: String) {
        //识别截图中好友数
        getCustomTemplateTextRecognition(filePath)
        Observable.create(ObservableOnSubscribe<File> { emitter ->
            val newFilePath = BitmapUtil.compressImage(filePath, CommonUtils.getAgentImageRootFile().path, System.currentTimeMillis().toString())
            val mFile = File(newFilePath)
            emitter.onNext(mFile)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<File>() {

                    override fun onNext(file: File) {
                        val fileUploadDTOS = ArrayList<FileUploadDTO>()
                        val fileUploadDTO = FileUploadDTO()
                        val content = CommonUtils.fileToBase64(file)
                        fileUploadDTO.content = content
                        fileUploadDTO.name = file.name
                        fileUploadDTO.size = file.totalSpace.toInt()
                        fileUploadDTOS.add(fileUploadDTO)

                        val mObserver = object : QuickObserver<List<FileUploadVO>>(mView.context()) {
                            @Throws(Exception::class)
                            override fun onRequestStart() {
                                super.onRequestStart()
                                showProgressDialog()
                            }

                            @Throws(Exception::class)
                            override fun onSuccessful(t: BaseResponseInfo<List<FileUploadVO>>) {
                                mView.wxScreenshotUploadSuccess(t.content)
                            }

                            @Throws(Exception::class)
                            override fun onCodeError(t: BaseResponseInfo<List<FileUploadVO>>) {
                                super.onCodeError(t)
                                mView.wxScreenshotUploadFail(false)
                            }

                            @Throws(Exception::class)
                            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                                mView.wxScreenshotUploadFail(isNetWorkError)
                            }
                        }
                        Client.getInstance(mView.context())
                                .uploadFileApiManager.uploadFiles(fileUploadDTOS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(mObserver)
                        addSubscribe(mObserver)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

    }

    fun getAgentWechatAccountList() {
        var observer = object : QuickObserver<List<AgentWechatAccountInfo>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<List<AgentWechatAccountInfo>>?) {
                mView.getAgentWechatAccountInfoSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }
        }
        Client.getInstance(mView.context()).apiManager.agentWechatAccountList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    /**
     * 识别微信截图好友数
     */
    fun getCustomTemplateTextRecognition(filePath: String) {
        var mProgressDialog: ProgressDialog? = ProgressDialog(mView.context())
        mProgressDialog!!.setCancelable(false)
        mProgressDialog.setMessage("正在识别微信截图中的微信好友数，请稍后")
        mProgressDialog.show()
        RecognizeService.recGeneral(mView.context(), filePath
        ) {
            var number: String? = null
            try {
                val mORCResult = JSON.parseObject(it, BaiDuORCResoult::class.java)
                var mFriendNumberStr=""
                for (item in mORCResult.words_result){
                    if (item.words.endsWith("位联系人")){
                        mFriendNumberStr=item.words
                        break
                    }
                }
                LogLevel.d("word", mFriendNumberStr)
                number = CommonUtils.getNumbers(mFriendNumberStr)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mView.getCustomTemplateTextRecognitionSuccess(number)
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }


}
