package cn.xzj.agent.entity.privacy

/**
 * 微信聊天记录
 * @param type
 * @param status
 * @param send
 * @param sendTime create time
 * @param talker
 * @param content
 */
data class WeChatRecordInfo(var type:String,var status:String,var send:String,var sendTime:String,var talker:String,var content:String?)