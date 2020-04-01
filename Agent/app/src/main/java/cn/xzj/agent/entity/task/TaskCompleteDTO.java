package cn.xzj.agent.entity.task;

import java.util.ArrayList;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/16
 * @ Des
 */
public class TaskCompleteDTO {

    /**
     * comment (string, optional): 备注 ,
     * communicateMethod (integer, optional): 沟通方式: 0 - 手机, 1 - 微信, 3 - 固化 ,
     * contactFailed (boolean, optional): 未取得联系 ,
     * lineConnected (boolean, optional): 已接通电话 ,
     * taskId (string, optional): 任务ID ,
     * taskTypeId (string, optional): 任务类型ID ,
     * telConnected (boolean, optional): 已接通固话 ,
     * userId (string, optional): 用户ID ,
     * wxAdded (boolean, optional): 微信已添加
     */

    private String comment;
    private int communicateMethod;
    private int communicateResult;
    private boolean contactFailed;
    private boolean lineConnected;
    private String taskId;
    private String communicateSituation;    //沟通具体情况
    private String taskTypeId;
    private boolean telConnected;
    private String userId;
    private boolean wxAdded;
    private ArrayList<String> reason;

    public ArrayList<String> getReason() {
        return reason;
    }

    public void setReason(ArrayList<String> reason) {
        this.reason = reason;
    }

    public int getCommunicateResult() {
        return communicateResult;
    }

    public void setCommunicateResult(int communicateResult) {
        this.communicateResult = communicateResult;
    }

    public String getCommunicateSituation() {
        return communicateSituation;
    }

    public void setCommunicateSituation(String communicateSituation) {
        this.communicateSituation = communicateSituation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommunicateMethod() {
        return communicateMethod;
    }

    public void setCommunicateMethod(int communicateMethod) {
        this.communicateMethod = communicateMethod;
    }

    public boolean isContactFailed() {
        return contactFailed;
    }

    public void setContactFailed(boolean contactFailed) {
        this.contactFailed = contactFailed;
    }

    public boolean isLineConnected() {
        return lineConnected;
    }

    public void setLineConnected(boolean lineConnected) {
        this.lineConnected = lineConnected;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public boolean isTelConnected() {
        return telConnected;
    }

    public void setTelConnected(boolean telConnected) {
        this.telConnected = telConnected;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isWxAdded() {
        return wxAdded;
    }

    public void setWxAdded(boolean wxAdded) {
        this.wxAdded = wxAdded;
    }
}
