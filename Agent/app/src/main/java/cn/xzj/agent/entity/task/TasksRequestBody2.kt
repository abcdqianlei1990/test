package cn.xzj.agent.entity.task

//{
//    "agentId": "string",
//    "pageNo": 0,
//    "pageSize": 0,
//    "startTimeFrom": "2018-07-04T12:46:16.150Z",
//    "startTimeTo": "2018-07-04T12:46:16.150Z",
//    "taskCategory": 0,
//    "taskTypeId": "string",
//    "userId": "string"
//}
/**
 * @param contactStatus 联系状态：0 - 未联系, 1 - 未取得联系
,
 */
data class TasksRequestBody2(var agentId:String,
                             var pageNo:Int,
                             var pageSize:Int,
                             var taskCategory:Int,
                             var contactStatus:Int?,
                             var taskTypeId:String,
                             var startTimeFrom:String,
                             var startTimeTo:String){


   companion object {
       const val CONTACT_STATUS_0 = 0
       const val CONTACT_STATUS_1 = 1
   }
}