package cn.xzj.agent.core.common.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
public abstract class BasePresenter<T extends BaseView> {
    protected T mView;
    private CompositeDisposable mCompositeDisposable;

    public void attachView(T view) {
        this.mView = view;
        if (mCompositeDisposable == null)
            mCompositeDisposable = new CompositeDisposable();
    }

    public void detachView() {
        mView = null;
        unSubscribe();
    }

    public void addSubscribe(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

}
