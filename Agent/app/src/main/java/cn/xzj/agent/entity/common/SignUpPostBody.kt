package cn.xzj.agent.entity.common

/**
 * 客户注册post body
 * @param storeId 门店id
 * @param storeName 门店名称
 * @param captcha 验证码
 * @param idNo 身份证号
 * @param name 用户姓名
 * @param phone 手机号
 * @param sex 性别: 1 - 男 2 - 女
 */
data class SignUpPostBody(var storeId:String?,var storeName:String?,var captcha:String,var idNo:String?,var name:String,var phone:String,var sex:String)