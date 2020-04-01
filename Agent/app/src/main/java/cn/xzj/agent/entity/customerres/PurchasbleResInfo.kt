package cn.xzj.agent.entity.customerres
//
//可采购的用户 {
//    desc (string, optional): 描述 ,
//    gold (integer, optional): 金豆数 ,
//    userId (string, optional): 客户ID
//}
class PurchasbleResInfo(var desc:String, var gold:Int, var userId:String,var selected:Boolean = false) {
}