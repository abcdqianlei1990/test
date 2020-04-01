package cn.xzj.agent.entity.share;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/14
 * @Des 微信红包小程序信息
 */
public class WechatHongbaoShareInfo {

    /**
     * content : string
     * imgUrl : string
     * targetUrl : string
     * title : string
     * wxImage : string
     * wxPath : string
     * wxUserName : string
     */

    private String content;
    private String imgUrl;
    private String targetUrl;
    private String title;
    private String wxImage;
    private String wxPath;
    private String wxUserName;

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

    public String getWxImage() {
        return wxImage;
    }

    public void setWxImage(String wxImage) {
        this.wxImage = wxImage;
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
}
