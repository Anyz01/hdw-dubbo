package com.hdw.dubbo.upms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import com.hdw.dubbo.common.base.BaseController;
import com.hdw.dubbo.upms.shiro.captcha.DreamCaptcha;
import com.hdw.dubbo.upms.entity.ShiroUser;



/**
 * 
 * @description 通用的控制器
 * @author TuMinglong
 * @date 2018年3月6日 上午9:44:10
 */

public abstract class CommonsController extends BaseController{
    @Autowired
    private DreamCaptcha dreamCaptcha;
    
    /**
     * 获取当前登录用户对象
     * @return {ShiroUser}
     */
    public ShiroUser getShiroUser() {
        return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前登录用户id
     * @return {Long}
     */
    public Long getUserId() {
        return this.getShiroUser().getId();
    }

    /**
     * 获取当前登录用户名
     * @return {String}
     */
    public String getStaffName() {
        return this.getShiroUser().getName();
    }
    
  
    /**
     * 图形验证码
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        dreamCaptcha.generate(request, response);
    }
    
 
}
