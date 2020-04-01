package cn.xzj.agent.entity.payment


/**
 * @param orderId 购买记录ID
 * @param status 状态: 1 - 支付中, 2 - 支付失败, 3 - 支付成功
 */
class PaymentStatusInfo(var orderId:String, var status:String) {
    companion object{
        val PAYMENT_STATUS_PAYING = "1"
        val PAYMENT_STATUS_FAILURE = "2"
        val PAYMENT_STATUS_SUCCESS = "3"
    }
}