package com.hdw.upms.shiro.cas;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.upms.entity.SysResource;
import com.hdw.upms.entity.vo.RoleVo;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IUpmsApiService;
import com.hdw.upms.shiro.ShiroUser;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.commons.lang3.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ShiroCasRealm extends Pac4jRealm {

    private final static Logger logger = LoggerFactory.getLogger(ShiroCasRealm.class);

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
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

        logger.info("Shiro开始登录认证");
        Pac4jToken token = (Pac4jToken) authcToken;
        LinkedHashMap<String, CommonProfile> profiles = token.getProfiles();
        Pac4jPrincipal principal = new Pac4jPrincipal(profiles);

        String loginName = principal.getProfile().getId();

        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user" , loginName);

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
        // 认证缓存信息
        return new SimpleAuthenticationInfo(su, profiles.hashCode(), getName());
    }

    /**
     * Shiro权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("Shiro开始权限配置");
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
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
        Session session = SecurityUtils.getSubject().getSession();
        String loginName = (String) session.getAttribute("userSessionId");
        logger.error("从session中获取的userSessionId：" + loginName);
        removeUserCache(loginName);

    }

    /**
     * 清除用户缓存
     *
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
