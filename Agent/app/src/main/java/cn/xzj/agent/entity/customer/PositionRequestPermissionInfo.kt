package cn.xzj.agent.entity.customer

/**
 * applied (boolean, optional): 是否申请过 ,
contractAgreed (boolean, optional): 是否同意过协议
 */
data class PositionRequestPermissionInfo(var contractAgreed:Boolean,var applied:Boolean)