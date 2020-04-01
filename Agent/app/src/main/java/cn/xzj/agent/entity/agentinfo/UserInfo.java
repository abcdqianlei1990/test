package cn.xzj.agent.entity.agentinfo;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/29
 * @Des 登录的用户信息，需要保存到数据库
 */
@Entity
public class UserInfo {
    @Id
    private long id;
    private String userId;
    private String mobile;
    private Date updateTime;

    public UserInfo(String userId, String mobile,Date updateTime) {
        this.userId = userId;
        this.mobile = mobile;
        this.updateTime=updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
