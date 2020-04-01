package cn.xzj.agent.entity.customer

//可分配客户信息
//agentId (string, optional): 经纪人ID ,
//agentName (string, optional): 经纪人名 ,
//nickname (string, optional): 客户常用名 ,
//lastContactComment (string, optional):  最后注纪内容 ,
//lastContactTime (string, optional): 最后联系时间 ,
//phone (string, optional): 客户手机号 ,
//referrerName (string, optional): 推荐人姓名 ,
//registerTime (string, optional): 注册时间 ,
//source (string, optional): 来源 ,
//userId (string, optional): 客户ID ,
//userName (string, optional): 客户名 ,
//weixin (string, optional): 微信
//wish (string, optional): 意愿
//matchTime 分配时间
data class OriginalCustomerInfo(var agentId:String
                                ,var agentName:String
                                ,var nickname:String
                                ,var lastContactComment:String
                                ,var lastContactTime:String
                                ,var phone:String
                                ,var referrerName:String
                                ,var registerTime:String
                                ,var source:String
                                ,var sourceName:String
                                ,var userId:String
                                ,var userName:String
                                ,var weixin:String
                                ,var wish:String
                                ,var rank:String,var matchTime:String?) {
    companion object {
        val INTENT_EXTREMELY_HIGH = "EXTREMELY_HIGH"
        val INTENT_HIGH = "HIGH"
        val INTENT_MEDIUM = "MEDIUM"
        val INTENT_LOW = "LOW"
        val INTENT_NONE = "NONE"
    }
}