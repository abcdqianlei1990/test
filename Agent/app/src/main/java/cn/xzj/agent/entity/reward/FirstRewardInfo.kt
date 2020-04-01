package cn.xzj.agent.entity.reward

//agentId (string, optional): 经纪人ID ,
//agentName (string, optional): 经纪人名 ,
//agentNickname (string, optional): 经纪人昵称 ,
//amount (number, optional): 提成金额 ,
//cause (string, optional): 奖励来源 ,
//currentLevelCustomerEvent (string, optional): 本级的客户 ,
//day (string, optional): 日期 ,
//lowerLevelAgentId (string, optional): 下级经纪人ID ,
//lowerLevelAgentName (string, optional): 下级经纪人名 ,
//lowerLevelAgentNickname (string, optional): 下级经纪人昵称 ,
//lowerLevelCustomerEvent (string, optional): 下级的客户 ,
//managerId (string, optional): 经理ID ,
//managerNickname (string, optional): 经理 ,
//supervisorId (string, optional): 主管ID ,
//supervisorNickname (string, optional): 主管
class FirstRewardInfo(
        var agentId:String,
        var agentName:String,
        var agentNickname:String,
        var amount:Double,
        var cause:String,
        var currentLevelCustomerEvent:String,
        var day:String,
        var lowerLevelAgentId:String,
        var lowerLevelAgentName:String,
        var lowerLevelAgentNickname:String,
        var lowerLevelCustomerEvent:String,
        var managerId:String,
        var managerNickname:String,
        var supervisorId:String,
        var supervisorNickname:String
){

}