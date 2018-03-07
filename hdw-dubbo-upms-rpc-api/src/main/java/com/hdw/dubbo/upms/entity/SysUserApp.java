package com.hdw.dubbo.upms.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户与应用关系表
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-07
 */
@TableName("t_sys_user_app")
public class SysUserApp extends Model<SysUserApp> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号_主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 所属用户
     */
	@TableField("user_id")
	private Long userId;
    /**
     * 所属应用
     */
	@TableField("app_id")
	private Long appId;
    /**
     * 角色列表
     */
	@TableField("role_ids")
	private String roleIds;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
