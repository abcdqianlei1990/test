package cn.xzj.agent.entity;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/27
 * @ Des
 */
public class DefaultEventMessage {
    private int code;
    private String message;
    private Object data;
    public DefaultEventMessage(){
        this(0,"",null);
    }
    public DefaultEventMessage(int code,String message,Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
