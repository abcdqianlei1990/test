package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.task.TaskTypeInfo
import cn.xzj.agent.iview.IHomeView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomePresenter : BasePresenter<IHomeView>() {

    /**
     * 获取所有任务类型
     */
    fun getTaskTypes(from: String?, to: String?) {
        val map = HashMap<String, String>()
        map.put("task_category", "0")
        map.put("with_count", "true")
        if (from != null) {
            map.put("from", from)
            map.put("to", to!!)
        }
        val mObserver = object : QuickObserver<List<TaskTypeInfo>>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<List<TaskTypeInfo>>?) {
                mView.onTaskTypesGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onTaskTypesGetFailure(isNetWorkError)
            }

        }
        Client.getInstance(mView.context()).apiManager.getTaskTypes(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)

    }

    fun getUnreadMsg() {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.unreadMsg
        val mObserver = object : QuickObserver<Int>(mView.context()) {
            override fun onSuccessful(t: BaseResponseInfo<Int>?) {
                mView.onUnreadMsgGetSuccess(t!!.content)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onUnreadMsgGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}