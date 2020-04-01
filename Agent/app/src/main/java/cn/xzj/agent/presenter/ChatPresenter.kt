package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.RobotChatInfo
import cn.xzj.agent.iview.IChatView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/19
 * @Des
 */
class ChatPresenter : BasePresenter<IChatView>() {
    fun sendProblem(userId: String, content: String) {
        val observer = object : QuickObserver<RobotChatInfo>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<RobotChatInfo>) {
                mView.onGetProblemResultSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<RobotChatInfo>?) {
                super.onCodeError(t)
                mView.onGetProblemResultFail()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onGetProblemResultFail()
            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .sendProblem(userId, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        addSubscribe(observer)
    }
}