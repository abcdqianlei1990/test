package cn.xzj.agent.entity.certificate;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by channey on 2016/11/28.
 * version:1.0
 * desc:
 */
public class BirthdayInfo implements Parcelable{
    private String day;
    private String month;
    private String year;

    public BirthdayInfo() {
    }

    protected BirthdayInfo(Parcel in) {
        day = in.readString();
        month = in.readString();
        year = in.readString();
    }

    public static final Creator<BirthdayInfo> CREATOR = new Creator<BirthdayInfo>() {
        @Override
        public BirthdayInfo createFromParcel(Parcel in) {
            return new BirthdayInfo(in);
        }

        @Override
        public BirthdayInfo[] newArray(int size) {
            return new BirthdayInfo[size];
        }
    };

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(year);
    }

    @Override
    public String toString() {
        return year+"-"+month+"-"+day;
    }
}
