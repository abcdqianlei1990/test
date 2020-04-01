package cn.xzj.agent.entity.certificate;

import android.os.Parcel;
import android.os.Parcelable;

public class IdCardOcrRetInfo implements Parcelable {
    private double quality;
    private String result;
    private int logic;

    protected IdCardOcrRetInfo(Parcel in) {
        quality = in.readDouble();
        result = in.readString();
        logic = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quality);
        dest.writeString(result);
        dest.writeInt(logic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IdCardOcrRetInfo> CREATOR = new Creator<IdCardOcrRetInfo>() {
        @Override
        public IdCardOcrRetInfo createFromParcel(Parcel in) {
            return new IdCardOcrRetInfo(in);
        }

        @Override
        public IdCardOcrRetInfo[] newArray(int size) {
            return new IdCardOcrRetInfo[size];
        }
    };

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }
}
