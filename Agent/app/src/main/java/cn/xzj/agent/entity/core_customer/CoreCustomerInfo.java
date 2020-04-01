package cn.xzj.agent.entity.core_customer;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/27
 * @Des
 */
public class CoreCustomerInfo {

    /**
     * agentId (string, optional): 经纪人ID ,
     * agentName (string, optional): 经纪人名 ,
     * agentNickname (string, optional): 经纪人昵称 ,
     * currentState (integer, optional): 当前状态 ,
     * lastContactComment (string, optional): 最后注纪内容 ,
     * lastContactCommentOperatorName (string, optional): 最后注纪注纪人 ,
     * lastContactTime (string, optional): 最后联系时间 ,
     * nickname (string, optional): 客户常用名 ,
     * onboardingTime (string, optional): 入职时间 ,
     * phone (string, optional): 客户手机号 ,
     * positionId (string, optional): 职位ID ,
     * positionName (string, optional): 职位名称 ,
     * positionTypes (string, optional): 岗位特性 ,
     * quitTime (string, optional): 离职时间 ,
     * signInStoreName (string, optional): 签到门店 ,
     * signInTime (string, optional): 签到时间 ,
     * signUpTime (string, optional): 注册时间 ,
     * supervisorId (string, optional): 主管ID ,
     * supervisorNickname (string, optional): 主管 ,
     * userId (string, optional): 客户ID ,
     * userName (string, optional): 客户名
     */

    private String agentId;
    private String agentName;
    private String agentNickname;
    private int currentState;
    private String lastContactComment;
    private String lastContactCommentOperatorName;
    private long lastContactTime;
    private String nickname;
    private long onboardingTime;
    private String phone;
    private String positionId;
    private String positionName;
    private String positionTypes;
    private long quitTime;
    private String signInStoreName;
    private long signInTime;
    private long signUpTime;
    private String supervisorId;
    private String supervisorNickname;
    private String userId;
    private String userName;
    private String wish;

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

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getOnboardingTime() {
        return onboardingTime;
    }

    public void setOnboardingTime(long onboardingTime) {
        this.onboardingTime = onboardingTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPositionTypes() {
        return positionTypes;
    }

    public void setPositionTypes(String positionTypes) {
        this.positionTypes = positionTypes;
    }

    public long getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(long quitTime) {
        this.quitTime = quitTime;
    }

    public String getSignInStoreName() {
        return signInStoreName;
    }

    public void setSignInStoreName(String signInStoreName) {
        this.signInStoreName = signInStoreName;
    }

    public long getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(long signInTime) {
        this.signInTime = signInTime;
    }

    public long getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(long signUpTime) {
        this.signUpTime = signUpTime;
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

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }
}
