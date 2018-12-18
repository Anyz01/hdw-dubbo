package com.hdw.upms.entity.vo;


import com.hdw.upms.entity.SysResource;
import com.hdw.upms.entity.SysRole;

import java.io.Serializable;
import java.util.List;


/**
 * @description：UserVo
 * @author：TuMinglong
 * @date 2018年5月6日 上午9:55:46
 */
public class RoleVo extends SysRole implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 拥有的权限列表
    private List<SysResource> permissions;

    public List<SysResource> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysResource> permissions) {
        this.permissions = permissions;
    }


}
