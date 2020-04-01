package cn.xzj.agent.entity;



/**
 * Created by channey on 2017/1/8.
 */

public class HttpThrowableInfo {
    private String code;
    private HttpErrorInfo error;

    public HttpThrowableInfo() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HttpErrorInfo getError() {
        return error;
    }

    public void setError(HttpErrorInfo error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "HttpThrowableInfo{" +
                "code='" + code + '\'' +
                ", error=" + error +
                '}';
    }
}
