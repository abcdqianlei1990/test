package cn.xzj.agent.net.download;


import android.os.Message;

import cn.xzj.agent.entity.DownloadInfo;
import cn.xzj.agent.util.DB;
import cn.xzj.agent.util.LogLevel;
import io.reactivex.observers.DisposableObserver;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/11
 */
public class DownloadProgressSubscriber<T> extends DisposableObserver<T> implements DownloadProgressListener {
    private static final String TAG = DownloadProgressSubscriber.class.getSimpleName();
    /*下载数据*/
    private DownloadInfo downloadInfo;
    private DownloadHandler mDownloadHandler;

    DownloadProgressSubscriber(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
        this.mDownloadHandler = new DownloadHandler();
        LogLevel.w("DownloadProgressSubscriber","id"+downloadInfo.getId());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        LogLevel.w(TAG,"下载失败");
        downloadInfo.setStatus(DownloadStatus.ERROR);
        /*停止下载*/
        DownloadFileManager.getInstance().stopDown(downloadInfo);
        sendDownloadHandlerMessage(downloadInfo);
    }

    @Override
    public void onComplete() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadInfo.setStatus(DownloadStatus.START);
        sendDownloadHandlerMessage(downloadInfo);
    }

    @Override
    public void onNext(T t) {
        downloadInfo.setStatus(DownloadStatus.FINISH);
        sendDownloadHandlerMessage(downloadInfo);
    }

    @Override
    public void update(long read, long count, final boolean done) {
        if (downloadInfo.getCountLength() > count) {
            read = downloadInfo.getCountLength() - count + read;
        } else {
            downloadInfo.setCountLength(count);
        }
        downloadInfo.setReadLength(read);
        switch (downloadInfo.getStatus()) {
            case DownloadStatus.PAUSE:
                sendDownloadHandlerMessage(downloadInfo);
                break;
            case DownloadStatus.STOP:
                sendDownloadHandlerMessage(downloadInfo);
                break;
            default:
                downloadInfo.setStatus(DownloadStatus.DOWN);
        }
        sendDownloadHandlerMessage(downloadInfo);
    }

    private void sendDownloadHandlerMessage(DownloadInfo downloadInfo) {
        Message mMessage = new Message();
        mMessage.obj = downloadInfo;
        mDownloadHandler.sendMessage(mMessage);
        DB.getDB().getDownloadInfoBox().put(downloadInfo);
    }

}
