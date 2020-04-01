package cn.xzj.agent.entity;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/22
 * @ Des
 */
public class WechatFriendsUploadDTO {

    /**
     * customerCount (integer, optional): 用户数 ,
     * snapshotUrl (string, optional): 截图 ,
     * wechatAccount (string, optional): 微信号
     */

    private int customerCount;
    private String snapshotUrl;
    private String wechatAccount;

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public String getSnapshotUrl() {
        return snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }
}
