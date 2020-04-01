package cn.xzj.agent.core.common.mvp;

import android.content.Context;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/30
 * @ Des View 基础代理类
 */
public interface BaseView {
    Context context();

    void showContent();

    void showLoading();

    void showEmpty();

    void showError();

    void showNetworkError();

}
