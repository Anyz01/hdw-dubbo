package com.hdw.enterprise.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 企业信息表
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@TableName("t_sys_enterprise")
public class Enterprise extends Model<Enterprise> {

    private static final long serialVersionUID = 1L;
    @TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 企业名称
     */
	@TableField("enterprise_name")
	private String enterpriseName;
    /**
     * 所属行业
     */
	@TableField("industry_code")
	private String industryCode;
    /**
     * 所属区域
     */
	@TableField("area_code")
	private String areaCode;
    /**
     * 企业负责人姓名
     */
	@TableField("main_person")
	private String mainPerson;
    /**
     * 企业负责人移动电话号码
     */
    @TableField("main_person_mobile")
    private String mainPersonMobile;
    /**
     * 企业安全负责人姓名
     */
	@TableField("safe_person")
	private String safePerson;
    /**
     * 企业安全负责人移动电话号码
     */
	@TableField("safe_person_mobile")
	private String safePersonMobile;
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
    /**
     * 记录创建者(用户)
     */
	@TableField("create_user")
	private String createUser;
    /**
     * 记录最后修改者(用户)
     */
	@TableField("update_user")
	private String updateUser;

	/**
	 * 所属行业名称
	 */
	@TableField(exist = false)
	private String industryName;

	/**
	 * 所属区域名称
	 */
	@TableField(exist = false)
	private String areaName;
	
	/**
	 *风险模型类型 
	 */
	@TableField("risk_model")
	private String riskModel;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getMainPerson() {
		return mainPerson;
	}

	public void setMainPerson(String mainPerson) {
		this.mainPerson = mainPerson;
	}

    public String getMainPersonMobile() {
        return mainPersonMobile;
    }

    public void setMainPersonMobile(String mainPersonMobile) {
        this.mainPersonMobile = mainPersonMobile;
	}

	public String getSafePerson() {
		return safePerson;
	}

	public void setSafePerson(String safePerson) {
		this.safePerson = safePerson;
	}

	public String getSafePersonMobile() {
		return safePersonMobile;
	}

	public void setSafePersonMobile(String safePersonMobile) {
		this.safePersonMobile = safePersonMobile;
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

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getRiskModel() {
		return riskModel;
	}

	public void setRiskModel(String riskModel) {
		this.riskModel = riskModel;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
