package cn.xzj.agent.net;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/5
 * @Des 完成订阅后就解绑订阅
 */
public class Observer2<T> implements Observer<T> {
    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable = d;
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        mDisposable.dispose();
    }
}
