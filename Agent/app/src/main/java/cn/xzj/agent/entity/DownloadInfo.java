package cn.xzj.agent.entity;

import cn.xzj.agent.net.download.DownloadListener;
import cn.xzj.agent.net.download.DownloadStatus;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/11
 * @Des
 */
@Entity
public class DownloadInfo {
    @Id
    private long id;
    /*存储位置*/
    private String savePath;
    /*下载url*/
    private String url;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*回调监听*/
    @Transient
    private DownloadListener listener;
    /*下载状态*/
    private String status = DownloadStatus.READY;
    //文件名称
    private String name;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }


    public DownloadListener getListener() {
        return listener;
    }

    public void setListener(DownloadListener listener) {
        this.listener = listener;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
