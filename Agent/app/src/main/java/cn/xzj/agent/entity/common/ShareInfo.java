package cn.xzj.agent.entity.common;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareInfo implements Parcelable {
    private String content;
    private String imgUrl;
    private String targetUrl;
    private String title;
    private String wxPath;
    private String wxUserName;
    private String targetPlatform;
    private String pageId;
    private String type;
    private String rewardType;

    public ShareInfo() {
    }

    protected ShareInfo(Parcel in) {
        content = in.readString();
        imgUrl = in.readString();
        targetUrl = in.readString();
        title = in.readString();
        wxPath = in.readString();
        wxUserName = in.readString();
        targetPlatform = in.readString();
        pageId = in.readString();
        type = in.readString();
        rewardType = in.readString();
    }

    public static final Creator<ShareInfo> CREATOR = new Creator<ShareInfo>() {
        @Override
        public ShareInfo createFromParcel(Parcel in) {
            return new ShareInfo(in);
        }

        @Override
        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWxPath() {
        return wxPath;
    }

    public void setWxPath(String wxPath) {
        this.wxPath = wxPath;
    }

    public String getWxUserName() {
        return wxUserName;
    }

    public void setWxUserName(String wxUserName) {
        this.wxUserName = wxUserName;
    }

    public String getTargetPlatform() {
        return targetPlatform;
    }

    public void setTargetPlatform(String targetPlatform) {
        this.targetPlatform = targetPlatform;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(imgUrl);
        dest.writeString(targetUrl);
        dest.writeString(title);
        dest.writeString(wxPath);
        dest.writeString(wxUserName);
        dest.writeString(targetPlatform);
        dest.writeString(pageId);
        dest.writeString(type);
        dest.writeString(rewardType);
    }
}
