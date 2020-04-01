package cn.xzj.agent.entity.customerres

//下单结果 {
//    failureInOrder (Array[OrderCustomer], optional): 下单失败的客户 ,
//    remainingQuantity (integer, optional): 还可以购买的用户数
//}
class ResPurchaseResp(var failureInOrder:ArrayList<OrderCustomerInfo>,var remainingQuantity:Int) {
}