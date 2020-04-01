package cn.xzj.agent.entity.customer

//添加客户注记
//{
//    "comment": "string",  //注记内容
//    "communicateMethod": 0,   //沟通方式: 0 - 手机, 1 - 微信, 2 - QQ, 3 - 固化
//    "communicateResult": 0,   //沟通结果: 0 - 未接通, 1 - 接通
//    "communicateSituation": "string", //沟通情况
//    "reason": [   //没约好的原因
//    "string"
//    ],
//    "userId": "string"
//}
data class NoteRemarkBodyNew(
        var comment:String,
        var communicateMethod:Int,
        var communicateResult:Int,
        var communicateSituation:String,
        var reason:ArrayList<String>,
        var userId:String
)