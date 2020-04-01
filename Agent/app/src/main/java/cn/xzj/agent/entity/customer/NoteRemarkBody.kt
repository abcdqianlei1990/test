package cn.xzj.agent.entity.customer

//添加客户注记 {
//    comment (string, optional): 注记内容 ,
//    communicateMethod (integer, optional): 沟通方式: 0 - 手机, 1 - 微信, 2 - QQ, 3 - 固化  ,
//    userId (string, optional): 客户ID
//}
data class NoteRemarkBody(var comment:String,var communicateMethod:Int,var userId:String)