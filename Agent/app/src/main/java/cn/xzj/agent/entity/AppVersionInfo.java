package cn.xzj.agent.entity;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/27
 * @ Des
 */
public class AppVersionInfo {

    /**
     * ANDROID_VERSION :  最新版本
     * ANDROID_UPDATE_PROMPT : 更新提示
     * ANDROID_APP_URL : 下载地址
     * ANDROID_FORCE_UPDATE : 0 是否强制更新
     */

    private String ANDROID_VERSION;
    private String ANDROID_UPDATE_PROMPT;
    private String ANDROID_APP_URL;
    private String ANDROID_FORCE_UPDATE;

    public String getANDROID_VERSION() {
        return ANDROID_VERSION;
    }

    public void setANDROID_VERSION(String ANDROID_VERSION) {
        this.ANDROID_VERSION = ANDROID_VERSION;
    }

    public String getANDROID_UPDATE_PROMPT() {
        return ANDROID_UPDATE_PROMPT;
    }

    public void setANDROID_UPDATE_PROMPT(String ANDROID_UPDATE_PROMPT) {
        this.ANDROID_UPDATE_PROMPT = ANDROID_UPDATE_PROMPT;
    }

    public String getANDROID_APP_URL() {
        return ANDROID_APP_URL;
    }

    public void setANDROID_APP_URL(String ANDROID_APP_URL) {
        this.ANDROID_APP_URL = ANDROID_APP_URL;
    }

    public String getANDROID_FORCE_UPDATE() {
        return ANDROID_FORCE_UPDATE;
    }

    public void setANDROID_FORCE_UPDATE(String ANDROID_FORCE_UPDATE) {
        this.ANDROID_FORCE_UPDATE = ANDROID_FORCE_UPDATE;
    }
    public boolean isForced(){
        return ANDROID_FORCE_UPDATE.equals("1");
    }
}
