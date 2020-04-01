package cn.xzj.agent.core.common.mvp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import cn.xzj.agent.core.common.QuickActivity;
import cn.xzj.agent.util.CreateUtil;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des
 */
public abstract class MVPBaseActivity<T extends BasePresenter> extends QuickActivity implements BaseView {
    protected T mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = CreateUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
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
    private ProgressDialog mProgressDialog;
    public void showProgressDialog(String message, boolean isCancelable) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mProgressDialog.cancel();
            }
        });
    }

    public void closeProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog.cancel();
            }
        }
    }
}
