package com.hdw.dubbo.upms.controller;

import org.apache.shiro.SecurityUtils;
import com.hdw.dubbo.common.base.BaseController;
import com.hdw.dubbo.upms.entity.ShiroUser;

/**
 * 
 * @description 通用的控制器
 * @author TuMinglong
 * @date 2018年3月6日 上午9:44:10
 */

public abstract class CommonsController extends BaseController {

	/**
	 * 获取当前登录用户对象
	 * 
	 * @return {ShiroUser}
	 */
	public ShiroUser getShiroUser() {
		return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	}

	/**
	 * 获取当前登录用户id
	 * 
	 * @return {Long}
	 */
	public Long getUserId() {
		return this.getShiroUser().getId();
	}

	/**
	 * 获取当前登录用户名
	 * 
	 * @return {String}
	 */
	public String getStaffName() {
		return this.getShiroUser().getName();
	}

}
