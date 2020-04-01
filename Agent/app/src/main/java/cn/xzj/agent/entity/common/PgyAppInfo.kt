package cn.xzj.agent.entity.common

import com.google.gson.Gson

/**
 * app详细信息，用于蒲公英开放接口
 */
//appKey	String	App Key
//userKey	String	User Key
//appType	Integer	应用类型（1:iOS; 2:Android）
//appIsFirst	Integer	是否是第一个App（1:是; 2:否）
//appIsLastest	Integer	是否是最新版（1:是; 2:否）
//appFileSize	Integer	App 文件大小
//appName	String	应用名称
//appVersion	String	版本号
//appVersionNo	Integer	适用于Android的版本编号，iOS始终为0
//appBuildVersion	Integer	蒲公英生成的用于区分历史版本的build号
//appIdentifier	String	应用程序包名，iOS为BundleId，Android为包名
//appIcon	String	应用的Icon图标key，访问地址为 https://appicon.pgyer.com/image/view/app_icons/[应用的Icon图标key]
//appDescription	String	应用介绍
//appUpdateDescription	String	应用更新说明
//appScreenShots	String	应用截图的key，获取地址为 https://app-screenshot.pgyer.com/image/view/app_screenshots/[应用截图的key]
//appShortcutUrl	String	应用短链接
//appQRCodeURL	String	应用二维码地址
//appCreated	String	应用上传时间
//appUpdated	String	应用更新时间
class PgyAppInfo: PgyBaseAppInfo() {
    var appType:Int? = null
    var appIsFirst:Int? = null
    var appIsLastest:Int? = null
    var appFileSize:Int? = null
    var appVersionNo:Int? = null
    var appIcon:String? = null
    var appDescription:String? = null
    var appScreenShots:String? = null
    var appShortcutUrl:String? = null
    var appQRCodeURL:String? = null
    var appUpdated:String? = null
    var otherapps:ArrayList<PgyBaseAppInfo>?=null
}