package cn.xzj.agent.net.download;


import java.util.HashMap;
import java.util.List;

import cn.xzj.agent.entity.DownloadInfo;
import cn.xzj.agent.entity.DownloadInfo_;
import cn.xzj.agent.util.DB;
import io.objectbox.Box;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/11
 * @Des 文件下载管理器`
 */
public class DownloadFileManager {
    private static DownloadFileManager _INSTANCE;
    //下载队列
    private HashMap<String, DownloadTask> mDownloadTasks;
    private HashMap<String, DownloadInfo> downloadInfoCache;
    private Box<DownloadInfo> mBox;

    /**
     * 获取单例
     */
    public static DownloadFileManager getInstance() {
        if (_INSTANCE == null) {
            synchronized (DownloadFileManager.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new DownloadFileManager();
                }
            }
        }
        return _INSTANCE;
    }

    private DownloadFileManager() {
        downloadInfoCache = new HashMap<>();
        mDownloadTasks = new HashMap<>();
        mBox = DB.getDB().getDownloadInfoBox();

    }

    /**
     * 开始下载
     */
    public void startDown(final DownloadInfo info) {

//        if (info == null || mDownloadTasks.get(info.getUrl()) != null) {
//            //当前DownloadInfo 正在下载队列中
//            return;
//        }
        if (info.getStatus().equals(DownloadStatus.FINISH))//已下载完成不做处理
            return;
        //判断是否已下载完成如果已下载过滤掉
        List<DownloadInfo> mData = DB.getDB().getDownloadInfoBox().query().equal(DownloadInfo_.url, info.getUrl()).build().find();
        if (!mData.isEmpty()) {
            if (mData.get(0).getStatus().equals(DownloadStatus.FINISH))
                return;
        }
        mBox.put(info);//插入到数据库
        DownloadTask downloadTask = new DownloadTask(info);
        mDownloadTasks.put(info.getUrl(), downloadTask);
        downloadInfoCache.put(info.getUrl(), info);
        downloadTask.start();


    }

    /**
     * 停止下载
     */
    public void stopDown(DownloadInfo info) {
        if (info == null)
            return;
        if (!info.getStatus().equals(DownloadStatus.DOWN))
            return;
        info.setStatus(DownloadStatus.STOP);
        if (info.getListener() != null)
            info.getListener().onStop();
        if (mDownloadTasks.containsKey(info.getUrl())) {
            DownloadTask mDownloadTask = mDownloadTasks.get(info.getUrl());
            assert mDownloadTask != null;
            mDownloadTask.remove();
            mDownloadTasks.remove(info.getUrl());
            mBox.put(info);//插入到数据库
        }
    }

    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(DownloadInfo info) {
        if (info == null) return;
        if (!info.getStatus().equals(DownloadStatus.DOWN))
            return;
        info.setStatus(DownloadStatus.PAUSE);
        if (info.getListener() != null)
            info.getListener().onPause();
        if (mDownloadTasks.containsKey(info.getUrl())) {
            DownloadTask mDownloadTask = mDownloadTasks.get(info.getUrl());
            assert mDownloadTask != null;
            mDownloadTask.remove();
            mDownloadTasks.remove(info.getUrl());
            mBox.put(info);//插入到数据库
        }
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (DownloadInfo downloadInfo : downloadInfoCache.values()) {
            stopDown(downloadInfo);
        }
        mDownloadTasks.clear();
        downloadInfoCache.clear();
        for (String key : mDownloadTasks.keySet()) {
            DownloadTask mDownloadTask = mDownloadTasks.get(key);
            if (mDownloadTask != null)
                mDownloadTask.remove();
        }
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (DownloadInfo downloadInfo : downloadInfoCache.values()) {
            pause(downloadInfo);
        }
        mDownloadTasks.clear();
        downloadInfoCache.clear();
        for (String key : mDownloadTasks.keySet()) {
            DownloadTask mDownloadTask = mDownloadTasks.get(key);
            if (mDownloadTask != null)
                mDownloadTask.remove();
        }
    }

    public DownloadInfo findDownloadInfo(String key) {
        if (downloadInfoCache != null) {
            return downloadInfoCache.get(key);
        }
        return null;
    }

    public void putDownloadInfo(DownloadInfo downloadInfo) {
        if (downloadInfoCache != null)
            downloadInfoCache.put(downloadInfo.getUrl(), downloadInfo);
    }

    public void removeTask(String key) {
        if (downloadInfoCache != null)
            downloadInfoCache.remove(key);
        //移除下载队列
        if (mDownloadTasks!=null)
            mDownloadTasks.remove(key);
    }
}
