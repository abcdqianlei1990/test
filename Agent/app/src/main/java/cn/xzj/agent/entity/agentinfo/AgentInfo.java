package cn.xzj.agent.entity.agentinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
public class AgentInfo implements Serializable {

    /**
     * agentId : yemao
     * nick : MarkYe
     * headImages : [{"url":"https://sdxd-oss-public.oss-cn-hzfinance.aliyuncs.com/2018091315015872920_1536822118122","selected":true}]
     * backgroundImages : []
     * name : 叶茂
     * sex : 0
     * jiguan : 重庆市
     * addStarCardNo : 500371199905041341
     * birthday : 1536076800000
     * age : 0
     * constellation : 处女座
     * phone : 17602143726
     * wechat : MarkYe
     * qq : MarkYe
     * regions : []
     * dailyWXFriendCountUploaded (boolean, optional): 今日微信数是否上传 ,
     * charge (boolean, optional): 是否收费小职姐 ,
     * chargeAmount (number, optional): 收取的费用 ,
     * chargeStatus (integer, optional): 支付状态: 1 - 支付中, 2 - 支付失败, 3 - 支付成功, 4 - 订单超时 ,
     * bank (string, optional): 开户行 ,
     * bankCardHolder (string, optional): 持卡人 ,
     * bindCard (boolean, optional): 是否绑卡 ,
     * face (boolean, optional): 是否刷脸认证 ,
     * realName (boolean, optional): 是否实名认证 ,
     * addStarBankCard (string, optional): 加星银行卡号 ,
     */

    private String agentId;
    private String nick;
    private String nickname;
    private String name;
    private int sex;
    private String jiguan;
    private String addStarCardNo;
    private long birthday;
    private int age;
    private String constellation;
    private String phone;
    private String cellphone;
    private String wechat;
    private String qq;
    private List<HeadImagesBean> headImages;
    private List<?> backgroundImages;
    private List<?> regions;
    private boolean dailyWXFriendCountUploaded;
    private String upperLevelAgentId;   //上级ID ,
    private String upperLevelAgentNickname; //上级昵称
    private String agentType; //小职姐类型: FULL_AGENT - 全职小职姐, PART_AGENT - 兼职小职姐, OUTSOURCING_AGENT - 外包小职姐, FRANCHISEE_AGENT - 加盟商小职姐
    private String supervisor; //是否是主管 ,
    private String updateTime;  //编辑时间 ,
    private boolean charge;  //是否收费小职 ,
    private double chargeAmount;  //收取的费用 ,
    private int chargeStatus;  //支付状态 ,
    private boolean bindCard;   //是否绑卡
    private boolean face;   //是否刷脸认证
    private boolean realName;   //是否实名认证
    private String bank;    //开户行
    private String bankCardHolder;  //持卡人
    private String addStarBankCard;  //加星银行卡号


    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getJiguan() {
        return jiguan;
    }

    public void setJiguan(String jiguan) {
        this.jiguan = jiguan;
    }

    public String getAddStarCardNo() {
        return addStarCardNo;
    }

    public void setAddStarCardNo(String addStarCardNo) {
        this.addStarCardNo = addStarCardNo;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public List<HeadImagesBean> getHeadImages() {
        return headImages;
    }

    public void setHeadImages(List<HeadImagesBean> headImages) {
        this.headImages = headImages;
    }

    public List<?> getBackgroundImages() {
        return backgroundImages;
    }

    public void setBackgroundImages(List<?> backgroundImages) {
        this.backgroundImages = backgroundImages;
    }

    public List<?> getRegions() {
        return regions;
    }

    public void setRegions(List<?> regions) {
        this.regions = regions;
    }

    public static class HeadImagesBean implements Serializable {
        /**
         * url : https://sdxd-oss-public.oss-cn-hzfinance.aliyuncs.com/2018091315015872920_1536822118122
         * selected : true
         */

        private String url;
        private boolean selected;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

    }

    public boolean isDailyWXFriendCountUploaded() {
        return dailyWXFriendCountUploaded;
    }

    public void setDailyWXFriendCountUploaded(boolean dailyWXFriendCountUploaded) {
        this.dailyWXFriendCountUploaded = dailyWXFriendCountUploaded;
    }

    public String getUpperLevelAgentId() {
        return upperLevelAgentId;
    }

    public void setUpperLevelAgentId(String upperLevelAgentId) {
        this.upperLevelAgentId = upperLevelAgentId;
    }

    public String getUpperLevelAgentNickname() {
        return upperLevelAgentNickname;
    }

    public void setUpperLevelAgentNickname(String upperLevelAgentNickname) {
        this.upperLevelAgentNickname = upperLevelAgentNickname;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getCellPhone() {
        return cellphone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellphone = cellPhone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public boolean isCharge() {
        return charge;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public boolean isBindCard() {
        return bindCard;
    }

    public void setBindCard(boolean bindCard) {
        this.bindCard = bindCard;
    }

    public boolean isFace() {
        return face;
    }

    public void setFace(boolean face) {
        this.face = face;
    }

    public boolean isRealName() {
        return realName;
    }

    public void setRealName(boolean realName) {
        this.realName = realName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCardHolder() {
        return bankCardHolder;
    }

    public void setBankCardHolder(String bankCardHolder) {
        this.bankCardHolder = bankCardHolder;
    }

    public String getAddStarBankCard() {
        return addStarBankCard;
    }

    public void setAddStarBankCard(String addStarBankCard) {
        this.addStarBankCard = addStarBankCard;
    }
}
