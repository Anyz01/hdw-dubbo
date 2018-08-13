package com.hdw.upms.shiro;

import java.io.Serializable;
import java.util.List;

import com.hdw.common.util.JacksonUtils;


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

    //企业Id
    private Long enterpriseId;

    //所属部门Id
    private Long organizationId;

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


    public Long getEnterpriseId() {
        return enterpriseId;
    }


    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }


    public Long getOrganizationId() {
        return organizationId;
    }


    public void setOrganizationId(Long organizationId) {
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


    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }

}