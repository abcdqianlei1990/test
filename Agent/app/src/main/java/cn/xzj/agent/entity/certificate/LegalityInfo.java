package cn.xzj.agent.entity.certificate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by channey on 2017/1/4.
 */

public class LegalityInfo{
    /**
     * 用工具合成或者编辑过的身份证图片
     */
    private float Edited;
    private float ID_Photo_Threshold;
    /**
     * 正式身份证复印件
     */
    private float Photocopy;
    /**
     * 临时身份证照片
     */
    private float Temporary_ID_Photo;

    /**
     * 正式身份证照片
     */
    private float ID_Photo;

    /**
     * 手机或电脑屏幕翻拍的照片
     */
    private float Screen;

    public float getEdited() {
        return Edited;
    }

    public void setEdited(float edited) {
        Edited = edited;
    }

    public float getID_Photo_Threshold() {
        return ID_Photo_Threshold;
    }

    public void setID_Photo_Threshold(float ID_Photo_Threshold) {
        this.ID_Photo_Threshold = ID_Photo_Threshold;
    }

    public float getPhotocopy() {
        return Photocopy;
    }

    public void setPhotocopy(float photocopy) {
        Photocopy = photocopy;
    }

    public float getTemporary_ID_Photo() {
        return Temporary_ID_Photo;
    }

    public void setTemporary_ID_Photo(float temporary_ID_Photo) {
        Temporary_ID_Photo = temporary_ID_Photo;
    }

    public float getID_Photo() {
        return ID_Photo;
    }

    public void setID_Photo(float ID_Photo) {
        this.ID_Photo = ID_Photo;
    }

    public float getScreen() {
        return Screen;
    }

    public void setScreen(float screen) {
        Screen = screen;
    }
}
