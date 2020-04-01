package cn.xzj.agent.entity.job;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/27
 * @ Des
 */
public class ReservationDTO {


    /**
     * applyId (string, optional): 处理申请ID ,
     * latitude (number, optional): 接站地址纬度 ,
     * longitude (number, optional): 接站地址经度 ,
     * pickUpAddress (string, optional): 接站地址 ,
     * pickUpLocation (string, optional): 接站地点 ,
     * pickUpTime (string, optional): 接站时间 ,
     * positionId (string, optional): 处理岗位ID ,
     * positionName (string, optional): 处理岗位名称 ,
     * source (string, optional): 来源 ,
     * userId (string, optional): 用户ID
     */

    private String applyId;
    private double latitude;
    private double longitude;
    private String pickUpAddress;
    private String pickUpLocation;
    private String pickUpTime;
    private String positionId;
    private String positionName;
    private String source;
    private String userId;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
