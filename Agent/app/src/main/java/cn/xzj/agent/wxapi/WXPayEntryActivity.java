package cn.xzj.agent.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cn.xzj.agent.constants.Config;
import cn.xzj.agent.entity.event.WxPaymentSuccessfulEvent;

/**
 * Created by channey on 2018/3/15.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxapi = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID, true);
        Intent intent = getIntent();
        boolean b = wxapi.handleIntent(intent, this);
        if (!b) finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("qian",""+baseReq.openId);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("qian","type="+resp.getType()+",errCode="+resp.errCode+",errStr="+resp.errStr);
        if (ConstantsAPI.COMMAND_PAY_BY_WX == resp.getType()){
            switch (resp.errCode){
                case BaseResp.ErrCode.ERR_OK:
                    Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new WxPaymentSuccessfulEvent(true));
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(getApplicationContext(),"取消支付",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    Toast.makeText(getApplicationContext(),"支付异常 errcode:-1",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                    default:
                        finish();
                        break;
            }
        }
    }
}
