package cn.xzj.agent.entity.agentinfo

//消息 {
//    content (string, optional): 内容 ,
//    status (integer, optional): 阅读状态: 0 - 未读, 1 - 已读 ,
//    time (string, optional): 时间 ,
//    title (string, optional): 标题
//}
//data class MsgInfo(var content:String,var status:Int,var time:String,var title:String)




//经纪人任务 {
//    agentId (string, optional): 经纪人ID ,
//    agentName (string, optional): 经纪人名 ,
//    agentNickname (string, optional): 经纪人昵称 ,
//    completeTime (string, optional): 完成时间 ,
//    contactStatus (integer, optional): 联系状态：0 - 未联系, 1 - 未取得联系 = ['0', '1'],
//    createTime (string, optional): 创建时间 ,
//    dueTime (string, optional): 到期时间 ,
//    nickname (string, optional): 客户常用名 ,
//    startTime (string, optional): 开始时间 ,
//    status (string, optional): 任务状态: 0-待处理，19-已完成，29-已关闭 ,
//    taskDescription (string, optional): 任务描述 ,
//    taskId (string, optional): 任务ID ,
//    taskNo (string, optional): 任务编号 ,
//    taskTypeId (string, optional): 任务类型ID ,
//    taskTypeName (string, optional): 任务类型名 ,
//    updateTime (string, optional): 更新时间 ,
//    userId (string, optional): 客户ID ,
//    userName (string, optional): 客户名 ,
//    userPhone (string, optional): 客户手机 ,
//    userRank (string, optional): 客户等级
//}
data class MsgInfo(var taskTypeName:String,var userName:String,var userPhone:String,var taskDescription:String,var userId:String)