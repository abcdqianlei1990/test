package cn.xzj.agent.entity;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/17
 * @ Des
 */
public class TokenResp {
    /**
     * var body:Token?,var message:String
     */
    private Token body;
    private String message;

    public Token getBody() {
        return body;
    }

    public void setBody(Token body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
