package com.hdw.dubbo.upms.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 应用
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-07
 */
@TableName("t_sys_app")
public class SysApp extends Model<SysApp> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号_主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 应用名称
     */
	private String name;
    /**
     * 应用key(唯一)
     */
	@TableField("app_key")
	private String appKey;
    /**
     * 应用安全码
     */
	@TableField("app_secret")
	private String appSecret;
    /**
     * 用户状态（0-正常，1-锁定）
     */
	private Integer status;


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

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
