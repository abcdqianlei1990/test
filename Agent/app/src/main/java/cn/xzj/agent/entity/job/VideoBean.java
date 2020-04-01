package cn.xzj.agent.entity.job;

import java.io.Serializable;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/22
 * @Des
 */
public class VideoBean implements Serializable {
    /**
     * ("链接")
     * private String url;
     * ("描述")
     * //     *     private String label;
     */
    private String url;
    private String label;

    public VideoBean(String label, String url) {
        this.url = url;
        this.label = label;
    }

    public VideoBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
