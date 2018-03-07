package com.hdw.dubbo.upms.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/** 
* @Author ChenShi 
* @Date 2017年12月26日
* @Version 1.0V
* 类说明 企业信息表
*/
@ApiModel(value="企业信息对象",description="企业信息对象")

@TableName("t_base_company")
public class Company extends Model<Company> {

	private static final long serialVersionUID = 1L;
	
	@TableId(type=IdType.UUID)
	private String id;
	
	/** 企业代码*/
	private String code;
	
	/** 企业类型*/
	@TableField("category_code")
	private String categoryCode;
	
	/** 企业名称*/
	private String name;
	
	/** 企业简称*/
	@ApiModelProperty(name="simpleName",value="企业简称",dataType="string",hidden=false,required=false)
	@TableField("simple_name")
	private String simpleName;
	
	/** 企业地址*/
	private String address;
	
	/** 电话*/
	private String tel;
	
	/** 邮箱*/
	private String email;
	
	/** 所属省*/
	private String province;
	
	/** 市*/
	private String city;
	
	/** 县*/
	private String county;
	
	/** 法人*/
	@TableField("legal_man")
    private String legalMan;	
	
	/** 企业代表*/
	@TableField("head_man")
	private String headMan;
	
	/** 手机号*/
	private String mobile;
	
	/** 企业状态  0:禁用 1:正常*/
	private Integer status;
	
    /** 创建时间*/
	@TableField("create_date")
	private Date createDate;
	
	/** 创建人*/
	@TableField("create_user")
	private String createUser;
	
	/** 最后修改时间*/
	@TableField("last_update_date")
	private Date lastUpdateDate;
	
	/** 最后修改人*/
	@TableField("last_update_user")
	private String lastUpdateUser;

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSimpleName() {
		return simpleName;
	}


	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getCounty() {
		return county;
	}


	public void setCounty(String county) {
		this.county = county;
	}


	public String getLegalMan() {
		return legalMan;
	}


	public void setLegalMan(String legalMan) {
		this.legalMan = legalMan;
	}


	public String getHeadMan() {
		return headMan;
	}


	public void setHeadMan(String headMan) {
		this.headMan = headMan;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getCreateUser() {
		return createUser;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public String getLastUpdateUser() {
		return lastUpdateUser;
	}


	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}


	@Override
	protected Serializable pkVal() {
		
		return null;
	}

}
