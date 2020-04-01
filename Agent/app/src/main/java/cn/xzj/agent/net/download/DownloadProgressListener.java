package cn.xzj.agent.net.download;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/22
 * @Des
 */
public interface DownloadProgressListener {
    void update(long read, long count, final boolean done);
}
