package cn.xzj.agent.net.download;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/11
 * @Des 下载状态
 */
public interface DownloadStatus {
    String READY = "0";//准备
    String START = "1"; //开始
    String DOWN = "2";//下载中
    String PAUSE = "3";//暂停
    String STOP = "4";//停止
    String ERROR = "5";//错误
    String FINISH = "6";//完成
}