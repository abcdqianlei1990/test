package cn.xzj.agent.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import cn.xzj.agent.constants.Config
import cn.xzj.agent.entity.payment.PaymentRetInfo
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

object PaymentUtil {
    private val WX_APP_ID = Config.WX_APP_ID

    private fun getWxApi(context: Context): IWXAPI? {
        val wxApi = WXAPIFactory.createWXAPI(context, WX_APP_ID, false)
        wxApi.registerApp(WX_APP_ID)
        if (!wxApi.isWXAppInstalled) {
            Toast.makeText(context,"您还未安装微信客户端",Toast.LENGTH_LONG).show()
            return null
        }
        return wxApi
    }

    fun payByWechat(context: Context,paramsInfo: PaymentRetInfo){
        val wxApi = getWxApi(context)
        if (wxApi != null) {
            val request = PayReq()
            request.appId = WX_APP_ID
            request.partnerId = paramsInfo.partnerId
            request.prepayId = paramsInfo.prepayId
            request.packageValue = paramsInfo.packageValue
            request.nonceStr = paramsInfo.nonceStr
            request.timeStamp = paramsInfo.timeStamp
            request.sign = paramsInfo.sign
//            request.extData = paramsInfo.outTradeNo.toString()
            var ret = wxApi.sendReq(request)
            Log.d("qian","ret = $ret")
        }
    }
}