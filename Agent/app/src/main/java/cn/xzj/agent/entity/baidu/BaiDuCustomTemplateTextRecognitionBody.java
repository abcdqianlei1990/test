package cn.xzj.agent.entity.baidu;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/25
 * @Des
 */
public class BaiDuCustomTemplateTextRecognitionBody {
    /**
     * image	true	string	-	图像数据，base64编码后进行urlencode，要求base64编码和urlencode后大小不超过4M，最短边至少15px，最长边最大4096px,支持jpg/png/bmp格式
     * templateSign	false	string	-	您在自定义文字识别平台制作的模板的ID，举例：Nsdax2424asaAS791823112
     * classifierId	false	int	-	分类器Id。这个参数和templateSign至少存在一个，优先使用templateSign。存在templateSign时，表示使用指定模板；如果没有templateSign而有classifierId，表示使用分类器去判断使用哪个模板
     */
    private String image;
    private String templateSign;
    private Integer classifierId;

    public BaiDuCustomTemplateTextRecognitionBody(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTemplateSign() {
        return templateSign;
    }

    public void setTemplateSign(String templateSign) {
        this.templateSign = templateSign;
    }

    public Integer getClassifierId() {
        return classifierId;
    }

    public void setClassifierId(Integer classifierId) {
        this.classifierId = classifierId;
    }
}
