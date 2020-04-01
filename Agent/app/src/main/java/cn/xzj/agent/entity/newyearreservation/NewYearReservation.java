package cn.xzj.agent.entity.newyearreservation;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/28
 * @Des
 */
public class NewYearReservation {

    /**
     * agentId (string, optional): 经纪人ID ,
     * agentName (string, optional): 经纪人名 ,
     * agentNickname (string, optional): 经纪人昵称 ,
     * applyTime (string, optional): 报名时间 ,
     * lastContactComment (string, optional): 最后注纪内容 ,
     * lastContactCommentOperatorName (string, optional): 最后注纪注纪人 ,
     * lastContactTime (string, optional): 最后联系时间 ,
     * positionId (string, optional): 岗位ID ,
     * positionName (string, optional): 岗位名称 ,
     * supervisorId (string, optional): 主管ID ,
     * supervisorNickname (string, optional): 主管 ,
     * userId (string, optional): 用户ID ,
     * userName (string, optional): 用户名 ,
     * userNickname (string, optional): 用户昵称 ,
     * userPhone (string, optional): 用户手机号
     */

    private String agentId;
    private String agentName;
    private String agentNickname;
    private long applyTime;
    private String lastContactComment;
    private String lastContactCommentOperatorName;
    private long lastContactTime;
    private String positionId;
    private String positionName;
    private String supervisorId;
    private String supervisorNickname;
    private String userId;
    private String userName;
    private String userNickname;
    private String userPhone;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentNickname() {
        return agentNickname;
    }

    public void setAgentNickname(String agentNickname) {
        this.agentNickname = agentNickname;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public String getLastContactComment() {
        return lastContactComment;
    }

    public void setLastContactComment(String lastContactComment) {
        this.lastContactComment = lastContactComment;
    }

    public String getLastContactCommentOperatorName() {
        return lastContactCommentOperatorName;
    }

    public void setLastContactCommentOperatorName(String lastContactCommentOperatorName) {
        this.lastContactCommentOperatorName = lastContactCommentOperatorName;
    }

    public long getLastContactTime() {
        return lastContactTime;
    }

    public void setLastContactTime(long lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorNickname() {
        return supervisorNickname;
    }

    public void setSupervisorNickname(String supervisorNickname) {
        this.supervisorNickname = supervisorNickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
