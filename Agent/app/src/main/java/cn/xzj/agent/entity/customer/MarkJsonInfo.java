package cn.xzj.agent.entity.customer;

import java.util.ArrayList;

import cn.xzj.agent.entity.common.KeyValue;

public class MarkJsonInfo {
    private ArrayList<KeyValue> communicateSituation;
    private ArrayList<KeyValue> wxCommunicateSituation;
    private ArrayList<KeyValue> reason;
    private ArrayList<KeyValue> appointmentResult;   //已接通情况下具体情况
    private ArrayList<KeyValue> situations;   //所有情况枚举

    public ArrayList<KeyValue> getCommunicateSituation() {
        return communicateSituation;
    }

    public void setCommunicateSituation(ArrayList<KeyValue> communicateSituation) {
        this.communicateSituation = communicateSituation;
    }

    public ArrayList<KeyValue> getWxCommunicateSituation() {
        return wxCommunicateSituation;
    }

    public void setWxCommunicateSituation(ArrayList<KeyValue> wxCommunicateSituation) {
        this.wxCommunicateSituation = wxCommunicateSituation;
    }

    public ArrayList<KeyValue> getReason() {
        return reason;
    }

    public void setReason(ArrayList<KeyValue> reason) {
        this.reason = reason;
    }

    public ArrayList<KeyValue> getAppointmentResult() {
        return appointmentResult;
    }

    public void setAppointmentResult(ArrayList<KeyValue> appointmentResult) {
        this.appointmentResult = appointmentResult;
    }

    public ArrayList<KeyValue> getSituations() {
        return situations;
    }

    public void setSituations(ArrayList<KeyValue> situations) {
        this.situations = situations;
    }
}
