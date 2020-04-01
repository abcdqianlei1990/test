package cn.xzj.agent.entity.job

//返费规则 {
//    condition (string, optional): 奖励规则 ,
//    stage (integer, optional): 阶段 ,
//    userFemaleFee (number, optional): 女士的返费 ,
//    userMaleFee (number, optional): 男士的返费
//}
data class ReturnConditionInfo(var condition:String, var stage:Int, var userFemaleFee:Double, var userMaleFee:Double)