package cn.xzj.agent.wxapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by channey on 2018/3/15.
 */

public class WxPayParamsInfo {
    private String appid;
    private String noncestr;
    private String packageValue;
    private String partnerid;
    private String prepayid;
    private long timestamp;
    private String sign;
    private String paymentStatus;
    private String paymentType;

    @SerializedName("out_trade_no")
    private int outTradeNo;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(int outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
