package cn.xzj.agent.entity.certificate;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by channey on 2016/11/9.
 * version:1.0
 * desc:身份证信息实体
 */

public class IDCardInfo{
    //基本信息
    private int result;
    private String request_id;  //用于区分每一次请求的唯一的字符串。
    private double time_used;   //	整个请求所花费的时间，单位为毫秒。
    private int completeness;
    private int side;    //0-正面 1-背面
    private LegalityInfo legality;
    //正面信息
    private IdCardOcrRetInfo address;
    private IdCardOcrRetInfo gender;
    private IdCardOcrRetInfo idcard_number;
    private IdCardOcrRetInfo name;
    private IdCardOcrRetInfo nationality;    //民族
    private IdCardOcrRetInfo birth_year;    //出生年份
    private IdCardOcrRetInfo birth_month;    //出生月份
    private IdCardOcrRetInfo birth_day;    //出生日
    //背面信息
    private IdCardOcrRetInfo valid_date_start;
    private IdCardOcrRetInfo valid_date_end;
    private IdCardOcrRetInfo issued_by;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public double getTime_used() {
        return time_used;
    }

    public void setTime_used(double time_used) {
        this.time_used = time_used;
    }

    public int getCompleteness() {
        return completeness;
    }

    public void setCompleteness(int completeness) {
        this.completeness = completeness;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public LegalityInfo getLegality() {
        return legality;
    }

    public void setLegality(LegalityInfo legality) {
        this.legality = legality;
    }

    public IdCardOcrRetInfo getAddress() {
        return address;
    }

    public void setAddress(IdCardOcrRetInfo address) {
        this.address = address;
    }

    public IdCardOcrRetInfo getGender() {
        return gender;
    }

    public void setGender(IdCardOcrRetInfo gender) {
        this.gender = gender;
    }

    public IdCardOcrRetInfo getIdcard_number() {
        return idcard_number;
    }

    public void setIdcard_number(IdCardOcrRetInfo idcard_number) {
        this.idcard_number = idcard_number;
    }

    public IdCardOcrRetInfo getName() {
        return name;
    }

    public void setName(IdCardOcrRetInfo name) {
        this.name = name;
    }

    public IdCardOcrRetInfo getNationality() {
        return nationality;
    }

    public void setNationality(IdCardOcrRetInfo nationality) {
        this.nationality = nationality;
    }

    public IdCardOcrRetInfo getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(IdCardOcrRetInfo birth_year) {
        this.birth_year = birth_year;
    }

    public IdCardOcrRetInfo getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(IdCardOcrRetInfo birth_month) {
        this.birth_month = birth_month;
    }

    public IdCardOcrRetInfo getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(IdCardOcrRetInfo birth_day) {
        this.birth_day = birth_day;
    }

    public IdCardOcrRetInfo getValid_date_start() {
        return valid_date_start;
    }

    public void setValid_date_start(IdCardOcrRetInfo valid_date_start) {
        this.valid_date_start = valid_date_start;
    }

    public IdCardOcrRetInfo getValid_date_end() {
        return valid_date_end;
    }

    public void setValid_date_end(IdCardOcrRetInfo valid_date_end) {
        this.valid_date_end = valid_date_end;
    }

    public IdCardOcrRetInfo getIssued_by() {
        return issued_by;
    }

    public void setIssued_by(IdCardOcrRetInfo issued_by) {
        this.issued_by = issued_by;
    }
}
