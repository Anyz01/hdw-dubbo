package com.hdw.enterprise.entity;


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
 * 企业职务配置表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
@ApiModel(value = "企业职务配置表")
@TableName("t_enterprise_job")
public class EnterpriseJob extends Model<EnterpriseJob> {
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	/**
	 * 企业部门表ID
	 */
    @ApiModelProperty(value = "企业部门表ID")
	@TableField("department_id")
	private String departmentId;
	/**
	 * 职务代码
	 */
    @ApiModelProperty(value = "职务代码")
	@TableField("job_code")
	private String jobCode;
	/**
	 * 职务名称
	 */
    @ApiModelProperty(value = "职务名称")
	@TableField("job_name")
	private String jobName;
	/**
	 * 记录创建时间
	 */
    @ApiModelProperty(value = "记录创建时间")
	@TableField("create_time")
	private Date createTime;
	/**
	 * 记录最后更新时间
	 */
    @ApiModelProperty(value = "记录最后更新时间")
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 记录创建用户
	 */
    @ApiModelProperty(value = "记录创建用户")
	@TableField("create_user")
	private String createUser;
	/**
	 * 记录最后更新用户
	 */
    @ApiModelProperty(value = "记录最后更新用户")
	@TableField("update_user")
	private String updateUser;
	/**
	 * 预留1
	 */
    @ApiModelProperty(value = "预留1")
	private String parameter1;
	/**
	 * 预留2
	 */
    @ApiModelProperty(value = "预留2")
	private String parameter2;
	/**
	 * 是否同步（0：是，1：否）
	 */
    @ApiModelProperty(value = "是否同步（0：是，1：否）")
	@TableField("is_sync")
	private Integer isSync;

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	/**
	 * 设置：企业部门表ID
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * 获取：企业部门表ID
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	/**
	 * 设置：职务代码
	 */
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	/**
	 * 获取：职务代码
	 */
	public String getJobCode() {
		return jobCode;
	}
	/**
	 * 设置：职务名称
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/**
	 * 获取：职务名称
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * 设置：记录创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：记录创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：记录最后更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：记录最后更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：记录创建用户
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：记录创建用户
	 */
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * 设置：记录最后更新用户
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * 获取：记录最后更新用户
	 */
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * 设置：预留1
	 */
	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}
	/**
	 * 获取：预留1
	 */
	public String getParameter1() {
		return parameter1;
	}
	/**
	 * 设置：预留2
	 */
	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}
	/**
	 * 获取：预留2
	 */
	public String getParameter2() {
		return parameter2;
	}
	/**
	 * 设置：是否同步（0：是，1：否）
	 */
	public void setIsSync(Integer isSync) {
		this.isSync = isSync;
	}
	/**
	 * 获取：是否同步（0：是，1：否）
	 */
	public Integer getIsSync() {
		return isSync;
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
