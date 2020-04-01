package cn.xzj.agent.entity.common

//预约(支付)订单信息 {
//    amount (string, optional): 应付费用 ,
//    baitiaoStatus (integer, optional): 白条状态 0 未认证 1 可使用 2 不可使用 ,
//    end (long, optional): 付款截止时间 ,
//    femaleFee (string, optional): 女费用 ,
//    id (string, optional): ID ,
//    maleFee (string, optional): 男费用 ,
//    payStatus (integer, optional): 支付状态 0 未支付 1 支付中 2 已支付 ,
//    paymentDate (string, optional): 支付日期 ,
//    returnAmount (string, optional): 退款金额 ,
//    returnDate (string, optional): 退款日期 ,
//    returnStatus (integer, optional): 退款状态 0 未退款 2 已退款 3 退款失败
//    paymentType (string, optional): 支付方式 WECHAT 微信支付，BAITIAO 白条支付{@LINK PaymentMethodInfo}
//    realname (boolean, optional): 是否需要认证
//}
class PaymentOrderInfo(
        var positionName:String,
        var amount:String,
        var baitiaoStatus:Int,
        var end:Long,
        var femaleFee:String,
        var id:String,
        var maleFee:String,
        var paymentStatus:Int,
        var paymentDate:String,
        var returnAmount:String,
        var returnDate:String,
        var returnStatus:Int,
        var paymentType:String,
        var needRealname:Boolean //是否需要认证
){
    companion object {
        //白条状态
        val BAITIAO_STATUS_UNCERTIFIED = 0    //未认证
        val BAITIAO_STATUS_ENABLED = 1  //可使用
        val BAITIAO_STATUS_DISABLED = 2 //不可使用

        //支付状态
        val PAY_STATUS_UNPAID = 0   //未支付
        val PAY_STATUS_PAYING = 1    //支付中
        val PAY_STATUS_PAID = 2  //已支付

        //退款状态
        val RETURN_STATUS_RETURNING = 1 //退款中
        val RETURN_STATUS_UNRETURN = 0    //未退款（默认值）
        val RETURN_STATUS_RETURNED = 2   //已退款
        val RETURN_STATUS_RETURN_FAILED = 3   //退款失败
    }
}