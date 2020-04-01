package cn.xzj.agent.entity.privacy;

import java.util.List;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/28
 * @ Des
 */
public class InstalledAppUploadBody {

    /**
     * agentId : string
     * apps : ["string"]
     * id : string
     * receiveTime : 2018-09-28T06:52:10.111Z
     * records : ["string"]
     */

    private String agentId;
    private String id;
    private String deviceId;
    private String receiveTime;
    private List<String> apps;
    private List<String> records;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    public List<String> getRecords() {
        return records;
    }

    public void setRecords(List<String> records) {
        this.records = records;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
