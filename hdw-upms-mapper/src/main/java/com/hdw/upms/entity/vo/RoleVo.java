package com.hdw.upms.entity.vo;

import java.io.Serializable;
import java.util.List;

import com.hdw.upms.entity.SysResource;
import com.hdw.upms.entity.SysRole;


public class RoleVo extends SysRole implements Serializable{

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
