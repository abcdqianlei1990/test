package cn.xzj.agent.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import cn.xzj.agent.entity.BaseResponseInfo;
import cn.xzj.agent.entity.ErrorInfo;
import cn.xzj.agent.entity.ErrorsInfo;
import cn.xzj.agent.entity.HttpThrowableInfo;
import cn.xzj.agent.ui.BaseActivity;
import cn.xzj.agent.util.NetworkUtil;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by channey on 2016/10/20.
 * version:1.0
 * desc:
 */

public abstract class BasePresenter {
    public void attachView(Context context){}
    public void detachView(){}
    interface ERROR_CODE{
        String TOKEN_DECODE_ERROR = "400004";   //无效的Token
        String TOKEN_INVALID = "400003";    //token失效
        String TOKEN_UNKNOW = "400001";    //未授权资源
        String TOKEN_UNKNOW_ALIAS = "400002";    //未知凭证
        String DEFAULT = "10086";    //默认error code
        String SIGN_ERR = "600006";    //签名验证失败
    }
    public void unSubscribe() {
    }

    public boolean tokenCheckOK(BaseActivity activity, String code){
        if(code.startsWith("4")){ //判断是否是4开头
            activity.tokenInvalid();
            return false;
        }
        return true;
    }
    public boolean isNetWorkAvailable(BaseActivity activity){
        int apnType = NetworkUtil.getConnectedType(activity);
        if(apnType >= 0){
            return true;
        }else {
            activity.networkUnavailable();
            return false;
        }
    }
    public void getUserInfo(){}

    public void logout(){}

    public void getAuthCode(String number, String use, String type){}
    public void authCodeValidate(String number, String code, String checkBits, String use, String type){}

    /**
     * 当code为0当时候，获取错误信息
     * @param info
     * @return
     */
    public String getErrorMsg(BaseResponseInfo info){
        StringBuilder msg = new StringBuilder();
        ErrorInfo error = info.getError();
        if (error != null){
            ArrayList<ErrorsInfo> errors = error.getErrors();
            if(errors == null || errors.size() == 0){
                msg.append(error.getMessage());
            }else{
                for (int i = 0;i < errors.size();i++){
                    msg.append(errors.get(i).getMessage());
                    if(i != errors.size()-1){
                        msg.append(",");
                    }
                }
            }
        }
        return msg.toString();
    }

    /**
     * 当code不为0当时候，获取错误信息
     * @param e
     * @return
     */
    public String getErrorMsg(Throwable e){
        if(e instanceof HttpException){
            String msg = "";
            try {
                ResponseBody errorBody = ((HttpException) e).response().errorBody();
                String readUtf8 = errorBody.source().readUtf8();
                if(!TextUtils.isEmpty(readUtf8)){
                    Gson gson = new Gson();
                    HttpThrowableInfo errorInfo = gson.fromJson(readUtf8, HttpThrowableInfo.class);
                    msg = errorInfo.getError().getMessage();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                msg = e.getMessage();
            }catch (Exception e2){
                msg = e.getMessage();
            }
            return msg;
        }else {
            return e.getMessage();
        }
    }
}
