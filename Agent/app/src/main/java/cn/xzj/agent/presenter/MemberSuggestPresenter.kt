package cn.xzj.agent.presenter

import android.app.ProgressDialog
import android.content.DialogInterface
import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.Token
import cn.xzj.agent.entity.TokenResp
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.customer.MemberSuggestBody
import cn.xzj.agent.iview.ILoginView
import cn.xzj.agent.iview.IMemberSuggestView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.Observer2
import cn.xzj.agent.net.QuickObserver
import com.alibaba.fastjson.JSON
import com.channey.utils.StringUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class MemberSuggestPresenter : BasePresenter<IMemberSuggestView>() {

    fun memberSuggest(name: String,phone: String,id: String) {
        var body = MemberSuggestBody(name,phone,id)
        val mObserver=object : QuickObserver<Boolean>(mView.context()) {
            @Throws(Exception::class)
            override fun onSuccessful(t: BaseResponseInfo<Boolean>) {
                mView.onSuggestSuccess(t.content)
            }

            @Throws(Exception::class)
            override fun onFailure(e: Throwable, isNetWorkError: Boolean) {
//                mView.onSuggestFailure(e.message!!)
            }
        }
        Client.getInstance(mView.context())
                .apiManager
                .memberSuggest(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

}