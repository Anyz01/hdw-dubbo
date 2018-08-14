package com.hdw.upms.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.util.JacksonUtils;
import com.hdw.upms.entity.SysResource;
import com.hdw.upms.entity.vo.RoleVo;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IUpmsApiService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TuMinglong
 * @Descriptin 身份校验核心类
 * @Date 2018年5月1日 下午2:47:19
 */
public class ShiroDBRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(ShiroDBRealm.class);

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
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
        UserVo userVo = upmsApiService.selectByLoginName(loginName);
        // 账号不存在
        if (userVo == null) {
            return null;
        }
        // 账号未启用
        if (userVo.getStatus() == 1) {
            return null;
        }

        ShiroUser su = userVoToShiroUser(userVo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(su, userVo.getPassword(), // 密码
                ByteSource.Util.bytes(userVo.getCredentialsSalt()), getName());
        return authenticationInfo;
    }

    /**
     * 此方法调用hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权): 1、如果用户正常退出，缓存自动清空； 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。 （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，调用clearCached方法；
     * Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        logger.info("Shiro开始权限配置");
        ShiroUser shiroUser = JacksonUtils.toObject(principals.getPrimaryPrincipal().toString(), ShiroUser.class);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        List<String> roleList = shiroUser.getRoles();
        roles.addAll(roleList);
        info.setRoles(roles);
        info.addStringPermissions(shiroUser.getUrlSet());
        return info;
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
        logger.error("从session中获取的userSessionId：" + ShiroKit.getUser().getLoginName());
        removeUserCache(ShiroKit.getUser());

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

    /**
     * 将UserVo赋值给shiroUser
     *
     * @param userVo
     * @return
     */
    public ShiroUser userVoToShiroUser(UserVo userVo) {
        if (userVo == null) {
            return null;
        } else {
            ShiroUser su = new ShiroUser();
            su.setId(userVo.getId());
            su.setLoginName(userVo.getLoginName());
            su.setOrganizationId(userVo.getOrganizationId());
            su.setEnterpriseId(userVo.getEnterpriseId());
            List<RoleVo> rvList = userVo.getRolesList();
            List<String> urlSet = new ArrayList<>();
            List<String> roles = new ArrayList<>();
            if (rvList != null && !rvList.isEmpty()) {
                for (RoleVo rv : rvList) {
                    roles.add(rv.getName());
                    List<SysResource> rList = rv.getPermissions();
                    if (rList != null && !rList.isEmpty()) {
                        for (SysResource r : rList) {
                            if (StringUtils.isNotBlank(r.getUrl())) {
                                urlSet.add(r.getUrl());
                            }
                        }
                    }
                }
            }
            su.setRoles(roles);
            su.setUrlSet(urlSet);
            return su;
        }
    }
}
