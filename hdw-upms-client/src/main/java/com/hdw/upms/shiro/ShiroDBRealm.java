package com.hdw.upms.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.util.JacksonUtils;
import com.hdw.upms.entity.Resource;
import com.hdw.upms.entity.vo.RoleVo;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IUpmsApiService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Descriptin 身份校验核心类
 * @author TuMinglong
 * @Date 2018年5月1日 下午2:47:19
 */
public class ShiroDBRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(ShiroDBRealm.class);
	
	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private IUpmsApiService upmsApiService;

	/**
	 * 认证信息.(身份验证) Authentication 是用来验证用户身份
	 *
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		logger.info("Shiro开始权限认证");

		// 获取用户的输入的账号.
		String loginName = (String) token.getPrincipal();

		// 通过loginName从数据库中查找 UserVo对象
		// 根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		UserVo userVo = upmsApiService.selectByLoginName(loginName);
		// 账号不存在
		if (userVo == null) {
			return null;
		}
		// 账号未启用
		if (userVo.getStatus() == 1) {
			return null;
		}

		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userVo, // 用户
				userVo.getPassword(), // 密码
				ByteSource.Util.bytes(userVo.getCredentialsSalt()), // salt=username+salt
				getName() // realm name
		);

		// 明文: 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
		// SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
		// userVo, //用户名
		// userVo.getPassword(), //密码
		// getName() //realm name
		// );
		return authenticationInfo;
	}

	/**
	 * 此方法调用hasRole,hasPermission的时候才会进行回调.
	 * <p>
	 * 权限信息.(授权): 1、如果用户正常退出，缓存自动清空； 2、如果用户非正常退出，缓存自动清空；
	 * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
	 * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，调用clearCached方法；
	 * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
	 *
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*
		 * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行， 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
		 * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了， 缓存过期之后会再次执行。
		 */
		logger.info("Shiro开始权限配置");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		UserVo userVo = JacksonUtils.toObject(principals.getPrimaryPrincipal().toString(),UserVo.class);

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

	/**
	 * 设置认证加密方式
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
		md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.HASH_ALGORITHM_NAME);
		md5CredentialsMatcher.setHashIterations(ShiroKit.HASH_ITERATIONS);
		super.setCredentialsMatcher(md5CredentialsMatcher);
	}
	
	@Override
	public void onLogout(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
        logger.error("从session中获取的userSessionId："+ShiroKit.getUser().getLoginName()); 
        removeUserCache(ShiroKit.getUser());
		
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
