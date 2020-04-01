package cn.xzj.agent.entity.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/28
 * @ Des
 */
public class RemarkVO implements Serializable {

    /**
     * age (string, optional): 年龄 ,
     * birthplaceArea (string, optional): 籍贯区 ,
     * birthplaceCity (string, optional): 籍贯市 ,
     * birthplaceProvince (string, optional): 籍贯省 ,
     * currentArea (string, optional): 当前所在区 ,
     * currentCity (string, optional): 当前所在市 ,
     * currentProvince (string, optional): 当前所在省 ,
     * currentSalary (string, optional): 当前薪资 ,
     * education (string, optional): 学历 ,
     * expectArea (string, optional): 期望工作区 ,
     * expectCity (string, optional): 期望工作市 ,
     * expectCityName (string, optional): 期望工作市名称 ,
     * expectOnboardingDate (string, optional): 期望入职时间 ,
     * expectPosition (string, optional): 求职意向 ,
     * expectProvince (string, optional): 期望工作省 ,
     * expectSalary (string, optional): 期望薪资 ,
     * findJob (string, optional): 找工作多久 ,
     * goOut (string, optional): 出门多久 ,
     * marital (string, optional): 婚姻状态: MARRIED - 已婚, UNMARRIED - 未婚, GOT_ENGAGED - 订婚, HAVING_BG_FRIEND - 有男女朋友, SINGLE - 单身 = ['MARRIED', 'UNMARRIED', 'GOT_ENGAGED', 'HAVING_BG_FRIEND', 'SINGLE'],
     * nickname (string, optional): 常用称呼 ,
     * onboarding (integer, optional): 工作状态: 1 - 在职 2 - 离职 ,
     * others (string, optional): 其他 ,
     * painPoints (string, optional): 生活痛点 ,
     * peoples (string, optional): 组团还是SOLO ,
     * personalityTags (Array[integer], optional): 个性标签: 11 - 不怕脏, 12 - 不怕累, 13 - 不看显微镜, 14 - 不穿无尘服, 15 - 希望高返费, 16 - 倾向小厂, 17 - 倾向大厂, 18 - 倾向短期工作, 19 - 倾向稳定工作20 - 要求包吃住21 - 要求五险一金 ,
     * qq (string, optional): QQ ,
     * sex (string, optional): 性别: 0 - 保密 1 - 男性 2 - 女性 ,
     * skillTag (string, optional): 技能标签: 101 - 普工, 102 - 技术工 ,
     * tags (Array[integer], optional): 标记: 1 - 少数名族, 2 - 烟疤, 3 - 残疾, 4 - 纹身, 5 - 骂人, 6 - 有案底 ,
     * userId (string, optional): 客户ID ,
     * wechat (string, optional): 微信
     */
    private String age;
    private String birthplaceArea;
    private String birthplaceCity;
    private String birthplaceProvince;
    private String currentArea;
    private String currentCity;
    private String currentProvince;
    private String currentSalary;
    private String education;
    private String expectArea;
    private String expectCity;
    private String expectCityName;
    private String expectOnboardingDate;
    private String expectPosition;
    private String expectProvince;
    private String expectSalary;
    private String findJob;
    private String goOut;
    private String marital;
    private String nickname;
    private int onboarding;
    private String others;
    private String painPoints;
    private String peoples;
    private String qq;
    private String sex;
    private String skillTag;
    private String userId;
    private String wechat;
    private List<Integer> personalityTags;
    private List<Integer> tags;
    private String invalidReason;   //无效用户理由: 中介送人的, 劳务招人的, 企业人事, 内部员工, 重度残疾, 重罪案底, 精神病人, 年龄超过55岁, 新疆维族人, 四川彝族人, 频繁骚扰, 恶意骂人, 其他原因(选择其他则需要填写原因) ,
    private Integer invalidStatus;   //无效用户状态: 0 - 已申请, 1 - 已通过, 2 - 已驳回

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthplaceArea() {
        return birthplaceArea;
    }

    public void setBirthplaceArea(String birthplaceArea) {
        this.birthplaceArea = birthplaceArea;
    }

    public String getBirthplaceCity() {
        return birthplaceCity;
    }

    public void setBirthplaceCity(String birthplaceCity) {
        this.birthplaceCity = birthplaceCity;
    }

    public String getBirthplaceProvince() {
        return birthplaceProvince;
    }

    public void setBirthplaceProvince(String birthplaceProvince) {
        this.birthplaceProvince = birthplaceProvince;
    }

