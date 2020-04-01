package cn.xzj.agent.entity.task;

import java.io.Serializable;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/14
 * @Des
 */
public class TaskDateInfo implements Serializable {


    private String date;// 日期 年月日
    private String shortDate;//短日期 月日
    private String from;//开始时间
    private String to;//结束时间
    private long timestamp;//当天时间戳
    private String title;

    public TaskDateInfo(String date, String shortDate, String from, String to, long timestamp, String title) {
        this.date = date;
        this.shortDate = shortDate;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.title = title;
    }

    public TaskDateInfo(String date, String from, String to, long timestamp, String title) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.title=title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDate() {
        return shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }
}
