package cn.xzj.agent.entity.job;

import java.io.Serializable;
import java.util.List;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/29
 * @Des
 */
public class RecruitmentNeedsInfo implements Serializable {

    /**
     * assembleTime : 2018-12-29T07:19:55.227Z
     * configuredReturnFee : false
     * femaleQuantity : 0
     * headCount : 0
     * hourlyWage : 0
     * hourlyWageUnit : string
     * id : string
     * interviewDate : 2018-12-29T07:19:55.227Z
     * laborFemaleToll : 0
     * laborMaleToll : 0
     * major : false
     * maleQuantity : 0
     * onboardingReward : 0
     * pickUpAddresses : [{"address":"string","location":"string","time":"2018-12-29T07:19:55.227Z"}]
     * platformFemaleSubsidyToll : 0
     * platformFemaleToll : 0
     * platformMaleSubsidyToll : 0
     * platformMaleToll : 0
     * platformSubsidy : 0
     * platformSubsidyDeadline : 2018-12-29T07:19:55.227Z
     * platformSubsidyUnit : string
     * returnConditions : [{"condition":"string","stage":0,"userFemaleFee":0,"userMaleFee":0}]
     * rewardDeadline : 2018-12-29T07:19:55.227Z
     * status : 0
     * totalHourlyWage : 0
     * userFemaleFee : 0
     * userFemaleToll : 0
     * userMaleFee : 0
     * userMaleToll : 0
     * workingReward : 0
     * ApiModelProperty("是否在职: 0 - 否, 1 - 是")
     * Integer incumbencyStatus;
     * ApiModelProperty("预计出名单时间")
     * Integer namesDays;
     */

    private String assembleTime;
    private boolean configuredReturnFee;
    private int femaleQuantity;
    private int headCount;
    private double hourlyWage;
    private String hourlyWageUnit;
    private String id;
    private String interviewDate;
    private double laborFemaleToll;
    private double laborMaleToll;
    private boolean major;
    private double maleQuantity;
    private double onboardingReward;
    private double platformFemaleSubsidyToll;
    private double platformFemaleToll;
    private double platformMaleSubsidyToll;
    private double platformMaleToll;
    private double platformSubsidy;
    private String platformSubsidyDeadline;
    private String platformSubsidyUnit;
    private String rewardDeadline;
    private int status;
    private double totalHourlyWage;
    private double userFemaleFee;
    private double userFemaleToll;
    private double userMaleFee;
    private double userMaleToll;
    private double workingReward;
    private List<PickUpAddressesBean> pickUpAddresses;
    private List<ReturnConditionsBean> returnConditions;
    private Integer incumbencyStatus;
    private Integer namesDays;
    private List<RewardConditionInfo> rewards;

    public String getAssembleTime() {
        return assembleTime;
    }

    public void setAssembleTime(String assembleTime) {
        this.assembleTime = assembleTime;
    }

    public boolean isConfiguredReturnFee() {
        return configuredReturnFee;
    }

    public void setConfiguredReturnFee(boolean configuredReturnFee) {
        this.configuredReturnFee = configuredReturnFee;
    }

    public int getFemaleQuantity() {
        return femaleQuantity;
    }

    public void setFemaleQuantity(int femaleQuantity) {
        this.femaleQuantity = femaleQuantity;
    }

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public String getHourlyWageUnit() {
        return hourlyWageUnit;
    }

