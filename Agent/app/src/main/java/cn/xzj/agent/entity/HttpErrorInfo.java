package cn.xzj.agent.entity;



/**
 * Created by channey on 2017/1/8.
 */

public class HttpErrorInfo{
    private String message;

    public HttpErrorInfo() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "HttpErrorInfo{" +
                "message='" + message + '\'' +
                '}';
    }
}
