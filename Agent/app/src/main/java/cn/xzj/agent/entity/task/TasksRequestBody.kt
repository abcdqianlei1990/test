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
data class TasksRequestBody(var agentId:String,var pageNo:Int,var pageSize:Int,var taskCategory:Int,var taskTypeId:String)