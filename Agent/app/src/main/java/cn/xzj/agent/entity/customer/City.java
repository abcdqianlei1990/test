package cn.xzj.agent.entity.customer;

import java.io.Serializable;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/6
 * @ Des
 */
public class City implements Serializable {

    /**
     * name : string
     * value : string
     */

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
