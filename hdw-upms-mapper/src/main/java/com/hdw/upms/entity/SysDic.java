package com.hdw.upms.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@TableName("t_sys_dic")
public class SysDic extends Model<SysDic> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 父变量ID
     */
	@TableField("parent_id")
	private Long parentId;
    /**
     * 变量代码
     */
	@TableField("var_code")
	private String varCode;
    /**
     * 变量名称
     */
	@TableField("var_name")
	private String varName;
    /**
     * 记录创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 记录修改时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 记录创建者（用户）
     */
	@TableField("create_user")
	private String createUser;
    /**
     * 记录最后修改者（用户）
     */
	@TableField("update_user")
	private String updateUser;
	
	/**
     * 父级名称
     */
	@TableField(exist=false)
	private String pname;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getVarCode() {
		return varCode;
	}

	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
