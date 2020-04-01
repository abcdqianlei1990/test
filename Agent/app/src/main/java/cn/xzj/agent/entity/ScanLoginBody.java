package cn.xzj.agent.entity;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/11/13
 * @ Des
 */
public class ScanLoginBody {
    public ScanLoginBody(String id) {
        this.id = id;
    }

    /**
     * id : string
     */

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
