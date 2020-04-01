package cn.xzj.agent.util;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.xzj.agent.R;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/14
 * @Des 分享工具类
 */
public class ShareUtils {
    /**
     * 分享微信小程序
     */
    public static void shareXiaoChengXuToWeChat(Context context, String title,
                                                String content,
                                                String wxUserName,
                                                String wxPath,
                                                String targetUrl,
                                                String wxImagePath,
                                                String imageUrl) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);
        shareParams.setTitle(title);
        shareParams.setText(content);
        shareParams.setWxUserName(wxUserName);//小程序id
        shareParams.setWxPath(wxPath);//小程序页面路径
        shareParams.setUrl(targetUrl);//分享内容的url、在微信和易信中也使用为视频文件地址
        shareParams.setImagePath(wxImagePath);//待分享的本地图片。如果目标平台使用客户端分享，此路径不可以在/data/data下面
        shareParams.setImageUrl(imageUrl);//待分享的网络图片
        Platform wxPlatform = ShareSDK.getPlatform(Wechat.NAME);
        wxPlatform.setPlatformActionListener(new DefaultPlatformActionListener(context));
        wxPlatform.share(shareParams);
    }

    /**
     * 快捷分享项目现在添加为不同的平台添加不同分享内容的方法。
     * 本类用于演示如何区别Twitter的分享内容和其他平台分享内容。
     */

    public static class ShareContentCustomizeDefault implements ShareContentCustomizeCallback {

        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
            // 改写twitter分享内容中的text字段，否则会超长，
            // 因为twitter会将图片地址当作文本的一部分去计算长度
//            if (Twitter.NAME.equals(platform.getName())) {
//                String text = platform.getContext().getString(R.string.share_content_short);
//                paramsToShare.setText(text);
//            }

        }
    }

    public static class DefaultPlatformActionListener implements PlatformActionListener {
        private Context mContext;

        private DefaultPlatformActionListener(Context context) {
            mContext = context;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Toast.makeText(mContext, R.string.share_success, Toast.LENGTH_SHORT).show();
            LogLevel.w("DefaultPlatformActionListener", "onComplete");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Toast.makeText(mContext, R.string.share_error, Toast.LENGTH_SHORT).show();
            LogLevel.w("DefaultPlatformActionListener", "onError");

        }

        @Override
        public void onCancel(Platform platform, int i) {
            LogLevel.w("DefaultPlatformActionListener", "onCancel");
            Toast.makeText(mContext, R.string.share_error, Toast.LENGTH_SHORT).show();
        }
    }
}
