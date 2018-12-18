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
import java.util.List;

/**
 * 资源表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@ApiModel(value = "资源表")
@TableName("t_sys_resource")
public class SysResource extends Model<SysResource> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
    @ApiModelProperty(value = "主键")
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 父级资源id
	 */
    @ApiModelProperty(value = "父级资源Id")
	@TableId("parent_id")
	private Long parentId;
	/**
	 * 资源名称
	 */
    @ApiModelProperty(value = "资源名称")
	private String name;
	/**
	 * 资源路径
	 */
    @ApiModelProperty(value = "资源路径")
	private String url;
	/**
	 * 资源介绍
	 */
    @ApiModelProperty(value = "资源介绍")
	private String description;
	/**
	 * 资源图标
	 */
    @ApiModelProperty(value = "资源图标")
	private String icon;
	/**
	 * 排序
	 */
    @ApiModelProperty(value = "排序")
	private Integer seq;
	/**
	 * 资源类别(0：菜单，1：按钮)
	 */
    @ApiModelProperty(value = "资源类别(0：菜单，1：按钮)")
	@TableField("resource_type")
	private Integer resourceType;
	/**
	 * 状态(0：开，1：关）
	 */
    @ApiModelProperty(value = "状态(0：开，1：关）")
	private Integer status;
	/**
	 * 记录创建时间
	 */
    @ApiModelProperty(value = "记录创建时间")
	@TableField("create_time")
	private Date createTime;
	/**
	 * 记录最后修改时间
	 */
    @ApiModelProperty(value = "记录最后修改时间")
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 记录创建用户
	 */
    @ApiModelProperty(value = "记录创建用户")
	@TableField("create_user")
	private String createUser;
	/**
	 * 记录最后修改用户
	 */
    @ApiModelProperty(value = "记录最后修改用户")
	@TableField("update_user")
	private String updateUser;

	/**
	 * 父菜单名称
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;

	@TableField(exist=false)
	private List<?> list;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：父级资源Id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父级资源Id
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：资源名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：资源名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：资源路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：资源路径
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：资源介绍
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：资源介绍
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：资源图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 获取：资源图标
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 设置：排序
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	/**
	 * 获取：排序
	 */
	public Integer getSeq() {
		return seq;
	}
	/**
	 * 设置：资源类别(0：菜单，1：按钮)
	 */
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	/**
	 * 获取：资源类别(0：菜单，1：按钮)
	 */
	public Integer getResourceType() {
		return resourceType;
	}
	/**
	 * 设置：状态(0：开，1：关）
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0：开，1：关）
	 */
	public Integer getStatus() {
		return status;
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
	 * 设置：记录最后修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：记录最后修改时间
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
	 * 设置：记录最后修改用户
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * 获取：记录最后修改用户
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
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
