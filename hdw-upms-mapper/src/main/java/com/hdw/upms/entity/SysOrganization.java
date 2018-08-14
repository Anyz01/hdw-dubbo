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
 * 组织机构
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@TableName("t_sys_organization")
public class SysOrganization extends Model<SysOrganization> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 组织名
     */
	private String name;
    /**
     * 地址
     */
	private String address;
    /**
     * 编号
     */
	private String code;
    /**
     * 图标
     */
	private String icon;
    /**
     * 父级主键
     */
	private Long pid;
    /**
     * 排序
     */
	private Integer seq;
	
	/**
     * 父级名称
     */
	@TableField(exist=false)
	private String pname;
    
    /**
     * 记录创建时间
     */
	@TableField("create_time")
	private Date createTime;
	
    /**
     * 记录最后修改时间
     */
	@TableField("update_time")
	private Date updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
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
