package cn.xzj.agent.entity.customer

//id (string, optional): ID ,
//onboardingTime (string, optional): 入职时间 ,
//positionName (string, optional): 岗位名称 ,
//quitTime (string, optional): 离职时间 ,
//removable (boolean, optional): 是否可删除
class WorkingRecordInfo(var id:String, var positionName:String, var onboardingTime:Long, var quitTime:Long,var removable:Boolean) {
}