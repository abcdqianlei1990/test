package cn.xzj.agent.presenter


import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.job.JobSearchSuggestion
import cn.xzj.agent.iview.IJobSearchView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
class JobSearchPresenter : BasePresenter<IJobSearchView>() {

    fun getSearchSuggestions(term: String) {
        val mObserver=object : QuickObserver<List<JobSearchSuggestion>>(mView.context()) {
            @Throws(Exception::class)
            override fun onSuccessful(t: BaseResponseInfo<List<JobSearchSuggestion>>) {
                mView.getSearchSuggestionsSuccess(t.content, term)
            }

            @Throws(Exception::class)
            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {

            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .getSearchSuggestions(term)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}
