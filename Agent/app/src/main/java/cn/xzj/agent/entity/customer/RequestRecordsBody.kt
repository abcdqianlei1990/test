package cn.xzj.agent.entity.customer

//岗位申请查询 {
//    applyStatus (integer, optional): 工作状态: 1-已申请 2-取消申请 10-已预约 11-取消预约 20-已签到 30－以上车 40-已录取 41-未录取 50-已入职 51-未入职 60-已结算返费 97-已外派 98-已转正 99-已离职 = ['1', '2', '10', '11', '20', '30', '40', '41', '50', '51', '60', '97', '98', '99'],
//    closed (boolean, optional): 是否关闭: false-未关闭 true-已关闭 ,
//    pageNo (integer): 页码 ,
//    pageSize (integer): 页大小 ,
//    positionName (string, optional): 岗位名称 ,
//    userId (string, optional): 客户ID
//}
data class RequestRecordsBody(var applyStatus:Int,var closed:Boolean,var pageNo:Int,var pageSize:Int,var positionName:String,var userId:String)