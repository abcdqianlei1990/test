package cn.xzj.agent.entity.websocket;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/24
 * @Des socket基类
 */
public class BaseSocketResponseEntity {

    /**
     * path : /authorize
     * result : {"code":"0"}
     * id : 9e22243f250348c0a873cf13f57cf70d
     */

    private String path;
    private ResultBean result;
    private String id;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class ResultBean {
        /**
         * code : 0
         */

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
        public boolean isInvalidToken(){
            return "invalid_token".equals(code);
        }
    }
}
