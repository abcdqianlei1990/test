package cn.xzj.agent.entity.websocket;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/24
 * @Des
 */
public class CallStatus {
    /**
     * arguments : {"status":1} status (integer, optional): 拨打状态: 0 - 无拨打, 2 - 通话中
     */
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
