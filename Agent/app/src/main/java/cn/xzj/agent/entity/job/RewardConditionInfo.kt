package cn.xzj.agent.entity.job

//commissionKey (string, optional): 提成Key: REWARD_DAYS_PUNCH - 出勤打卡日, REWARD_DAYS_INSERVICE - 在职日, REWARD_DAYS_WORK - 工作日, REWARD_HOURS_WORK - 出勤小时, HW_SERVICE - 小职姐服务费 ,
//commissionType (string, optional): 提成类型: REWARD - 入职奖励(正常反费), HW - 小时工 ,
//commissionValueFemale (number, optional): 金额 ,
//commissionValueMale (number, optional): 金额 ,
//condition (integer, optional): 条件: 单位为小时或天, 当提成Key为 REWARD_DAYS_PUNCH, REWARD_DAYS_INSERVICE和REWARD_DAYS_WORK时, 单位为日; 当提成Key为 REWARD_HOURS_WORK和HW_SERVICE时, 单位为小时 ,
//stage (integer, optional): 阶段
class RewardConditionInfo(var commissionKey:String,var commissionType:String,var commissionValueFemale:Double,var commissionValueMale:Double,var condition:Int,var stage:Int)