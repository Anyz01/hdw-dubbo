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
 * 数据字典表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@ApiModel(value = "数据字典表")
@TableName("t_sys_dic")
public class SysDic extends Model<SysDic> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
    @ApiModelProperty(value = "主键ID")
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 父变量ID
	 */
    @ApiModelProperty(value = "父变量ID")
	@TableField("parent_id")
	private Long parentId;
	/**
	 * 变量代码
	 */
    @ApiModelProperty(value = "变量代码")
	@TableField("var_code")
	private String varCode;
	/**
	 * 变量名称
	 */
    @ApiModelProperty(value = "变量名称")
	@TableField("var_name")
	private String varName;
	/**
	 * 记录创建时间
	 */
    @ApiModelProperty(value = "记录创建时间")
	@TableField("create_time")
	private Date createTime;
	/**
	 * 记录修改时间
	 */
    @ApiModelProperty(value = "记录修改时间")
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 记录创建者（用户）
	 */
    @ApiModelProperty(value = "记录创建者（用户）")
	@TableField("create_user")
	private String createUser;
	/**
	 * 记录最后修改者（用户）
	 */
    @ApiModelProperty(value = "记录最后修改者（用户）")
	@TableField("update_user")
	private String updateUser;
	/**
	 * 数据是否同步(0:是,1:否)
	 */
    @ApiModelProperty(value = "数据是否同步(0:是,1:否)")
	@TableField("is_sync")
	private Integer isSync;

	/**
	 * 父变量名称
	 */
	@TableField(exist = false)
	private String parentName;

	/**
	 * 设置：主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：父变量ID
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父变量ID
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：变量代码
	 */
	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}
	/**
	 * 获取：变量代码
	 */
	public String getVarCode() {
		return varCode;
	}
	/**
	 * 设置：变量名称
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}
	/**
	 * 获取：变量名称
	 */
	public String getVarName() {
		return varName;
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
	 * 设置：记录修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：记录修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：记录创建者（用户）
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：记录创建者（用户）
	 */
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * 设置：记录最后修改者（用户）
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * 获取：记录最后修改者（用户）
	 */
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * 设置：数据是否同步(0:是,1:否)
	 */
	public void setIsSync(Integer isSync) {
		this.isSync = isSync;
	}
	/**
	 * 获取：数据是否同步(0:是,1:否)
	 */
	public Integer getIsSync() {
		return isSync;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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
