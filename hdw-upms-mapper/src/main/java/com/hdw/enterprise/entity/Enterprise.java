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
 * 企业信息表
 * 
 * @author TuMinglong
 * @date 2018-12-11 13:49:00
 */
@ApiModel(value = "企业信息表")
@TableName("t_enterprise")
public class Enterprise extends Model<Enterprise> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
    @ApiModelProperty(value = "主键ID")
	@TableId
	private String id;
	/**
	 * 企业id前缀
	 */
    @ApiModelProperty(value = "企业id前缀")
	private String prefix;
	/**
	 * 企业注册码(工商注册码-三证合一)
	 */
    @ApiModelProperty(value = "企业注册码(工商注册码-三证合一)")
	@TableField("business_license_number")
	private String businessLicenseNumber;
	/**
	 * 企业编号
	 */
    @ApiModelProperty(value = "企业编号")
	@TableField("enterprise_code")
	private String enterpriseCode;
	/**
	 * 企业名称
	 */
    @ApiModelProperty(value = "企业名称")
	@TableField("enterprise_name")
	private String enterpriseName;
	/**
	 * 所属行业
	 */
    @ApiModelProperty(value = "所属行业")
	@TableField("industry_code")
	private String industryCode;
	/**
	 * 所属区域
	 */
    @ApiModelProperty(value = "所属区域")
	@TableField("area_code")
	private String areaCode;
	/**
	 * 企业类型(国企:0，民企:1，私企:2，外企:3)
	 */
    @ApiModelProperty(value = "企业类型(国企:0，民企:1，私企:2，外企:3)")
	@TableField("enterprise_type")
	private Integer enterpriseType;
	/**
	 * 企业联系电话
	 */
    @ApiModelProperty(value = "企业联系电话")
	private String telephone;
	/**
	 * 企业邮箱
	 */
    @ApiModelProperty(value = "企业邮箱")
	private String email;
	/**
	 * 邮政编码
	 */
    @ApiModelProperty(value = "邮政编码")
	@TableField("zip_code")
	private String zipCode;
	/**
	 * 法人
	 */
    @ApiModelProperty(value = "法人")
	@TableField("legal_person")
	private String legalPerson;
	/**
	 * 企业负责人姓名
	 */
    @ApiModelProperty(value = "企业负责人姓名")
	@TableField("main_person")
	private String mainPerson;
	/**
	 * 企业负责人移动电话号码
	 */
    @ApiModelProperty(value = "企业负责人移动电话号码")
	@TableField("main_person_mobile")
	private String mainPersonMobile;
	/**
	 * 企业负责人固定电话号码
	 */
    @ApiModelProperty(value = "企业负责人固定电话号码")
	@TableField("main_person_telephone")
	private String mainPersonTelephone;
	/**
	 * 企业安全负责人姓名
	 */
    @ApiModelProperty(value = "企业安全负责人姓名")
	@TableField("safe_person")
	private String safePerson;
	/**
	 * 企业安全负责人移动电话号码
	 */
    @ApiModelProperty(value = "企业安全负责人移动电话号码")
	@TableField("safe_person_mobile")
	private String safePersonMobile;
	/**
	 * 企业安全负责人固定电话号码
	 */
    @ApiModelProperty(value = "企业安全负责人固定电话号码")
	@TableField("safe_person_telephone")
	private String safePersonTelephone;
	/**
	 * x坐标
	 */
    @ApiModelProperty(value = "x坐标")
	@TableField("map_x")
	private String mapX;
	/**
	 * y坐标
	 */
    @ApiModelProperty(value = "y坐标")
	@TableField("map_y")
	private String mapY;
	/**
	 * z坐标
	 */
    @ApiModelProperty(value = "z坐标")
	@TableField("map_z")
	private String mapZ;
	/**
	 * 地址
	 */
    @ApiModelProperty(value = "地址")
	private String address;
	/**
	 * 数据是否同步(0:是,1:否)
	 */
    @ApiModelProperty(value = "数据是否同步(0:是,1:否)")
	@TableField("is_sync")
	private Integer isSync;
	/**
	 * 企业状态（0-正常，1-禁用）
	 */
    @ApiModelProperty(value = "企业状态（0-正常，1-禁用）")
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
	 * 设置：主键ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：主键ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：企业id前缀
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * 获取：企业id前缀
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * 设置：企业注册码(工商注册码-三证合一)
	 */
	public void setBusinessLicenseNumber(String businessLicenseNumber) {
		this.businessLicenseNumber = businessLicenseNumber;
	}
	/**
	 * 获取：企业注册码(工商注册码-三证合一)
	 */
	public String getBusinessLicenseNumber() {
		return businessLicenseNumber;
	}
	/**
	 * 设置：企业编号
	 */
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	/**
	 * 获取：企业编号
	 */
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	/**
	 * 设置：企业名称
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	/**
	 * 获取：企业名称
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}
	/**
	 * 设置：所属行业
	 */
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	/**
	 * 获取：所属行业
	 */
	public String getIndustryCode() {
		return industryCode;
	}
	/**
	 * 设置：所属区域
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	/**
	 * 获取：所属区域
	 */
	public String getAreaCode() {
		return areaCode;
	}
	/**
	 * 设置：企业类型(国企:0，民企:1，私企:2，外企:3)
	 */
	public void setEnterpriseType(Integer enterpriseType) {
		this.enterpriseType = enterpriseType;
	}
	/**
	 * 获取：企业类型(国企:0，民企:1，私企:2，外企:3)
	 */
	public Integer getEnterpriseType() {
		return enterpriseType;
	}
	/**
	 * 设置：企业联系电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：企业联系电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：企业邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：企业邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：邮政编码
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * 获取：邮政编码
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * 设置：法人
	 */
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	/**
	 * 获取：法人
	 */
	public String getLegalPerson() {
		return legalPerson;
	}
	/**
	 * 设置：企业负责人姓名
	 */
	public void setMainPerson(String mainPerson) {
		this.mainPerson = mainPerson;
	}
	/**
	 * 获取：企业负责人姓名
	 */
	public String getMainPerson() {
		return mainPerson;
	}
	/**
	 * 设置：企业负责人移动电话号码
	 */
	public void setMainPersonMobile(String mainPersonMobile) {
		this.mainPersonMobile = mainPersonMobile;
	}
	/**
	 * 获取：企业负责人移动电话号码
	 */
	public String getMainPersonMobile() {
		return mainPersonMobile;
	}
	/**
	 * 设置：企业负责人固定电话号码
	 */
	public void setMainPersonTelephone(String mainPersonTelephone) {
		this.mainPersonTelephone = mainPersonTelephone;
	}
	/**
	 * 获取：企业负责人固定电话号码
	 */
	public String getMainPersonTelephone() {
		return mainPersonTelephone;
	}
	/**
	 * 设置：企业安全负责人姓名
	 */
	public void setSafePerson(String safePerson) {
		this.safePerson = safePerson;
	}
	/**
	 * 获取：企业安全负责人姓名
	 */
	public String getSafePerson() {
		return safePerson;
	}
	/**
	 * 设置：企业安全负责人移动电话号码
	 */
	public void setSafePersonMobile(String safePersonMobile) {
		this.safePersonMobile = safePersonMobile;
	}
	/**
	 * 获取：企业安全负责人移动电话号码
	 */
	public String getSafePersonMobile() {
		return safePersonMobile;
	}
	/**
	 * 设置：企业安全负责人固定电话号码
	 */
	public void setSafePersonTelephone(String safePersonTelephone) {
		this.safePersonTelephone = safePersonTelephone;
	}
	/**
	 * 获取：企业安全负责人固定电话号码
	 */
	public String getSafePersonTelephone() {
		return safePersonTelephone;
	}
	/**
	 * 设置：x坐标
	 */
	public void setMapX(String mapX) {
		this.mapX = mapX;
	}
	/**
	 * 获取：x坐标
	 */
	public String getMapX() {
		return mapX;
	}
	/**
	 * 设置：y坐标
	 */
	public void setMapY(String mapY) {
		this.mapY = mapY;
	}
	/**
	 * 获取：y坐标
	 */
	public String getMapY() {
		return mapY;
	}
	/**
	 * 设置：z坐标
	 */
	public void setMapZ(String mapZ) {
		this.mapZ = mapZ;
	}
	/**
	 * 获取：z坐标
	 */
	public String getMapZ() {
		return mapZ;
	}
	/**
	 * 设置：地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：地址
	 */
	public String getAddress() {
		return address;
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
	/**
	 * 设置：企业状态（0-正常，1-禁用）
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：企业状态（0-正常，1-禁用）
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

    @Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
        return JacksonUtils.toJson(this);
	}
}
