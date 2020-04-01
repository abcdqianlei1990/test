package cn.xzj.agent.entity.customer

//客户注记信息 {
//    comment (string, optional): 注记内容 ,
//    communicateMethod (integer, optional): 沟通方式: 0 - 电话, 1 - 微信, 2 - QQ 3，固话 ,
//    operatorName (string, optional): 操作人 ,
//    updateTime (string, optional): 日期
//}
class RemarkHistoryInfo(var comment:String,var communicateMethod:Int,var operatorName:String,var updateTime:String,var communicateResult:Int,var communicateSituation:ArrayList<String>){

    fun getCommunicate() :String{
        var communicate:String = ""
        when(communicateMethod){
            0 -> communicate = "手机"
            1 -> communicate = "微信"
            2 -> communicate = "QQ"
            3 -> communicate = "固话"
        }
        return communicate
    }

    fun getCommunicateResult():String{
        return if (0 == communicateResult) "未接通" else "已接通"
    }

    fun getAppointmentResult():String{
        return if (communicateSituation.contains("RESERVED")) "约好了" else "没约好"
    }

    fun isAppointmentSuccess():Boolean{
        return communicateSituation.contains("RESERVED")
    }
}