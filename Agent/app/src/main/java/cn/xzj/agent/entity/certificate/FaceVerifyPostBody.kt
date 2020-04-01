package cn.xzj.agent.entity.certificate

//刷脸认证 {
//    delta (string, optional): Delta ,
//    faceImageUrl (string, optional): 人脸图片 ,
//    faceWithBgImageUrl (string, optional): 背景人脸图片
//}
class FaceVerifyPostBody(var delta:String, var faceImageUrl:String, var faceWithBgImageUrl:String) {
}