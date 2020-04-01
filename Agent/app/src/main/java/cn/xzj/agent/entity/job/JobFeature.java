package cn.xzj.agent.entity.job;

import java.util.List;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/18
 * @ Des 岗位特征实体
 */
public class JobFeature {

    /**
     * features : [{"id":"string","name":"string"}]
     * name : string
     */

    private String name;
    private List<FeaturesBean> features;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FeaturesBean> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeaturesBean> features) {
        this.features = features;
    }

    public static class FeaturesBean {
        /**
         * id : string
         * name : string
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
