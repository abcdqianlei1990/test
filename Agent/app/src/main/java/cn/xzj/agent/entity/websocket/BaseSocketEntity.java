package cn.xzj.agent.entity.websocket;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/24
 * @Des socket基类
 */
public class BaseSocketEntity<T> {
    private String id;
    private String path;
    private T arguments;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getArguments() {
        return arguments;
    }

    public void setArguments(T arguments) {
        this.arguments = arguments;
    }

}
