package cn.xzj.agent.entity.job;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/24
 * @ Des 接站地址
 */
public class PickLocationVO {


    /**
     * address (string, optional): 接站地址 ,
     * latitude (number, optional): 纬度 ,
     * location (string, optional): 接站地点 ,
     * longitude (number, optional): 经度
     */

    private String address;
    private double latitude;
    private String location;
    private double longitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
