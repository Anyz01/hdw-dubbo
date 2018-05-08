package com.hdw.upms.entity.vo;

import java.io.Serializable;
import java.util.List;

import com.hdw.upms.entity.Resource;
import com.hdw.upms.entity.Role;


public class RoleVo extends Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 拥有的权限列表
    private List<Resource> permissions;

	public List<Resource> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Resource> permissions) {
		this.permissions = permissions;
	}
    
    

}
