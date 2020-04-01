package cn.xzj.agent.entity;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/3
 * @Des
 */
public class AgentWechatAccountInfo {

    /**
     * agentId (string, optional): 小职姐ID ,
     * agentJobNo (string, optional): 小职姐工号 ,
     * agentNickname (string, optional): 小职姐 ,
     * defaultAccount (boolean, optional): 是否默认帐号, 默认帐号会在小职姐个人资料中展示 ,
     * id (string, optional): ID ,
     * lastUploadTime (string, optional): 上传 ,
     * totalContactCount (integer, optional): 联系人总数 ,
     * wxAccount (string, optional): 微信帐号 ,
     * wxQrCodeUrl (string, optional): 微信二维码URL
     */

    private String agentId;
    private String agentJobNo;
    private String agentNickname;
    private boolean defaultAccount;
    private String id;
    private String lastUploadTime;
    private int totalContactCount;
    private String wxAccount;
    private String wxQrCodeUrl;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentJobNo() {
        return agentJobNo;
    }

    public void setAgentJobNo(String agentJobNo) {
        this.agentJobNo = agentJobNo;
    }

    public String getAgentNickname() {
        return agentNickname;
    }

    public void setAgentNickname(String agentNickname) {
        this.agentNickname = agentNickname;
    }

    public boolean isDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUploadTime() {
        return lastUploadTime;
    }

    public void setLastUploadTime(String lastUploadTime) {
        this.lastUploadTime = lastUploadTime;
    }

    public int getTotalContactCount() {
        return totalContactCount;
    }

    public void setTotalContactCount(int totalContactCount) {
        this.totalContactCount = totalContactCount;
    }

    public String getWxAccount() {
        return wxAccount;
    }

    public void setWxAccount(String wxAccount) {
        this.wxAccount = wxAccount;
    }

    public String getWxQrCodeUrl() {
        return wxQrCodeUrl;
    }

    public void setWxQrCodeUrl(String wxQrCodeUrl) {
        this.wxQrCodeUrl = wxQrCodeUrl;
    }
}
