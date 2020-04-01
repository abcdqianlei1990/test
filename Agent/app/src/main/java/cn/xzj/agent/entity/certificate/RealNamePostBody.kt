package cn.xzj.agent.entity.certificate

//实名认证 {
//    idCardBackImageUrl (string, optional): 身份证背面图片 ,
//    idCardFrontImageUrl (string, optional): 身份证正面图片 ,
//    idNo (string, optional): 身份证号 ,
//    name (string, optional): 小职姐姓名
//}
class RealNamePostBody(var idCardBackImageUrl:String,var idCardFrontImageUrl:String,var idNo:String,var name:String) {
}