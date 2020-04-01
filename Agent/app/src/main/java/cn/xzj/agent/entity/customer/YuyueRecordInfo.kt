package cn.xzj.agent.entity.customer

/**
 * 职位预约 {
comment (string, optional): 备注 ,
configuredReturnFee (boolean, optional): 是否有反费 ,
operatorType (integer, optional): 预约人类型: 0 - 用户 1 - 经纪人 ,
paymentAmount (number, optional): 支付金额 ,
paymentDate (string, optional): 支付时间 ,
paymentStatus (integer, optional): 支付状态: 0 - 未支付, 1 - 支付中, 2 - 已支付, 3 - 已取消, 4 - 支付失败 ,
paymentType (string, optional): 支付类型: WECHAT - 微信, BAITIAO - 白条 ,
positionId (string, optional): 岗位ID ,
positionName (string, optional): 岗位名称 ,
refundAmount (number, optional): 退款金额 ,
refundDate (string, optional): 退款时间 ,
refundStatus (integer, optional): 退款状态: 0 - 未退款, 1 - 审核中, 2 - 退款中, 3 - 审核拒绝, 4 - 已退款, 5 - 退款失败 ,
refundType (string, optional): 退款类型: WECHAT - 微信, BAITIAO - 白条 ,
reserveDate (string, optional): 预约签到时间 ,
reserveId (string, optional): 预约ID ,
signInLocation (string, optional): 签到地点 ,
status (integer, optional): 状态: 0 - 取消, 1 - 已签到, 2 - 未签到
}
 */
data class YuyueRecordInfo(var comment: String, var positionId: String, var positionName: String, var reserveDate: String, var reserveId: String, var signInLocation: String, var operatorType: Int, var configuredReturnFee: Boolean,var paymentStatus:Int?=null,var refundStatus:Int?=null,var status:Int?=null)