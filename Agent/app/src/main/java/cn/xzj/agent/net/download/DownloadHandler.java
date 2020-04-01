package cn.xzj.agent.net.download;

import android.os.Handler;
import android.os.Message;

import java.io.File;

import cn.xzj.agent.MyApplication;
import cn.xzj.agent.entity.DownloadInfo;
import cn.xzj.agent.util.CommonUtils;


/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/15
 * @Des
 */
public class DownloadHandler extends Handler {
    private String TAG = DownloadHandler.class.getSimpleName();

    DownloadHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        DownloadInfo mDownloadInfo = (DownloadInfo) msg.obj;
        if (mDownloadInfo != null) {
            DownloadInfo mDownloadingInfo = DownloadFileManager.getInstance().findDownloadInfo(mDownloadInfo.getUrl());
            if (mDownloadingInfo == null)
                return;
            DownloadListener mListener = mDownloadingInfo.getListener();
            switch (mDownloadInfo.getStatus()) {
                case DownloadStatus.DOWN:
                    if (mListener != null)
                        mListener.updateProgress(mDownloadInfo.getReadLength(), mDownloadInfo.getCountLength(), mDownloadInfo.getReadLength() == mDownloadInfo.getCountLength());
                    break;
                case DownloadStatus.STOP:
                    if (mListener != null)
                        mListener.onStop();
                    break;
                case DownloadStatus.ERROR:
                    if (mListener != null)
                        mListener.onError(null);
                    DownloadFileManager.getInstance().removeTask(mDownloadInfo.getUrl());
                    break;
                case DownloadStatus.PAUSE:
                    if (mListener != null)
                        mListener.onPause();
                    break;
                case DownloadStatus.START:
                    if (mListener != null)
                        mListener.onStart();
                    break;
                case DownloadStatus.FINISH:
                    if (mListener != null)
                        mListener.onComplete();
                    CommonUtils.sendBroadcastFileChange(MyApplication.application, new File(mDownloadInfo.getSavePath()));
                    break;
            }
        }
    }
}
