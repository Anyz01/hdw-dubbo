package com.hdw.upms.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hdw.common.util.JacksonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@ApiModel(value = "系统日志表")
@TableName("t_sys_log")
public class SysLog extends Model<SysLog> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
    @ApiModelProperty(value = "主键id")
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 登陆名
	 */
    @ApiModelProperty(value = "登陆名")
	@TableField("login_name")
	private String loginName;
	/**
	 * 角色名
	 */
    @ApiModelProperty(value = "角色名")
	@TableField("role_name")
	private String roleName;

	/**
	 * 日志类型（0:系统日志，1：操作日志）
	 */
	@ApiModelProperty(value = "日志类型")
	private Integer type;

	/**
	 * 用户操作
	 */
	@ApiModelProperty(value = "用户操作")
	private String operation;
	/**
	 * 类名
	 */
    @ApiModelProperty(value = "类名")
	@TableField("class_name")
	private String className;

	/**
	 * 请求方法
	 */
    @ApiModelProperty(value = "请求方法")
	private String method;
	/**
	 * 请求参数
	 */
    @ApiModelProperty(value = "请求参数")
	private String params;
	/**
	 * 执行时长
	 */
    @ApiModelProperty(value = "执行时长")
	private Long time;
	/**
	 * 客户端ip
	 */
    @ApiModelProperty(value = "客户端ip")
	@TableField("client_ip")
	private String clientIp;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间")
	@TableField("create_time")
	private Date createTime;

	/**
	 * 设置：主键id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：登陆名
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * 获取：登陆名
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * 设置：角色名
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * 获取：角色名
	 */
	public String getRoleName() {
		return roleName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 设置：类名
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 获取：类名
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 设置：执行时长
	 */
	public void setTime(Long time) {
		this.time = time;
	}
	/**
	 * 获取：执行时长
	 */
	public Long getTime() {
		return time;
	}
	/**
	 * 设置：客户端ip
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	/**
	 * 获取：客户端ip
	 */
	public String getClientIp() {
		return clientIp;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

    @Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
        return JacksonUtils.toJson(this);
	}
}
