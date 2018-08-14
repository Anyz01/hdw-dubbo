package com.hdw.upms.entity.vo;

import com.hdw.common.util.JacksonUtils;
import com.hdw.upms.entity.SysUser;

import java.io.Serializable;
import java.util.List;



/**
 * @description：UserVo
 * @author：TuMinglong
 * @date：2015/10/1 14:51
 */
public class UserVo extends SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private String organizationName;

	private String enterpriseName;

	private List<RoleVo> rolesList;
	
	private String roleIds;

	public String getOrganizationName() {
		return organizationName;
	}



	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}



	public String getEnterpriseName() {
		return enterpriseName;
	}



	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}



	public List<RoleVo> getRolesList() {
		return rolesList;
	}



	public void setRolesList(List<RoleVo> rolesList) {
		this.rolesList = rolesList;
	}



	public String getRoleIds() {
		return roleIds;
	}



	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
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