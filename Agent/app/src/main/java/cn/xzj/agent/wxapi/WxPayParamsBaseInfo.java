package cn.xzj.agent.wxapi;


import cn.xzj.agent.entity.ErrorInfo;

/**
 * Created by channey on 2018/3/15.
 */

public class WxPayParamsBaseInfo{
    private String code;
    private WxPayParamsInfo content;
    private ErrorInfo error;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WxPayParamsInfo getContent() {
        return content;
    }

    public void setContent(WxPayParamsInfo content) {
        this.content = content;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }
}