    public String getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(String currentArea) {
        this.currentArea = currentArea;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentProvince() {
        return currentProvince;
    }

    public void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    public String getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(String currentSalary) {
        this.currentSalary = currentSalary;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExpectArea() {
        return expectArea;
    }

    public void setExpectArea(String expectArea) {
        this.expectArea = expectArea;
    }

    public String getExpectCity() {
        return expectCity;
    }

    public void setExpectCity(String expectCity) {
        this.expectCity = expectCity;
    }

    public String getExpectCityName() {
        return expectCityName;
    }

    public void setExpectCityName(String expectCityName) {
        this.expectCityName = expectCityName;
    }

    public String getExpectOnboardingDate() {
        return expectOnboardingDate;
    }

    public void setExpectOnboardingDate(String expectOnboardingDate) {
        this.expectOnboardingDate = expectOnboardingDate;
    }

    public String getExpectPosition() {
        return expectPosition;
    }

    public void setExpectPosition(String expectPosition) {
        this.expectPosition = expectPosition;
    }

    public String getExpectProvince() {
        return expectProvince;
    }

    public void setExpectProvince(String expectProvince) {
        this.expectProvince = expectProvince;
    }

    public String getExpectSalary() {
        return expectSalary;
    }

    public void setExpectSalary(String expectSalary) {
        this.expectSalary = expectSalary;
    }

    public String getFindJob() {
        return findJob;
    }

    public void setFindJob(String findJob) {
        this.findJob = findJob;
    }

    public String getGoOut() {
        return goOut;
    }

    public void setGoOut(String goOut) {
        this.goOut = goOut;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOnboarding() {
        return onboarding;
    }

    public void setOnboarding(int onboarding) {
        this.onboarding = onboarding;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getPainPoints() {
        return painPoints;
    }

    public void setPainPoints(String painPoints) {
        this.painPoints = painPoints;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSkillTag() {
        return skillTag;
    }

    public void setSkillTag(String skillTag) {
        this.skillTag = skillTag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public List<Integer> getPersonalityTags() {
        return personalityTags;
    }

    public void setPersonalityTags(List<Integer> personalityTags) {
        this.personalityTags = personalityTags;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public Integer getInvalidStatus() {
        return invalidStatus;
    }

    public void setInvalidStatus(Integer invalidStatus) {
        this.invalidStatus = invalidStatus;
    }

    public List<String> getStrTag() {
        if (tags == null)
            return null;
        List<String> mTags = new ArrayList<>();
        for (Integer tag : tags) {
            switch (tag) {
                case 1:
                    mTags.add("少数名族");
                    break;
                case 2:
                    mTags.add("烟疤");
                    break;
                case 3:
                    mTags.add("残疾");
                    break;
                case 4:
                    mTags.add("纹身");
                    break;
                case 5:
                    mTags.add("骂人");
                    break;
                case 6:
                    mTags.add("有案底");
                    break;
            }
        }
        return mTags;
    }

    public Integer getTagId(String strTag) {
        Integer tagId = 0;
        switch (strTag) {
            case "少数名族":
                tagId = 1;
                break;
            case "烟疤":
                tagId = 2;
                break;
            case "残疾":
                tagId = 3;
                break;
            case "纹身":
                tagId = 4;
                break;
            case "骂人":
                tagId = 5;
                break;
            case "有案底":
                tagId = 6;
                break;
        }
        return tagId;
    }

    public String getStrSkillTag() {
        if (skillTag == null)
            return "";
        switch (skillTag) {
            case "101":
                return "普工";
            case "102":
                return "技术工";
        }
        return "";
    }

    public String getStrPersonalityTags() {
        if (personalityTags == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Integer personalityTag : personalityTags) {
            switch (personalityTag) {
                case 11:
                    sb.append("不怕脏");
                    break;
                case 12:
                    sb.append("不怕累");
                    break;
                case 13:
                    sb.append("不看显微镜");
                    break;
                case 14:
                    sb.append("不穿无尘服");
                    break;
                case 15:
                    sb.append("希望高返费");
                    break;
                case 16:
                    sb.append("倾向小厂");
                    break;
                case 17:
                    sb.append("倾向大厂");
                    break;
                case 18:
                    sb.append("倾向短期工作");
                    break;
                case 19:
                    sb.append("倾向稳定工作");
                    break;
                case 20:
                    sb.append("要求包吃住");
                    break;
                case 21:
                    sb.append("要求五险一金");
                    break;

            }
        }
        return sb.toString();
    }

    public String getStrWorkingStatus() {
        switch (onboarding) {
            case 1:
                return "在职";
            case 2:
                return "离职";
        }
        return "";
    }

    public String getStrMarital() {
        if (marital == null)
            return "";
        switch (marital) {
            case "MARRIED":
                return "已婚";
            case "UNMARRIED":
                return "未婚";
            case "GOT_ENGAGED":
                return "订婚";
            case "HAVING_BG_FRIEND":
                return "有男女朋友";
            case "SINGLE":
                return "单身";
        }
        return "";
    }

    public String getStrSex() {
        if (sex==null)
            return "";
        switch (sex) {
//            case "0":
//                return "保密";
            case "1":
                return "男";
            case "2":
                return "女";
        }
        return "";
    }
    //中介送人的, 劳务招人的, 企业人事, 内部员工, 重度残疾, 重罪案底, 精神病人, 年龄超过55岁, 新疆维族人, 四川彝族人, 频繁骚扰, 恶意骂人, 其他原因(选择其他则需要填写原因)
    public boolean invalidOtherReasons(String invalidReason){
        boolean temp = invalidReason.equalsIgnoreCase("中介送人的")
                || invalidReason.equalsIgnoreCase("劳务招人的")
                || invalidReason.equalsIgnoreCase("企业人事")
                || invalidReason.equalsIgnoreCase("内部员工")
                || invalidReason.equalsIgnoreCase("重度残疾")
                || invalidReason.equalsIgnoreCase("重罪案底")
                || invalidReason.equalsIgnoreCase("精神病人")
                || invalidReason.equalsIgnoreCase("年龄超过55岁")
                || invalidReason.equalsIgnoreCase("新疆维族人")
                || invalidReason.equalsIgnoreCase("四川彝族人")
                || invalidReason.equalsIgnoreCase("频繁骚扰")
                || invalidReason.equalsIgnoreCase("恶意骂人");
        return !temp;
    }

    /**
     * 标记无效用户请求，是否已通过审核
     * @return
     */
    public boolean invalidRequestPassed(){
        if (invalidStatus == null) return false;
        return invalidStatus == 1;
    }
}
