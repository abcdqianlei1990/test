package cn.xzj.agent.entity;



/**
 * Created by channey on 2016/10/28.
 * version:1.0
 * desc:设备信息
 */

public class DeviceInfo {
    private String deviceModel; //手机型号
    private String deviceOSVersion; //操作系统版本
    private String deviceName; //设备名称，如 aaa的手机
    private String escape;  //是否越狱  0-false 1-true
    private String memory;  //内存大小
    private String id;  //IMEI或者mac
    private String macAddress;
    private String netWorkType;
    private String osType;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceName, String escape, String memory) {
        this.deviceName = deviceName;
        this.escape = escape;
        this.memory = memory;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isRoot() {
        return "1".endsWith(escape);
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getNetWorkType() {
        return netWorkType;
    }

    public void setNetWorkType(String netWorkType) {
        this.netWorkType = netWorkType;
    }
}
