package cn.xzj.agent.entity.common;



/**
 * Created by channey on 2017/2/4.
 */

public class ShareMenuInfo {
    private int icon;
    private String title;
    private String platform;

    public ShareMenuInfo() {
    }

    public ShareMenuInfo(int icon, String title, String platform) {
        this.icon = icon;
        this.title = title;
        this.platform = platform;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
