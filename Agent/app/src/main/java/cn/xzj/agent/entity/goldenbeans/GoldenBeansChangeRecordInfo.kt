package cn.xzj.agent.entity.goldenbeans

//金豆变更情况 {
//comment (string, optional): 说明 ,
//gold (integer, optional): 金豆数 ,
//updateTime (string, optional): 变更时间
//}
class GoldenBeansChangeRecordInfo(
        var comment:String,
        var gold:Int,
        var updateTime:String
) {
}