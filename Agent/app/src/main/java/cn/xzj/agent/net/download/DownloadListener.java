package cn.xzj.agent.net.download;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/11
 * @Des
 */
public abstract class DownloadListener{

    /**
     * 开始下载
     */
    public abstract void onStart();

    /**
     * 完成下载
     */
    public abstract void onComplete();

    /**
     * 下载进度
     *
     * @param readLength  当前下载
     * @param countLength 总长度
     * @param isFinish    是否结束
     */
    public abstract void updateProgress(long readLength, long countLength, boolean isFinish);
    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     *
     * @param e
     */
    public void onError(Throwable e) {

    }

    /**
     * 暂停下载
     */
   public void onPause() {

    }

    /**
     * 停止下载销毁
     */
   public void onStop() {
    }
}
