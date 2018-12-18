package com.hdw.upms.shiro;

import com.hdw.common.util.JacksonUtils;

import java.io.Serializable;
import java.util.List;


/**
 * @author TuMinglong
 * @version 1.0.0
 * @Description 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 * @date 2018年5月14日下午7:30:29
 */
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;

    private Long id;

    private String loginName;

    private String name;

    //是否领导（0-是，1-否）
    private Integer isLeader;

    //所属职务
    private String userJob;

    //用户类型（0-超级用户，1-企业用户，2-监管用户）
    private Integer userType;

    //企业Id
    private String enterpriseId;

    //角色所拥有的企业ID
    private List<String> enterprises;

    //所属部门Id
    private String organizationId;

    private List<String> urlSet;

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

    public List<String> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<String> enterprises) {
        this.enterprises = enterprises;
    }

    public String getOrganizationId() {
        return organizationId;
    }


    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Integer isLeader) {
        this.isLeader = isLeader;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }

}