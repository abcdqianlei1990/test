package cn.xzj.agent.entity.privacy

/**
 * 微信联系人信息
 * @param wechatId 微信id
 * @param wechatAlias 微信号
 * @param wechatNoteName 微信备注名
 */
data class WechatContactInfo(var wechatId:String, var wechatAlias:String, var wechatNoteName:String)