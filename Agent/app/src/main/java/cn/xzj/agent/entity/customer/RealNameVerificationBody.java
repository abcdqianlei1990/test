package cn.xzj.agent.entity.customer;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/21
 * @Des
 */
public class RealNameVerificationBody {


    /**
     * 实名信息 {
     * idNo (string): 身份证号 ,
     * name (string): 用户姓名 ,
     * userId (string): 用户ID
     * }
     */

    private String idNo;
    private String name;
    private String userId;

    public RealNameVerificationBody(String idNo, String name, String userId) {
        this.idNo = idNo;
        this.name = name;
        this.userId = userId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
