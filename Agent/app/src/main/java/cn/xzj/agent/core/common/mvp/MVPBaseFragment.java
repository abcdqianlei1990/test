package cn.xzj.agent.core.common.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.xzj.agent.core.common.BaseFragment;
import cn.xzj.agent.util.CreateUtil;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
public abstract class MVPBaseFragment<T extends BasePresenter> extends BaseFragment implements BaseView {
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = CreateUtil.getT(this, 0);
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void showContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showNetworkError() {

    }
}