    public void setHourlyWageUnit(String hourlyWageUnit) {
        this.hourlyWageUnit = hourlyWageUnit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public double getLaborFemaleToll() {
        return laborFemaleToll;
    }

    public void setLaborFemaleToll(double laborFemaleToll) {
        this.laborFemaleToll = laborFemaleToll;
    }

    public double getLaborMaleToll() {
        return laborMaleToll;
    }

    public void setLaborMaleToll(double laborMaleToll) {
        this.laborMaleToll = laborMaleToll;
    }

    public boolean isMajor() {
        return major;
    }

    public void setMajor(boolean major) {
        this.major = major;
    }

    public double getMaleQuantity() {
        return maleQuantity;
    }

    public void setMaleQuantity(double maleQuantity) {
        this.maleQuantity = maleQuantity;
    }

    public double getOnboardingReward() {
        return onboardingReward;
    }

    public void setOnboardingReward(double onboardingReward) {
        this.onboardingReward = onboardingReward;
    }

    public double getPlatformFemaleSubsidyToll() {
        return platformFemaleSubsidyToll;
    }

    public void setPlatformFemaleSubsidyToll(double platformFemaleSubsidyToll) {
        this.platformFemaleSubsidyToll = platformFemaleSubsidyToll;
    }

    public double getPlatformFemaleToll() {
        return platformFemaleToll;
    }

    public void setPlatformFemaleToll(double platformFemaleToll) {
        this.platformFemaleToll = platformFemaleToll;
    }

    public double getPlatformMaleSubsidyToll() {
        return platformMaleSubsidyToll;
    }

    public void setPlatformMaleSubsidyToll(double platformMaleSubsidyToll) {
        this.platformMaleSubsidyToll = platformMaleSubsidyToll;
    }

    public double getPlatformMaleToll() {
        return platformMaleToll;
    }

    public void setPlatformMaleToll(double platformMaleToll) {
        this.platformMaleToll = platformMaleToll;
    }

    public double getPlatformSubsidy() {
        return platformSubsidy;
    }

    public void setPlatformSubsidy(double platformSubsidy) {
        this.platformSubsidy = platformSubsidy;
    }

    public String getPlatformSubsidyDeadline() {
        return platformSubsidyDeadline;
    }

    public void setPlatformSubsidyDeadline(String platformSubsidyDeadline) {
        this.platformSubsidyDeadline = platformSubsidyDeadline;
    }

    public String getPlatformSubsidyUnit() {
        return platformSubsidyUnit;
    }

    public void setPlatformSubsidyUnit(String platformSubsidyUnit) {
        this.platformSubsidyUnit = platformSubsidyUnit;
    }

    public String getRewardDeadline() {
        return rewardDeadline;
    }

    public void setRewardDeadline(String rewardDeadline) {
        this.rewardDeadline = rewardDeadline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotalHourlyWage() {
        return totalHourlyWage;
    }

    public void setTotalHourlyWage(double totalHourlyWage) {
        this.totalHourlyWage = totalHourlyWage;
    }

    public double getUserFemaleFee() {
        return userFemaleFee;
    }

    public void setUserFemaleFee(double userFemaleFee) {
        this.userFemaleFee = userFemaleFee;
    }

    public double getUserFemaleToll() {
        return userFemaleToll;
    }

    public void setUserFemaleToll(double userFemaleToll) {
        this.userFemaleToll = userFemaleToll;
    }

    public double getUserMaleFee() {
        return userMaleFee;
    }

    public void setUserMaleFee(double userMaleFee) {
        this.userMaleFee = userMaleFee;
    }

    public double getUserMaleToll() {
        return userMaleToll;
    }

    public void setUserMaleToll(double userMaleToll) {
        this.userMaleToll = userMaleToll;
    }

    public double getWorkingReward() {
        return workingReward;
    }

    public void setWorkingReward(double workingReward) {
        this.workingReward = workingReward;
    }

    public List<PickUpAddressesBean> getPickUpAddresses() {
        return pickUpAddresses;
    }

    public void setPickUpAddresses(List<PickUpAddressesBean> pickUpAddresses) {
        this.pickUpAddresses = pickUpAddresses;
    }

    public List<ReturnConditionsBean> getReturnConditions() {
        return returnConditions;
    }

    public void setReturnConditions(List<ReturnConditionsBean> returnConditions) {
        this.returnConditions = returnConditions;
    }

    public static class PickUpAddressesBean implements Serializable {
        /**
         * address : string
         * location : string
         * time : 2018-12-29T07:19:55.227Z
         */

        private String address;
        private String location;
        private String time;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class ReturnConditionsBean implements Serializable {
        /**
         * condition : string
         * stage : 0
         * userFemaleFee : 0
         * userMaleFee : 0
         */

        private String condition;
        private int stage;
        private int userFemaleFee;
        private int userMaleFee;

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public int getUserFemaleFee() {
            return userFemaleFee;
        }

        public void setUserFemaleFee(int userFemaleFee) {
            this.userFemaleFee = userFemaleFee;
        }

        public int getUserMaleFee() {
            return userMaleFee;
        }

        public void setUserMaleFee(int userMaleFee) {
            this.userMaleFee = userMaleFee;
        }
    }

    public Integer getIncumbencyStatus() {
        return incumbencyStatus;
    }

    public void setIncumbencyStatus(Integer incumbencyStatus) {
        this.incumbencyStatus = incumbencyStatus;
    }

    public Integer getNamesDays() {
        return namesDays;
    }

    public void setNamesDays(Integer namesDays) {
        this.namesDays = namesDays;
    }

    public List<RewardConditionInfo> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardConditionInfo> rewards) {
        this.rewards = rewards;
    }
}
