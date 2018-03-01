package com.hdw.dubbo.upms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author TuMinglong
 * @since 2017-11-14
 */
@TableName("t_sys_dic")
public class SysDic extends Model<SysDic> {

    private static final long serialVersionUID = 1L;

	private Long id;
	@TableField("parent_id")
	private Long parentId;
	private String name;
	private String code;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
