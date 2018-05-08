package com.hdw.upms.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.upms.entity.Resource;
import com.hdw.upms.entity.vo.RoleVo;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IUpmsApiService;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class ShiroCasRealm extends Pac4jRealm {

	private static final Logger LOGGER = LogManager.getLogger(ShiroCasRealm.class);

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private IUpmsApiService upmsApiService;
	
	public ShiroCasRealm() {
		super();
	}

	/**
	 * Shiro登录认证(原理：用户提交 用户名和密码 --- shiro 封装令牌 ---- realm 通过用户名将密码查询返回 ---- shiro
	 * 自动去比较查询出密码和用户输入密码是否一致---- 进行登陆控制 )
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {

		LOGGER.info("Shiro开始登录认证");
		Pac4jToken token = (Pac4jToken) authcToken;
		LinkedHashMap<String, CommonProfile> profiles = token.getProfiles();
		Pac4jPrincipal principal = new Pac4jPrincipal(profiles);

		String loginName = principal.getProfile().getId();

		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute("userSessionId", loginName );

		UserVo userVo= upmsApiService.selectByLoginName(loginName);
		// 账号不存在
		if (userVo == null) {
			return null;
		}
		// 账号未启用
		if (userVo.getStatus() == 1) {
			return null;
		}
		// 认证缓存信息
		return new SimpleAuthenticationInfo(userVo, profiles.hashCode(), getName());
	}

	/**
	 * Shiro权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		Session session = SecurityUtils.getSubject().getSession();  
        String loginName = (String)session.getAttribute("userSessionId");

		UserVo userVo= upmsApiService.selectByLoginName(loginName);
		// 账号不存在
		if (userVo == null) {
			return null;
		}
		// 账号未启用
		if (userVo.getStatus() == 1) {
			return null;
		}

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 设置相应角色的权限信息
		for (RoleVo role : userVo.getRolesList()) {
			// 设置角色
			authorizationInfo.addRole(role.getName());
			for (Resource r : role.getPermissions()) {
				// 设置权限
				authorizationInfo.addStringPermission(r.getUrl());
			}
		}

		return authorizationInfo;
	}

	@Override
	public void onLogout(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
		Session session = SecurityUtils.getSubject().getSession();  
        String loginName = (String)session.getAttribute("userSessionId");
        LOGGER.error("从session中获取的userSessionId："+loginName); 
        removeUserCache(loginName);
		
	}

	/**
	 * 清除用户缓存
	 * @param userVo
	 */
	public void removeUserCache(UserVo userVo) {
		removeUserCache(userVo.getLoginName());
	}

	/**
	 * 清除用户缓存
	 * 
	 * @param loginName
	 */
	public void removeUserCache(String loginName) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection();
		principals.add(loginName, super.getName());
		super.clearCachedAuthenticationInfo(principals);
	}

}
