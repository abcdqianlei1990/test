package cn.xzj.agent.net;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.channey.utils.StringUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.xzj.agent.BuildConfig;
import cn.xzj.agent.R;
import cn.xzj.agent.entity.BaseResponseInfo;
import cn.xzj.agent.entity.ErrorsInfo;
import cn.xzj.agent.entity.HttpThrowableInfo;
import cn.xzj.agent.ui.LoginActivity;
import cn.xzj.agent.util.LogLevel;
import cn.xzj.agent.util.SharedPreferencesUtil;
import cn.xzj.agent.widget.CommonDialog;
import cn.xzj.agent.widget.SimpleToast;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/14
 * @ Des 网络请求观察者基类
 */
public abstract class QuickObserver<T> extends DisposableObserver<BaseResponseInfo<T>> {
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private CommonDialog mLoginInvalidCommonDialog;
    private boolean mShowProgressDialog = true;

    public QuickObserver(Context context) {
        this.mContext = context;
        mLoginInvalidCommonDialog = CommonDialog.newBuilder(mContext)
                .setMessage(mContext.getResources().getString(R.string.login_invalid_please_relogin))
                .setPositiveButton(mContext.getResources().getString(R.string.confirm), new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(CommonDialog dialog) {
                        dialog.dismiss();
                        //退出登录
                        SharedPreferencesUtil.clear(mContext);
                        LoginActivity.Companion.jump(mContext);
                    }
                })
                .create();
    }

    public QuickObserver(Context context,boolean showProgressDialog) {
        this.mContext = context;
        mShowProgressDialog = showProgressDialog;
        mLoginInvalidCommonDialog = CommonDialog.newBuilder(mContext)
                .setMessage(mContext.getResources().getString(R.string.login_invalid_please_relogin))
                .setPositiveButton(mContext.getResources().getString(R.string.confirm), new CommonDialog.OnClickListener() {
                    @Override
                    public void onClick(CommonDialog dialog) {
                        dialog.dismiss();
                        //退出登录
                        SharedPreferencesUtil.clear(mContext);
                        LoginActivity.Companion.jump(mContext);
                    }
                })
                .create();
    }

    @Override
    public void onComplete() {

    }

    protected void onStart() {
        try {
//            if (mShowProgressDialog) showProgressDialog();
            onRequestStart();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                onFailure(e, false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 请求失败时调用的方法
     */
    @Override
    public void onError(Throwable e) {
        LogLevel.e("onError", e + "");
        try {
            onRequestEnd();
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                onFailure(e, false);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException
                    || e instanceof HttpException
                    || e instanceof SocketTimeoutException) {
                if (BuildConfig.DEBUG) {
                    SimpleToast.showLong(e.getMessage() + "");
                } else {
                    StringBuilder sb = new StringBuilder(mContext.getString(R.string.connection_to_server_failed));
                    if (StringUtils.INSTANCE.isNotEmpty(e.getMessage()))
                        sb.append(","+e.getMessage());
                    SimpleToast.showShort(sb.toString());

                }
                onFailure(e, true);
            } else {
                if (BuildConfig.DEBUG) {
                    SimpleToast.showLong(e + "");
                }
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onNext(BaseResponseInfo<T> tBaseResponseInfo) {
        try {
            onRequestEnd();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                onFailure(e, false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (tBaseResponseInfo.isSuccess()) {
            try {
                onSuccessful(tBaseResponseInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                onCodeError(tBaseResponseInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回成功
     */
    protected abstract void onSuccessful(BaseResponseInfo<T> t) throws Exception;

    /**
     * 返回成功了,但是code错误
     */
    protected void onCodeError(BaseResponseInfo<T> t) throws Exception {
        try {
            StringBuilder errorMessage = new StringBuilder();
            if (t.getError() != null) {
                if (t.getError().getErrors() != null) {
                    for (ErrorsInfo errorsInfo : t.getError().getErrors()) {
                        if (errorMessage.length() == 0) {
                            errorMessage.append(errorsInfo.getMessage());
                        } else {
                            errorMessage.append(",").append(errorsInfo.getMessage());
                        }
                    }
                } else {
                    errorMessage.append(t.getError().getMessage());
                }

            } else {
                errorMessage.append(t.getError().getMessage());
            }
            SimpleToast.showNormal(errorMessage.toString());
        } catch (Exception e) {
            e.printStackTrace();
            SimpleToast.showNormal(t.toString());
        }
        if (t.isTokenInvalid()) {
            //登录失效
            if (!mLoginInvalidCommonDialog.isShowing() && SharedPreferencesUtil.getToken(mContext) != null) {
                mLoginInvalidCommonDialog.show();
            }
        }
    }

    /**
     * 返回失败
     *
     * @param isNetWorkError 是否是网络错误
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

    protected void onRequestStart() throws Exception {
    }

    protected void onRequestEnd() throws Exception {
        closeProgressDialog();
    }

    public void showProgressDialog() {
        this.showProgressDialog(false);
    }

    public void showProgressDialog(boolean isCancelable) {
        this.showProgressDialog("请稍后", isCancelable);
    }

    public void showProgressDialog(String message) {
        this.showProgressDialog(message, false);
    }

    public void showProgressDialog(String message, boolean isCancelable) {
        mProgressDialog = new ProgressDialog(mContext);
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

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog.cancel();
            }
        }
    }

    /**
     * 当code不为0当时候，获取错误信息
     */
    public String getErrorMsg(Throwable e) {
        if (e instanceof HttpException) {
            String msg = "";
            try {
                ResponseBody errorBody = ((HttpException) e).response().errorBody();

                String readUtf8 = errorBody.source().readUtf8();
                if (!TextUtils.isEmpty(readUtf8)) {
                    Gson gson = new Gson();
                    HttpThrowableInfo errorInfo = gson.fromJson(readUtf8, HttpThrowableInfo.class);
                    msg = readUtf8;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                msg = e.getMessage();
            } catch (Exception e2) {
                msg = e.getMessage();
            }
            return msg;
        } else if (e instanceof SocketException || e instanceof SocketTimeoutException) {
            return "socketException";
        } else {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
