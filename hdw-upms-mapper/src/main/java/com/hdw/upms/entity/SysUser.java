package com.hdw.upms.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;


import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@TableName("t_sys_user")
public class SysUser extends Model<SysUser> implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 登陆名
     */
	@Length(min = 4, max = 64)
	@TableField("login_name")
	private String loginName;
    /**
     * 用户名
     */
	private String name;
    /**
     * 密码
     */
	@JsonIgnore
	private String password;
    /**
     * 密码加密盐
     */
	@JsonIgnore
	private String salt;
    /**
     * 性别
     */
	private Integer sex;
    /**
     * 年龄
     */
	private Integer age;
    /**
     * 手机号
     */
	private String phone;

	private String email;
    /**
     * 用户类别
     */
	@TableField("user_type")
	private Integer userType;
    /**
     * 用户状态(0-正常，1-不正常)
     */
	private Integer status;
    /**
     * 过期字段（0-不过期，1-过期）
     */
	private Integer expired;
    /**
     * 所属机构
     */
	@TableField("organization_id")
	private Long organizationId;

	@TableField("enterprise_id")
	private Long enterpriseId;
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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getExpired() {
		return expired;
	}

	public void setExpired(Integer expired) {
		this.expired = expired;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	/**
	 * 比较vo和数据库中的用户是否同一个user，采用id比较
	 * @param user 用户
	 * @return 是否同一个人
	 */
	public boolean equalsUser(SysUser user) {
		if (user == null) {
			return false;
		}
		Long userId = user.getId();
		if (id == null || userId == null) {
			return false;
		}
		return id.equals(userId);
	}

}
