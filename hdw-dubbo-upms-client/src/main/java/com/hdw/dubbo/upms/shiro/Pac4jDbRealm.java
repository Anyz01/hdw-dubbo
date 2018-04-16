package com.hdw.dubbo.upms.shiro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdw.dubbo.upms.entity.ShiroUser;
import com.hdw.dubbo.upms.entity.User;
import com.hdw.dubbo.upms.entity.vo.UserVo;
import com.hdw.dubbo.upms.rpc.api.IUpmsApiService;

import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;

@Component
public class Pac4jDbRealm extends Pac4jRealm {

	private static final Logger LOGGER = LogManager.getLogger(Pac4jDbRealm.class);

	@Autowired
	private IUpmsApiService upmsApiService;
	
	public Pac4jDbRealm() {
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

		UserVo uservo = new UserVo();
		uservo.setLoginName(loginName);
		List<User> list = upmsApiService.selectByLoginName(uservo);
		// 账号不存在
		if (list == null || list.isEmpty()) {
			return null;
		}
		User user = list.get(0);
		// 账号未启用
		if (user.getStatus() == 1) {
			return null;
		}
		// 读取用户的url和角色
		Map<String, Set<String>> resourceMap = upmsApiService.selectResourceMapByUserId(user.getId());
		Set<String> urls = resourceMap.get("urls");
		Set<String> roles = resourceMap.get("roles");
		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName(), urls);
		shiroUser.setRoles(roles);
		// 认证缓存信息
		return new SimpleAuthenticationInfo(shiroUser, profiles.hashCode(), getName());
	}

	/**
	 * Shiro权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		Session session = SecurityUtils.getSubject().getSession();  
        String loginName = (String)session.getAttribute("userSessionId"); 
		
		UserVo uservo = new UserVo();
		uservo.setLoginName(loginName);
		List<User> list = upmsApiService.selectByLoginName(uservo);
		// 账号不存在
		if (list == null || list.isEmpty()) {
			return null;
		}
		User user = list.get(0);
		// 账号未启用
		if (user.getStatus() == 1) {
			return null;
		}
		// 读取用户的url和角色
		Map<String, Set<String>> resourceMap = upmsApiService.selectResourceMapByUserId(user.getId());
		Set<String> urls = resourceMap.get("urls");
		Set<String> roles = resourceMap.get("roles");
		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName(), urls);
		shiroUser.setRoles(roles);
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(shiroUser.getRoles());
		info.addStringPermissions(shiroUser.getUrlSet());

		return info;
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
	 * 
	 * @param shiroUser
	 */
	public void removeUserCache(ShiroUser shiroUser) {
		removeUserCache(shiroUser.getLoginName());
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
