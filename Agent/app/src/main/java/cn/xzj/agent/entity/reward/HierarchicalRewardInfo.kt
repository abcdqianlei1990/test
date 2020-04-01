package cn.xzj.agent.entity.reward

//agentId (string, optional): 经纪人ID ,
//agentName (string, optional): 经纪人名 ,
//agentNickname (string, optional): 经纪人昵称 ,
//agentType (string, optional): 小职姐类型: FULL_AGENT - 全职小职姐, PART_AGENT - 兼职小职姐, OUTSOURCING_AGENT - 外包小职姐, FRANCHISEE_AGENT - 加盟商小职姐 ,
//amount (number, optional): 提成金额 ,
//day (string, optional): 日期 ,
//managerId (string, optional): 经理ID ,
//managerNickname (string, optional): 经理 ,
//nickname (string, optional): 客户常用名 ,
//onboardingStatus (integer, optional): 入职状态: 0 - 未入职, 1 - 入职, 2 - 离职 ,
//onboardingTime (string, optional): 入职时间 ,
//phone (string, optional): 用户手机 ,
//positionId (string, optional): 岗位ID ,
//positionName (string, optional): 岗位名称 ,
//quitTime (string, optional): 离职时间 ,
//rewardMode (string, optional): 提成模式 HOURLY_WORK - 小时工, NORMAL_RETURN - 正常返费, DAILY_SALARY - 日薪, WEEKLY_SALARY - 周薪, DAILY_RETURN - 日日返,
//supervisorId (string, optional): 主管ID ,
//supervisorNickname (string, optional): 主管 ,
//userId (string, optional): 客户ID ,
//userName (string, optional): 客户名
//lowerLevelAgentId (string, optional): 下级经纪人ID ,
//lowerLevelAgentName (string, optional): 下级经纪人名 ,
//lowerLevelAgentNickname (string, optional): 下级经纪人昵称 ,
//rewardCondition (string, optional): 提成条件 ,
class HierarchicalRewardInfo(
        var agentId:String,
        var agentName:String,
        var agentNickname:String,
        var agentType:String,
        var amount:Double,
        var day:String,
        var managerId:String,
        var managerNickname:String,
        var nickname:String,
        var onboardingStatus:Int,
        var onboardingTime:String,
        var phone:String,
        var positionId:String,
        var positionName:String,
        var quitTime:String,
        var rewardMode:String,
        var supervisorId:String,
        var supervisorNickname:String,
        var userId:String,
        var userName:String,
        var lowerLevelAgentId:String,
        var lowerLevelAgentName:String,
        var lowerLevelAgentNickname:String,
        var rewardCondition:String
){
    companion object{
        val reward_mode_hourlyWork = "HOURLY_WORK"
        val reward_mode_normalRet = "NORMAL_RETURN"
        val reward_mode_dailySalary = "DAILY_SALARY"
        val reward_mode_weeklySalary = "WEEKLY_SALARY"
        val reward_mode_dailyRet = "DAILY_RETURN"
    }
    fun getRewardModeStr():String{
        var str:String
        when(this.rewardMode){
            reward_mode_hourlyWork -> str = "小时工"
            reward_mode_normalRet -> str = "正常返费"
            reward_mode_dailySalary -> str = "日薪"
            reward_mode_weeklySalary -> str = "周薪"
            reward_mode_dailyRet -> str = "日日返"
            else -> str = "正常返费"
        }
        return str
    }
}