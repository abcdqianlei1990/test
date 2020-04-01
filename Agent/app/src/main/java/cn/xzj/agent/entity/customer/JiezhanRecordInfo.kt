package cn.xzj.agent.entity.customer

//接站信息 {
//    pickUpAddress (string, optional): 接站地址 ,
//    pickUpId (string, optional): 接站记录ID ,
//    pickUpLocation (string, optional): 接站地点 ,
//    pickUpTime (string, optional): 接站时间 ,
//    positionName (string, optional): 职位名称
//     operatorType (string, optional): 预约人类型: 0 - 用户 1 - 经纪人 ,
//}
data class JiezhanRecordInfo(var pickUpAddress: String
                             , var pickUpId: String
                             , var pickUpLocation: String
                             , var pickUpTime: String
                             , var positionName: String
                             , var operatorType: Int)