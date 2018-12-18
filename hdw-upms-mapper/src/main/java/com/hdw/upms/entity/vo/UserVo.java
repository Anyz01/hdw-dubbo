package com.hdw.upms.entity.vo;


import com.hdw.common.util.JacksonUtils;
import com.hdw.upms.entity.SysRole;
import com.hdw.upms.entity.SysUser;

import java.io.Serializable;
import java.util.List;


/**
 * @description：UserVo
 * @author：TuMinglong
 * @date 2018年5月6日 上午9:55:46
 */
public class UserVo extends SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String enterpriseName;

    private String departmentName;

    private String jobName;

    private List<SysRole> roles;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    /**
     * 密码盐
     */
    public String getCredentialsSalt() {
        return getLoginName() + getSalt();
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }
}