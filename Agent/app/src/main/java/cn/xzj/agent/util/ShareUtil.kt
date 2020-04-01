package cn.xzj.agent.util


import android.content.Context

import java.util.HashMap

import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
//import cn.sharesdk.sina.weibo.SinaWeibo
//import cn.sharesdk.tencent.qq.QQ
//import cn.sharesdk.tencent.qzone.QZone
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import cn.xzj.agent.entity.common.ShareInfo
import com.channey.utils.StringUtils
import com.channey.utils.ToastUtils

//import cn.sharesdk.wechat.favorite.WechatFavorite;
//import cn.sharesdk.wechat.moments.WechatMoments;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by channey on 2017/2/4.
 * 分享工具类
 */

object ShareUtil {
    val SHARE_TYPE_QQ = "qq"
    val SHARE_TYPE_WECHAT = "weChat"
    val SHARE_TYPE_MOMENTS = "wxMoments"
    val SHARE_TYPE_QZONE = "QZone"
    val SHARE_TYPE_SINA = "sina"

    fun share(context: Context, shareInfo: ShareInfo, listener: ShareSuccessListener) {
        val platform = shareInfo.targetPlatform
        if (!StringUtils.isEmpty(platform)){
            if (platform.equals(SHARE_TYPE_WECHAT,true)){
                shareWeChat(context, shareInfo, listener=listener)
            }else if (platform.equals(SHARE_TYPE_MOMENTS,true)){
                shareWexinMoments(context, shareInfo, listener=listener)
            }else if (platform.equals(SHARE_TYPE_QQ,true)){
                shareQQ(context, shareInfo, listener=listener)
            }else if (platform.equals(SHARE_TYPE_QZONE,true)){
                shareQZone(context, shareInfo, listener=listener)
            }else if (platform.equals(SHARE_TYPE_SINA,true)){
                shareSina(context, shareInfo, listener=listener)
            }else{

            }
        }else{
            ToastUtils.showToast(context,"未指定分享平台")
        }
    }

    /**
     *
     * @param context
     * @param info
     */
    fun shareWeChat(context: Context, info: ShareInfo,listener: ShareSuccessListener) {
        val platform = ShareSDK.getPlatform(Wechat.NAME)
        val shareParams = Platform.ShareParams()
        shareParams.text = info.content
        shareParams.title = info.title
        if (!StringUtils.isEmpty(info.rewardType)){
            shareParams.url = "${info.targetUrl}?${info.rewardType}"
        }else{
            shareParams.url = info.targetUrl
        }
        shareParams.imageUrl = info.imgUrl
        var shareType = Platform.SHARE_WEBPAGE
        if (!StringUtils.isEmpty(info.wxPath) && !StringUtils.isEmpty(info.wxUserName)){
            shareParams.wxUserName = info.wxUserName
            shareParams.wxPath = info.wxPath
            shareType = Platform.SHARE_WXMINIPROGRAM
        }
        shareParams.shareType = shareType
        platform.platformActionListener = object : PlatformActionListener {
            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
                showToast(context,"分享成功")
                listener.onShareSuccess(SHARE_TYPE_WECHAT)
            }

            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
                throwable.printStackTrace()
                if (platform.isClientValid) {
                    showToast(context,"目前您的微信版本过低或未安装微信，需要安装微信才能使用")
                } else {
                    showToast(context,"微信分享失败")
                }
            }

