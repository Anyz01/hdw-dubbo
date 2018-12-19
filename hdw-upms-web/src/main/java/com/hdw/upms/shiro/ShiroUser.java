package com.hdw.upms.shiro;

import com.hdw.common.util.JacksonUtils;

import java.io.Serializable;
import java.util.List;


/**
 * @Description 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 * @Author TuMinglong
 * @Date 2018/12/12 19:03
 */
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;

    private Long id;

    private String loginName;

    private String name;

    //用户类型（0-超级用户，1-企业用户，2-监管用户）
    private Integer userType;

    //状态
    private Integer status;

    //是否领导（0-是，1-否）
    private Integer isLeader;

    //企业Id
    private String enterpriseId;

    //所属部门Id
    private String departmentId;

    //所属职务Id
    private String jobId;

    /**
     *  角色管理的企业ID集合
     */
    private List<String> enterpriseIdList;

    /**
     * 用户拥有的权限集合
     */
    private List<String> urlSet;

    /**
     * 用户拥有的角色集合
     */
    private List<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Integer isLeader) {
        this.isLeader = isLeader;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<String> getEnterpriseIdList() {
        return enterpriseIdList;
    }

    public void setEnterpriseIdList(List<String> enterpriseIdList) {
        this.enterpriseIdList = enterpriseIdList;
    }

    public List<String> getUrlSet() {
        return urlSet;
    }

    public void setUrlSet(List<String> urlSet) {
        this.urlSet = urlSet;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }

}