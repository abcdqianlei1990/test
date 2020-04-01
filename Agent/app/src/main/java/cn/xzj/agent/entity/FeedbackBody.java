package cn.xzj.agent.entity;

import java.util.List;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/3/8
 * @Des 问题反馈body
 */
public class FeedbackBody {


    public FeedbackBody(String content, String type, List<String> images) {
        this.content = content;
        this.type = type;
        this.images = images;
    }

    /**
     * content (string, optional): 内容 ,
     * images (Array[string], optional): 图片链接 ,
     * type (string, optional): 问题类型: APP - 用户端APP, AGENT_PC - 小职姐PC端, AGENT_APP - 小职姐服务版, STORE - 门店系统
     * }
     */

    private String content;
    private String type;
    private List<String> images;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
