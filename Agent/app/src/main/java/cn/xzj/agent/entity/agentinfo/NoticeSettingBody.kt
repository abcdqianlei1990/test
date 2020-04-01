package cn.xzj.agent.entity.agentinfo

//延迟预约提醒 {
//    remindComment (string, optional): 提醒内容 ,
//    remindTime (string, optional): 提醒时间 ,
//    userId (string, optional): 用户ID
//}
data class NoticeSettingBody(var remindComment:String,var remindTime:String,var userId:String) {
}