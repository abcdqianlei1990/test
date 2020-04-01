package cn.xzj.agent.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.channey.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.xzj.agent.BuildConfig;
import cn.xzj.agent.constants.Config;

/**
 * Created by channey on 2018/3/7.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private IWXAPI mWxApi;
    private static String uuid;
    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_entry);
        wxapi = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID, true);
        Intent intent = getIntent();
        boolean b = wxapi.handleIntent(intent, this);
        if (!b) finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(getIntent(), this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        setVisible(false);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        finish();
//    }

    @Override
    public void onReq(BaseReq resp) {
//        Log.d("qian",resp.getType()+"");
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) {
                    ToastUtils.INSTANCE.showToast(this,"分享失败");
                    finish();
                } else {
                    ToastUtils.INSTANCE.showToast(this,"登录失败");
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
//                        String code = ((SendAuth.Resp) resp).code;
//
//                        String url = UrlConfig.Companion.getWX_LOGIN()+"&code="+code;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("url",url);
//                        Intent intent = new Intent(this, WebViewActivity.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        finish();
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        ToastUtils.INSTANCE.showToast(this,"微信分享成功");
//                        Boolean shouldShow = SharedPreferencesUtils.INSTANCE.getBoolean(this, SharedPreferencesKeys.Companion.getShouldShowShareSuccess());
//                        if (shouldShow){
//                            SharedPreferencesUtils.INSTANCE.remove(this,SharedPreferencesKeys.Companion.getShouldShowShareSuccess());
//                            gotoShareSuccess();
//                        }
//                        finish();
                        break;
                    case ConstantsAPI.COMMAND_PAY_BY_WX:
                        ToastUtils.INSTANCE.showToast(this,"微信支付");
                        break;
                }
                break;
            case BaseResp.ErrCode.ERR_BAN:
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:

                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                finish();
                break;
        }
    }

    public void gotoShareSuccess(){
//        Intent intent = new Intent(this,ShareSuccessActivity.class);
//        startActivity(intent);
    }
}