            override fun onCancel(platform: Platform, i: Int) {
                showToast(context,"取消分享")
            }
        }
        platform.share(shareParams)
    }

    fun shareWexinMoments(context: Context, info: ShareInfo,listener: ShareSuccessListener) {
        val shareParams = Platform.ShareParams()
        shareParams.title = info.title
        shareParams.text = info.content
        shareParams.imageUrl = info.imgUrl
        if (!StringUtils.isEmpty(info.rewardType)){
            shareParams.url = "${info.targetUrl}?${info.rewardType}"
        }else{
            shareParams.url = info.targetUrl
        }
        shareParams.shareType = Platform.SHARE_WEBPAGE

        val momentsPlatform = ShareSDK.getPlatform(WechatMoments.NAME)
        momentsPlatform.platformActionListener = object : PlatformActionListener {
            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
                showToast(context,"分享成功")
                listener.onShareSuccess(SHARE_TYPE_MOMENTS)
            }

            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
                throwable.printStackTrace()
                if (platform.isClientValid) {
                    showToast(context,"目前您的微信版本过低或未安装微信，需要安装微信才能使用")
                } else {
                    showToast(context,"微信分享失败")
                }
            }

            override fun onCancel(platform: Platform, i: Int) {
                showToast(context,"取消分享")
            }
        }
        momentsPlatform.share(shareParams)
    }

    fun shareQQ(context: Context, info: ShareInfo, listener: ShareSuccessListener) {
//        val shareParams = Platform.ShareParams()
//        shareParams.title = info.title
//        shareParams.imageUrl = info.imgUrl
//        if (!StringUtils.isEmpty(info.rewardType)){
//            shareParams.url = "${info.targetUrl}?${info.rewardType}"
//            shareParams.titleUrl = "${info.targetUrl}?${info.rewardType}"
//        }else{
//            shareParams.url = info.targetUrl
//            shareParams.titleUrl = info.targetUrl
//        }
//        shareParams.text = info.content
//        //        shareParams.setShareType(Platform.SHARE_WEBPAGE);
//
//        val plat = ShareSDK.getPlatform(QQ.NAME)
//        plat.platformActionListener = object : PlatformActionListener {
//            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
//                showToast(context,"分享成功")
//                listener.onShareSuccess(SHARE_TYPE_QQ)
//            }
//
//            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
//                throwable.printStackTrace()
//                if (platform.isClientValid) {
//                    showToast(context,"目前您的QQ版本过低或未安装QQ，需要安装QQ才能使用")
//                } else {
//                    showToast(context,"QQ分享失败")
//                }
//            }
//
//            override fun onCancel(platform: Platform, i: Int) {
//                showToast(context,"取消分享")
//            }
//        }
//
//        plat.share(shareParams)
    }

    fun shareQZone(context: Context, info: ShareInfo,listener: ShareSuccessListener) {
//        val shareParams = Platform.ShareParams()
//        shareParams.title = info.title
//        shareParams.imageUrl = info.imgUrl
//        if (!StringUtils.isEmpty(info.rewardType)){
//            shareParams.url = "${info.targetUrl}?${info.rewardType}"
//            shareParams.titleUrl = "${info.targetUrl}?${info.rewardType}"
//        }else{
//            shareParams.url = info.targetUrl
//            shareParams.titleUrl = info.targetUrl
//        }
//        shareParams.text = info.content
//        shareParams.shareType = Platform.SHARE_WEBPAGE
//        shareParams.site = "小职姐"
//        shareParams.siteUrl = "http://www.lxworker.com/"
//
//        val plat = ShareSDK.getPlatform(QZone.NAME)
//        plat.platformActionListener = object : PlatformActionListener {
//            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
//                showToast(context,"分享成功")
//                listener.onShareSuccess(SHARE_TYPE_QZONE)
//            }
//
//            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
//                throwable.printStackTrace()
//                if (platform.isClientValid) {
//                    showToast(context,"目前您的QQ版本过低或未安装QQ，需要安装QQ才能使用")
//                } else {
//                    showToast(context,"QQ空间分享失败")
//                }
//            }
//
//            override fun onCancel(platform: Platform, i: Int) {
//                showToast(context,"取消分享")
//            }
//        }
//
//        plat.share(shareParams)
    }

    fun shareSina(context: Context, info: ShareInfo, listener: ShareSuccessListener) {
//        val shareParams = Platform.ShareParams()
//        var targetUrl:String
//        if (!StringUtils.isEmpty(info.rewardType)){
//            targetUrl = "${info.targetUrl}?${info.rewardType}"
//        }else{
//            targetUrl = info.targetUrl
//        }
//        shareParams.text = "${info.content} $targetUrl"
//        shareParams.imagePath = info.imgUrl
//        shareParams.imageUrl = info.imgUrl
//        val weibo = ShareSDK.getPlatform(SinaWeibo.NAME)
//        weibo.platformActionListener = object : PlatformActionListener {
//            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
//                showToast(context,"分享成功")
//                listener.onShareSuccess(SHARE_TYPE_SINA)
//            }
//
//            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
//                throwable.printStackTrace()
//                if (platform.isClientValid) {
//                    showToast(context,"目前您的新浪微博版本过低或未安装新浪微博，需要安装新浪微博才能使用")
//                } else {
//                    showToast(context,"新浪微博分享失败")
//                }
//            }
//
//            override fun onCancel(platform: Platform, i: Int) {
//                showToast(context,"取消分享")
//            }
//        }
//        // 执行图文分享
//        weibo.share(shareParams)
    }

    /**
     * 分享成功回调接口
     */
    interface ShareSuccessListener {
        /**
         * 分享成功回调方法
         * @param shareType 分享类型 {@value SHARE_TYPE_QQ,SHARE_TYPE_WECHAT,SHARE_TYPE_MOMENTS,SHARE_TYPE_QZONE,SHARE_TYPE_SINA}
         */
        fun onShareSuccess(shareType: String)
    }

    //    /**
    //     *
    //     * @param activity
    //     */
    //    public static void oneKeyShare(final BaseActivity activity){
    //        OnekeyShare oks = new OnekeyShare();
    //        //关闭sso授权
    //        oks.disableSSOWhenAuthorize();
    //
    //        // title标题，微信、QQ和QQ空间等平台使用
    //        oks.setName("name");
    //        // titleUrl QQ和QQ空间跳转链接
    //        oks.setTitleUrl("http://sharesdk.cn");
    //        // text是分享文本，所有平台都需要这个字段
    //        oks.setText("我是分享文本");
    //        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    //        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
    //        // url在微信、微博，Facebook等平台中使用
    //        oks.setUrl("http://sharesdk.cn");
    //        // comment是我对这条分享的评论，仅在人人网使用
    //        oks.setComment("我是测试评论文本");
    //        // 启动分享GUI
    //        oks.show(activity);
    //    }


    private fun showToast(context: Context,msg:String?){
        if (StringUtils.isNotEmpty(msg)) ToastUtils.showToast(context,msg!!)
    }
}
