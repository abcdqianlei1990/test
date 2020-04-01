package cn.xzj.agent.entity.company;

import java.util.List;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/28
 * @Des
 */
public class CompanyJobInfo {

    /**
     * companyShortName : string
     * companySn : string
     * interviewRequirements : string
     * maxSalary : 0
     * minSalary : 0
     * notice : string
     * positionFeatures : ["string"]
     * positionId : string
     * positionName : string
     * positionTypes : ["string"]
     * privateInfo : string
     * recruitmentNeeds : [{"assembleTime":"2018-12-28T06:40:53.271Z","configuredReturnFee":false,"femaleQuantity":0,"headCount":0,"hourlyWage":0,"id":"string","interviewDate":"2018-12-28T06:40:53.271Z","laborFemaleToll":0,"laborMaleToll":0,"major":false,"maleQuantity":0,"onboardingReward":0,"pickUpAddresses":[{"address":"string","location":"string","time":"2018-12-28T06:40:53.271Z"}],"platformFemaleSubsidyToll":0,"platformFemaleToll":0,"platformMaleSubsidyToll":0,"platformMaleToll":0,"platformSubsidy":0,"returnConditions":[{"condition":"string","stage":0,"userFemaleFee":0,"userMaleFee":0}],"rewardDeadline":"2018-12-28T06:40:53.271Z","status":0,"totalHourlyWage":0,"userFemaleFee":0,"userFemaleToll":0,"userMaleFee":0,"userMaleToll":0,"workingReward":0}]
     * requirement : string
     * status : 0
     */

    private String companyShortName;
    private String companySn;
    private String interviewRequirements;
    private int maxSalary;
    private int minSalary;
    private String notice;
    private String positionId;
    private String positionName;
    private String privateInfo;
    private String requirement;
    private int status;
    private List<String> positionFeatures;
    private List<String> positionTypes;
    private List<RecruitmentNeedsBean> recruitmentNeeds;

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getCompanySn() {
        return companySn;
    }

    public void setCompanySn(String companySn) {
        this.companySn = companySn;
    }

    public String getInterviewRequirements() {
        return interviewRequirements;
    }

