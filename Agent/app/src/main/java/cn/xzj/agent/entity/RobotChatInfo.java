package cn.xzj.agent.entity;

import java.util.List;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/18
 * @Des
 */
public class RobotChatInfo {

    private int objectType;//0自己1他人
    private String content;//发送内容
    private String contentType;//发送消息类型
    private String icon;//头像
    private long createTime;//创建时间
    private List<String> messages;


    public RobotChatInfo(){

    }

    public RobotChatInfo(int objectType, String content, List<String> messages) {
        this.objectType = objectType;
        this.content = content;
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
