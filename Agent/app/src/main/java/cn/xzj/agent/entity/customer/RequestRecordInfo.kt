package cn.xzj.agent.entity.customer

//职位申请 {
//    applyId (string, optional): 申请ID ,
//    applyStatus (integer, optional): 状态: 1-已申请 2-取消申请 10-已预约 11-取消预约 20-已签到 30－以上车 40-已录取 41-未录取 50-已入职 51-未入职 60-已结算返费 97-已外派 98-已转正 99-已离职 ,
//    applyTime (string, optional): 申请时间 ,
//    closed (boolean, optional): 是否关闭: false-未关闭 true-已关闭 ,
//    positionTypes (Array[string], optional): 岗位特性 ,
//    positionId (string, optional): 职位ID ,
//    positionName (string, optional): 职位名称
//}
data class RequestRecordInfo(var applyId:String
                             ,var applyStatus:Int
                             ,var applyTime:String
                             ,var closed:Boolean
                             ,var positionTypes:List<String>
                             ,var positionId:String
                             ,var positionName:String)