    public void setInterviewRequirements(String interviewRequirements) {
        this.interviewRequirements = interviewRequirements;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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

    public String getPrivateInfo() {
        return privateInfo;
    }

    public void setPrivateInfo(String privateInfo) {
        this.privateInfo = privateInfo;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getPositionFeatures() {
        return positionFeatures;
    }

    public void setPositionFeatures(List<String> positionFeatures) {
        this.positionFeatures = positionFeatures;
    }

    public List<String> getPositionTypes() {
        return positionTypes;
    }

    public void setPositionTypes(List<String> positionTypes) {
        this.positionTypes = positionTypes;
    }

    public List<RecruitmentNeedsBean> getRecruitmentNeeds() {
        return recruitmentNeeds;
    }

    public void setRecruitmentNeeds(List<RecruitmentNeedsBean> recruitmentNeeds) {
        this.recruitmentNeeds = recruitmentNeeds;
    }

    public static class RecruitmentNeedsBean {
        /**
         * assembleTime : 2018-12-28T06:40:53.271Z
         * configuredReturnFee : false
         * femaleQuantity : 0
         * headCount : 0
         * hourlyWage : 0
         * id : string
         * interviewDate : 2018-12-28T06:40:53.271Z
         * laborFemaleToll : 0
         * laborMaleToll : 0
         * major : false
         * maleQuantity : 0
         * onboardingReward : 0
         * pickUpAddresses : [{"address":"string","location":"string","time":"2018-12-28T06:40:53.271Z"}]
         * platformFemaleSubsidyToll : 0
         * platformFemaleToll : 0
         * platformMaleSubsidyToll : 0
         * platformMaleToll : 0
         * platformSubsidy : 0
         * returnConditions : [{"condition":"string","stage":0,"userFemaleFee":0,"userMaleFee":0}]
         * rewardDeadline : 2018-12-28T06:40:53.271Z
         * status : 0
         * totalHourlyWage : 0
         * userFemaleFee : 0
         * userFemaleToll : 0
         * userMaleFee : 0
         * userMaleToll : 0
         * workingReward : 0
         */

        private String assembleTime;
        private boolean configuredReturnFee;
        private int femaleQuantity;
        private int headCount;
        private int hourlyWage;
        private String id;
        private String interviewDate;
        private int laborFemaleToll;
        private int laborMaleToll;
        private boolean major;
        private int maleQuantity;
        private int onboardingReward;
        private int platformFemaleSubsidyToll;
        private int platformFemaleToll;
        private int platformMaleSubsidyToll;
        private int platformMaleToll;
        private int platformSubsidy;
        private String rewardDeadline;
        private int status;
        private int totalHourlyWage;
        private int userFemaleFee;
        private int userFemaleToll;
        private int userMaleFee;
        private int userMaleToll;
        private int workingReward;
        private List<PickUpAddressesBean> pickUpAddresses;
        private List<ReturnConditionsBean> returnConditions;

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

        public int getHourlyWage() {
            return hourlyWage;
        }

        public void setHourlyWage(int hourlyWage) {
            this.hourlyWage = hourlyWage;
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

        public int getLaborFemaleToll() {
            return laborFemaleToll;
        }

        public void setLaborFemaleToll(int laborFemaleToll) {
            this.laborFemaleToll = laborFemaleToll;
        }

        public int getLaborMaleToll() {
            return laborMaleToll;
        }

        public void setLaborMaleToll(int laborMaleToll) {
            this.laborMaleToll = laborMaleToll;
        }

        public boolean isMajor() {
            return major;
        }

        public void setMajor(boolean major) {
            this.major = major;
        }

        public int getMaleQuantity() {
            return maleQuantity;
        }

        public void setMaleQuantity(int maleQuantity) {
            this.maleQuantity = maleQuantity;
        }

        public int getOnboardingReward() {
            return onboardingReward;
        }

        public void setOnboardingReward(int onboardingReward) {
            this.onboardingReward = onboardingReward;
        }

        public int getPlatformFemaleSubsidyToll() {
            return platformFemaleSubsidyToll;
        }

        public void setPlatformFemaleSubsidyToll(int platformFemaleSubsidyToll) {
            this.platformFemaleSubsidyToll = platformFemaleSubsidyToll;
        }

        public int getPlatformFemaleToll() {
            return platformFemaleToll;
        }

        public void setPlatformFemaleToll(int platformFemaleToll) {
            this.platformFemaleToll = platformFemaleToll;
        }

        public int getPlatformMaleSubsidyToll() {
            return platformMaleSubsidyToll;
        }

        public void setPlatformMaleSubsidyToll(int platformMaleSubsidyToll) {
            this.platformMaleSubsidyToll = platformMaleSubsidyToll;
        }

        public int getPlatformMaleToll() {
            return platformMaleToll;
        }

        public void setPlatformMaleToll(int platformMaleToll) {
            this.platformMaleToll = platformMaleToll;
        }

        public int getPlatformSubsidy() {
            return platformSubsidy;
        }

        public void setPlatformSubsidy(int platformSubsidy) {
            this.platformSubsidy = platformSubsidy;
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

        public int getTotalHourlyWage() {
            return totalHourlyWage;
        }

        public void setTotalHourlyWage(int totalHourlyWage) {
            this.totalHourlyWage = totalHourlyWage;
        }

        public int getUserFemaleFee() {
            return userFemaleFee;
        }

        public void setUserFemaleFee(int userFemaleFee) {
            this.userFemaleFee = userFemaleFee;
        }

        public int getUserFemaleToll() {
            return userFemaleToll;
        }

        public void setUserFemaleToll(int userFemaleToll) {
            this.userFemaleToll = userFemaleToll;
        }

        public int getUserMaleFee() {
            return userMaleFee;
        }

        public void setUserMaleFee(int userMaleFee) {
            this.userMaleFee = userMaleFee;
        }

        public int getUserMaleToll() {
            return userMaleToll;
        }

        public void setUserMaleToll(int userMaleToll) {
            this.userMaleToll = userMaleToll;
        }

        public int getWorkingReward() {
            return workingReward;
        }

        public void setWorkingReward(int workingReward) {
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

        public static class PickUpAddressesBean {
            /**
             * address : string
             * location : string
             * time : 2018-12-28T06:40:53.271Z
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

        public static class ReturnConditionsBean {
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
    }
}
