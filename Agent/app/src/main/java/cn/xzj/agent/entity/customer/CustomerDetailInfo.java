package cn.xzj.agent.entity.customer;

import android.os.Parcel;
import android.os.Parcelable;

//客户信息 {
//    age (integer, optional): 年龄 ,
//    agentId (string, optional): 当前经纪人ID ,
//    agentName (string, optional): 当前经纪人名 ,
//    nickname (string, optional): 客户常用名 ,
//    constellation (string, optional): 星座 ,
//    deviceId (string, optional): 设备号 ,
//    exAgent (string, optional): 前任经纪人 ,
//    idCardVerified (boolean, optional): 是否实名认证 ,
//    jiguan (string, optional): 家乡 ,
//    mark (string, optional): 用户标记 ,
//    minzu (string, optional): 民族 ,
//    nextUserId (string, optional): 下一个客户ID ,
//    phone (string, optional): 用户手机 ,
//    positioningByPhone (string, optional): 手机定位 ,
//    referrer (string, optional): 邀请人 ,
//    registerTime (string, optional): 注册时间 ,
//    salaryCard (string, optional): 工资卡 ,
//    salaryCardBank (string, optional): 工资卡银行 ,
//    sex (string, optional): 性别: 0 - 保密 1 - 男性 2 - 女性 ,
//    source (string, optional): 注册来源 ,
//    userId (string, optional): 客户ID ,
//    userName (string, optional): 客户名 ,
//    zxContractAgreed (boolean, optional): 同意过周薪协议
//    userRank (boolean, optional): 客户等级
//}
public class CustomerDetailInfo implements Parcelable{
    private int age;
    private String agentId;
    private String agentName;
    private String nickname;
    private String constellation;
    private String deviceId;
    private String exAgent;
    private boolean idCardVerified;
    private String jiguan;
    private String mark;
    private String minzu;
    private String nextUserId;
    private String phone;
    private String positioningByPhone;
    private String referrer;
    private String registerTime;
    private String salaryCard;
    private String salaryCardBank;
    private String sex;
    private String source;
    private String sourceName;
    private String userId;
    private String userName;
    private String zxContractAgreed;
    private String rank;
    private String wish;

    protected CustomerDetailInfo(Parcel in) {
        age = in.readInt();
        agentId = in.readString();
        agentName = in.readString();
        nickname = in.readString();
        constellation = in.readString();
        deviceId = in.readString();
        exAgent = in.readString();
        idCardVerified = in.readByte() != 0;
        jiguan = in.readString();
        mark = in.readString();
        minzu = in.readString();
        nextUserId = in.readString();
        phone = in.readString();
        positioningByPhone = in.readString();
        referrer = in.readString();
        registerTime = in.readString();
        salaryCard = in.readString();
        salaryCardBank = in.readString();
        sex = in.readString();
        source = in.readString();
        sourceName = in.readString();
        userId = in.readString();
        userName = in.readString();
        zxContractAgreed = in.readString();
        rank = in.readString();
        wish = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(agentId);
        dest.writeString(agentName);
        dest.writeString(nickname);
        dest.writeString(constellation);
        dest.writeString(deviceId);
        dest.writeString(exAgent);
        dest.writeByte((byte) (idCardVerified ? 1 : 0));
        dest.writeString(jiguan);
        dest.writeString(mark);
        dest.writeString(minzu);
        dest.writeString(nextUserId);
        dest.writeString(phone);
        dest.writeString(positioningByPhone);
        dest.writeString(referrer);
        dest.writeString(registerTime);
        dest.writeString(salaryCard);
        dest.writeString(salaryCardBank);
        dest.writeString(sex);
        dest.writeString(source);
        dest.writeString(sourceName);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(zxContractAgreed);
        dest.writeString(rank);
        dest.writeString(wish);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerDetailInfo> CREATOR = new Creator<CustomerDetailInfo>() {
        @Override
        public CustomerDetailInfo createFromParcel(Parcel in) {
            return new CustomerDetailInfo(in);
        }

        @Override
        public CustomerDetailInfo[] newArray(int size) {
            return new CustomerDetailInfo[size];
        }
    };

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getExAgent() {
        return exAgent;
    }

    public void setExAgent(String exAgent) {
        this.exAgent = exAgent;
    }

    public boolean isIdCardVerified() {
        return idCardVerified;
    }

    public void setIdCardVerified(boolean idCardVerified) {
        this.idCardVerified = idCardVerified;
    }

    public String getJiguan() {
        return jiguan;
    }

    public void setJiguan(String jiguan) {
        this.jiguan = jiguan;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMinzu() {
        return minzu;
    }

    public void setMinzu(String minzu) {
        this.minzu = minzu;
    }

    public String getNextUserId() {
        return nextUserId;
    }

    public void setNextUserId(String nextUserId) {
        this.nextUserId = nextUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPositioningByPhone() {
        return positioningByPhone;
    }

    public void setPositioningByPhone(String positioningByPhone) {
        this.positioningByPhone = positioningByPhone;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getSalaryCard() {
        return salaryCard;
    }

    public void setSalaryCard(String salaryCard) {
        this.salaryCard = salaryCard;
    }

    public String getSalaryCardBank() {
        return salaryCardBank;
    }

    public void setSalaryCardBank(String salaryCardBank) {
        this.salaryCardBank = salaryCardBank;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public String getZxContractAgreed() {
        return zxContractAgreed;
    }

    public void setZxContractAgreed(String zxContractAgreed) {
        this.zxContractAgreed = zxContractAgreed;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }
}
