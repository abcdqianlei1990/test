package cn.xzj.agent.entity.payment;

import com.google.gson.annotations.SerializedName;

/**
 * 预约订单支付结果
 * 注：当paymentType为白条时，根据接口返回code判断是否成功；当paymentType不是白条时（如微信支付），
 * 根据paymentStatus处理逻辑，如唤起本地微信去支付
 */

//appId (string, optional),
//        nonceStr (string, optional),
//        orderId (string, optional): 订单号 ,
//        packageValue (string, optional),
//        partnerId (string, optional),
//        prepayId (string, optional),
//        sign (string, optional),
//        timeStamp (string, optional)

public class PaymentRetInfo {
    private String appId;
    private String nonceStr;
    private String orderId;
    private String packageValue;
    private String partnerId;
    private String prepayId;
    private String timeStamp;
    private String sign;
    private int paymentStatus;
    private String paymentType;

    // 0 待支付 1 支付中 2 已支付 3 已取消
    public static int PAYMENT_STATUS_UNPAID = 0;
    public static int PAYMENT_STATUS_PAYING = 1;
    public static int PAYMENT_STATUS_PAID = 2;
    public static int PAYMENT_STATUS_CANCELLED = 3;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
