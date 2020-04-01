package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.FeedbackBody
import cn.xzj.agent.entity.FileUploadDTO
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.iview.IFeedbackView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.util.BitmapUtil
import cn.xzj.agent.util.CommonUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/3/7
 * @Des
 */
class FeedbackPresenter : BasePresenter<IFeedbackView>() {
    fun commit(body: FeedbackBody) {
        val observer = object : QuickObserver<Any>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Any>?) {
                mView.onFeedbackSuccess()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
            }

        }
        Client.getInstance(mView.context())
                .apiManager
                .feedback(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }

    fun imagesUpload(filePaths: List<String>) {
        val mObserver = object : QuickObserver<List<FileUploadVO>>(mView.context()) {
            @Throws(Exception::class)
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            @Throws(Exception::class)
            override fun onSuccessful(t: BaseResponseInfo<List<FileUploadVO>>) {
                mView.onImagesUploadSuccess(t.content)
            }

            @Throws(Exception::class)
            override fun onCodeError(t: BaseResponseInfo<List<FileUploadVO>>) {
                super.onCodeError(t)
                mView.onImagesUploadFail()
            }

            @Throws(Exception::class)
            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
                mView.onImagesUploadFail()
            }
        }

        Observable.create(ObservableOnSubscribe<List<File>> { emitter ->
            val mFiles = ArrayList<File>()
            for (filePath in filePaths) {
                val newFilePath = BitmapUtil.compressImage(filePath, CommonUtils.getAgentImageRootFile().path, System.currentTimeMillis().toString())
                val mFile = File(newFilePath)
                mFiles.add(mFile)
            }
            emitter.onNext(mFiles)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer2<List<File>>() {
                    override fun onNext(files: List<File>) {
                        val fileUploadDTOS = ArrayList<FileUploadDTO>()
                        val fileUploadDTO = FileUploadDTO()
                        for (file in files) {
                            val content = CommonUtils.fileToBase64(file)
                            fileUploadDTO.content = content
                            fileUploadDTO.name = file.name
                            fileUploadDTO.size = file.totalSpace.toInt()
                            fileUploadDTOS.add(fileUploadDTO)
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
}