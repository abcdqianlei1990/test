package cn.xzj.agent.entity.active_customer;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/27
 * @Des
 */
public class CustomerActiveEventInfo {

    /**
     * content (string, optional): 内容 ,
     * occurTime (string, optional): 发生时间
     */

    private String content;
    private long occurTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(long occurTime) {
        this.occurTime = occurTime;
    }
}
