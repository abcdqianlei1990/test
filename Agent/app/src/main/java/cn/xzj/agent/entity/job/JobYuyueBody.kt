package cn.xzj.agent.entity.job

//推荐岗位 {
//    applyId (string, optional): 处理申请ID ,
//    comment (string, optional): 备注 ,
//    assembleTime (string, optional): 集合时间  ,
//    interviewAddress (string, optional): 面试地址 ,
//    interviewDate (string, optional): 面试时间 ,
//    laborId (string, optional): 劳务公司ID ,
//    laborName (string, optional): 劳务公司名称 ,
//    needPickUp (boolean, optional): 是否需要接站 ,
//    pickUpAddress (string, optional): 接站地址 ,
//    pickUpLocation (string, optional): 接站地点 ,
//    pickUpTime (string, optional): 接站时间 ,
//    positionId (string, optional): 处理岗位ID ,
//    positionName (string, optional): 处理岗位名称 ,
//    reserve (boolean, optional): 是否预约 ,
//    signInTime (string, optional): 签到时间 ,（预约时间）
//    source (string, optional): 来源 ,
//    storeId (string, optional): 门店ID ,
//    storeName (string, optional): 门店名称 ,
//    userId (string, optional): 用户ID
//}
data class JobYuyueBody(var applyId:String?
                        ,var comment:String
                        ,var interviewAddress:String
                        ,var interviewDate:String
                        ,var laborId:String
                        ,var laborName:String
                        ,var needPickUp:Boolean
                        ,var pickUpAddress:String
                        ,var pickUpLocation:String
                        ,var pickUpTime:String
                        ,var positionId:String
                        ,var positionName:String
                        ,var reserve:Boolean
                        ,var signInTime:String
                        ,var source:String
                        ,var storeId:String
                        ,var storeName:String
                        ,var userId:String
                        ,var assembleTime:String?) {
}