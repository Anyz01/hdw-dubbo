package com.hdw.upms.entity;



import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.hdw.common.util.JacksonUtils;


/**
 *
 * 角色资源
 *
 */
@TableName("t_sys_role_resource")
public class SysRoleResource implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键id */
	@TableId(type=IdType.AUTO)
	private Long id;

	/** 角色id */
	@TableField(value="role_id")
	private Long roleId;

	/** 资源id */
	@TableField(value="resource_id")
	private Long resourceId;


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public String toString() {
		return JacksonUtils.toJson(this);
	}
}
