package cn.xzj.agent.entity;

import java.util.ArrayList;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/29
 * @ Des
 */
public class RegionJsonPickerBean {
//    private ArrayList<RegionJsonBean> options1Items = new ArrayList<>();
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    public ArrayList<String> getOptions1Items() {
        return options1Items;
    }

    public void setOptions1Items(ArrayList<String> options1Items) {
        this.options1Items = options1Items;
    }

    public ArrayList<ArrayList<String>> getOptions2Items() {
        return options2Items;
    }

    public void setOptions2Items(ArrayList<ArrayList<String>> options2Items) {
        this.options2Items = options2Items;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getOptions3Items() {
        return options3Items;
    }

    public void setOptions3Items(ArrayList<ArrayList<ArrayList<String>>> options3Items) {
        this.options3Items = options3Items;
    }
}
