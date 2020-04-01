package cn.xzj.agent.entity.active_customer;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/27
 * @Des
 */
public class ActiveCustomerInfo {

    /**
     * activeTimes (integer, optional): 活跃次数 ,
     * agentId (string, optional): 经纪人ID ,
     * agentName (string, optional): 经纪人名 ,
     * agentNickname (string, optional): 经纪人昵称 ,
     * lastActiveEvents (string, optional): 最近活跃事件 ,
     * lastActiveTime (string, optional): 最近活跃时间 ,
     * lastApplyStatus (string, optional): 最近申请状态 ,
     * lastContactComment (string, optional): 最后注纪内容 ,
     * lastContactCommentOperatorName (string, optional): 最后注纪注纪人 ,
     * lastContactTime (string, optional): 最后联系时间 ,
     * lastOnboardingStatus (string, optional): 最近入职状态 ,
     * lastQuitStatus (string, optional): 最近离职状态 ,
     * nickname (string, optional): 客户常用名 ,
     * phone (string, optional): 客户手机号 ,
     * supervisorId (string, optional): 主管ID ,
     * supervisorNickname (string, optional): 主管 ,
     * userId (string, optional): 客户ID ,
     * userName (string, optional): 客户名
     */

    private int activeTimes;
    private String agentId;
    private String agentName;
    private String agentNickname;
    private String lastActiveEvents;
    private String lastActiveTime;
    private String lastApplyStatus;
    private String lastContactComment;
    private String lastContactCommentOperatorName;
    private String lastContactTime;
    private String lastOnboardingStatus;
    private String lastQuitStatus;
    private String nickname;
    private String phone;
    private String supervisorId;
    private String supervisorNickname;
    private String userId;
    private String userName;

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    private String wish;

    public int getActiveTimes() {
        return activeTimes;
    }

    public void setActiveTimes(int activeTimes) {
        this.activeTimes = activeTimes;
    }

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

    public String getLastActiveEvents() {
        return lastActiveEvents;
    }

    public void setLastActiveEvents(String lastActiveEvents) {
        this.lastActiveEvents = lastActiveEvents;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public String getLastApplyStatus() {
        return lastApplyStatus;
    }

    public void setLastApplyStatus(String lastApplyStatus) {
        this.lastApplyStatus = lastApplyStatus;
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

    public String getLastContactTime() {
        return lastContactTime;
    }

    public void setLastContactTime(String lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    public String getLastOnboardingStatus() {
        return lastOnboardingStatus;
    }

    public void setLastOnboardingStatus(String lastOnboardingStatus) {
        this.lastOnboardingStatus = lastOnboardingStatus;
    }

    public String getLastQuitStatus() {
        return lastQuitStatus;
    }

    public void setLastQuitStatus(String lastQuitStatus) {
        this.lastQuitStatus = lastQuitStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
