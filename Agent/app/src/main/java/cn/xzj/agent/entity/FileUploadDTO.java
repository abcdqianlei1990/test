package cn.xzj.agent.entity;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/23
 * @ Des
 */
public class FileUploadDTO {

    /**
     * content (string, optional): 内容 ,
     * name (string, optional): 文件名 ,
     * size (integer, optional): 文件大小
     */

    private String content;
    private String name;
    private int size;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
