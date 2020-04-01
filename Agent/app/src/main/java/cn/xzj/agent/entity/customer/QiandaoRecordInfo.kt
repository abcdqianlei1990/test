package cn.xzj.agent.entity.customer

//签到信息 {
//    positionName (string, optional): 岗位名称 ,
//    returnFee (number, optional): 返费金额 ,
//    signInTime (string, optional): 签到时间 ,
//    storeName (string, optional): 门店
//    positionTypes (Array[string], optional): 岗位特性 ,
//}
data class QiandaoRecordInfo(var positionName:String,var returnFee:Double,var signInTime:String,var storeName:String,var positionTypes:List<String>?)