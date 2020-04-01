package cn.xzj.agent.entity;

import java.util.List;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/11/13
 * @ Des
 */
public class Token {

    /**
     * accessToken : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXNwbGF5LW5hbWUiOiIxMDAwMSIsInVzZXJfbmFtZSI6IjEwMDAxIiwic2NvcGUiOltdLCJleHAiOjE1NDIxNjQ1NjgsImF1dGhvcml0aWVzIjpbIlVTRVIiLCJZWV9PUEVSQVRFX1NVUFBPUlRFUiIsIkFHRU5UX01FTUJFUiJdLCJqdGkiOiI0YjRjMzlkNy0wYjRkLTQyYWMtYWY0Zi00MDVmOGMxZWVjYjQiLCJjbGllbnRfaWQiOiJkZWZhdWx0In0.alD-ZG9BNyEGI_9susVq9Es-StZy4AwVJJ8cchGxep4
     * username : 叶茂
     * roles : ["AGENT_MEMBER","YY_OPERATE_SUPPORTER"]
     * permissions : ["AGENT_SYSTEM","AGENT_TASK","AGENT_CUSTOMER_MANAGE","AGENT_DASHBOARD","YY_SYSTEM","YY_COMPANY","YY_POSITION","YY_COMPANY_LIST","YY_COMPANY_ADD","YY_LABOR_LIST","YY_POSITION_ADD","YY_POSITION_WATCH","YY_LABOR_USER","YY_RECURIT_WATCH"]
     */

    private String accessToken;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    /**
     * code : 500001
     * error : {"message":"验证码错误"}
     */

    private String code;
    private ErrorInfo error;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }


}
