package cn.xzj.agent.entity.privacy;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/17
 * @ Des
 * data class SmsInfo(var address:String?,var date:Long?,var read:Int?,var type:Int?,var body:String?)
 */
public class SmsInfo {
    private  String address;
    private Long date;
    private int read ;
    private int type;
    private String body;

    public SmsInfo(String address, Long date, int read, int type, String body) {
        this.address = address;
        this.date = date;
        this.read = read;
        this.type = type;
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
