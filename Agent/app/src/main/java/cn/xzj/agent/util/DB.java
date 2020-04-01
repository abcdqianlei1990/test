package cn.xzj.agent.util;

import android.content.Context;

import cn.xzj.agent.entity.DownloadInfo;
import cn.xzj.agent.entity.MyObjectBox;
import cn.xzj.agent.entity.agentinfo.UserInfo;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/22
 * @Des
 */
public class DB {
    private static DB _DB;
    private BoxStore mBoxStore;
    private Box<DownloadInfo> mDownloadInfoBox;
    private Box<UserInfo> mUserInfoBox;

    public static void init(Context context) {
        if (_DB == null)
            _DB = new DB(context);
    }

    public static DB getDB() {
        return _DB;
    }

    private DB(Context context) {
        mBoxStore = MyObjectBox.builder().androidContext(context).build();
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    public Box<DownloadInfo> getDownloadInfoBox() {
        if (mDownloadInfoBox == null) {
            mDownloadInfoBox = mBoxStore.boxFor(DownloadInfo.class);
        }
        return mDownloadInfoBox;
    }

    public Box<UserInfo> getUserInfoBox() {
        if (mUserInfoBox == null) {
            mUserInfoBox = mBoxStore.boxFor(UserInfo.class);
        }
        return mUserInfoBox;
    }
}
