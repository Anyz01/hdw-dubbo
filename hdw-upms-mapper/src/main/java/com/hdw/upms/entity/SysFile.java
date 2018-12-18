package com.hdw.upms.entity;


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
 * 附件表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@ApiModel(value = "附件表")
@TableName("t_sys_file")
public class SysFile extends Model<SysFile> {
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	/**
	 * 附件类型(哪个表的附件)
	 */
    @ApiModelProperty(value = "附件类型(哪个表的附件)")
	@TableField("table_id")
	private String tableId;
	/**
	 * 附件ID(哪个表的记录Id)
	 */
    @ApiModelProperty(value = "附件ID(哪个表的记录Id)")
	@TableField("record_id")
	private String recordId;
	/**
	 * 表的记录Id下的附件分组的组名
	 */
    @ApiModelProperty(value = "表的记录Id下的附件分组的组名")
	@TableField("attachment_group")
	private String attachmentGroup;
	/**
	 * 附件名称
	 */
    @ApiModelProperty(value = "附件名称")
	@TableField("attachment_name")
	private String attachmentName;
	/**
	 * 附件路径
	 */
    @ApiModelProperty(value = "附件路径")
	@TableField("attachment_path")
	private String attachmentPath;
	/**
	 * 附件类型(0-word,1-excel,2-pdf,3-jpg,png,4-其他)
	 */
    @ApiModelProperty(value = "附件类型(0-word,1-excel,2-pdf,3-jpg,png,4-其他)")
	@TableField("attachment_type")
	private Integer attachmentType;
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
	 * 记录创建者(用户)
	 */
    @ApiModelProperty(value = "记录创建者(用户)")
	@TableField("create_user")
	private String createUser;
	/**
	 * 记录最后修改者(用户)
	 */
    @ApiModelProperty(value = "记录最后修改者(用户)")
	@TableField("update_user")
	private String updateUser;
	/**
	 * 数据是否同步(0:是,1:否)
	 */
    @ApiModelProperty(value = "数据是否同步(0:是,1:否)")
	@TableField("is_sync")
	private Integer isSync;

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	/**
	 * 设置：附件类型(哪个表的附件)
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	/**
	 * 获取：附件类型(哪个表的附件)
	 */
	public String getTableId() {
		return tableId;
	}
	/**
	 * 设置：附件ID(哪个表的记录Id)
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：附件ID(哪个表的记录Id)
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * 设置：表的记录Id下的附件分组的组名
	 */
	public void setAttachmentGroup(String attachmentGroup) {
		this.attachmentGroup = attachmentGroup;
	}
	/**
	 * 获取：表的记录Id下的附件分组的组名
	 */
	public String getAttachmentGroup() {
		return attachmentGroup;
	}
	/**
	 * 设置：附件名称
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	/**
	 * 获取：附件名称
	 */
	public String getAttachmentName() {
		return attachmentName;
	}
	/**
	 * 设置：附件路径
	 */
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	/**
	 * 获取：附件路径
	 */
	public String getAttachmentPath() {
		return attachmentPath;
	}
	/**
	 * 设置：附件类型(0-word,1-excel,2-pdf,3-jpg,png,4-其他)
	 */
	public void setAttachmentType(Integer attachmentType) {
		this.attachmentType = attachmentType;
	}
	/**
	 * 获取：附件类型(0-word,1-excel,2-pdf,3-jpg,png,4-其他)
	 */
	public Integer getAttachmentType() {
		return attachmentType;
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
	 * 设置：记录创建者(用户)
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：记录创建者(用户)
	 */
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * 设置：记录最后修改者(用户)
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * 获取：记录最后修改者(用户)
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

    @Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
        return JacksonUtils.toJson(this);
	}
}
