package cn.xzj.agent.core.common;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/26
 * @ Des 自定义handler防止内存泄漏，使用 "弱引用"
 */
public abstract class CommonHandler<T> extends Handler {
    private WeakReference<T> mWeakReference;

     protected CommonHandler(T t) {
        mWeakReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mWeakReference.get() != null) {
            executeMessage(msg);
        }
    }

    public abstract void executeMessage(Message msg);


}
