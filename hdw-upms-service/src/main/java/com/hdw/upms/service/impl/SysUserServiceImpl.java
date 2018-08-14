package com.hdw.upms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.common.result.PageInfo;
import com.hdw.common.util.BeanUtils;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.entity.SysUserRole;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.mapper.SysUserMapper;
import com.hdw.upms.mapper.SysUserRoleMapper;
import com.hdw.upms.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SysUser 表数据服务层接口实现类
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}" ,
        group = "hdw-upms"
)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public UserVo selectByLoginName(String loginName) {

        return userMapper.selectByLoginName(loginName);
    }

    @Override
    public void insertByVo(UserVo userVo) {
        SysUser user = BeanUtils.copy(userVo, SysUser.class);
        user.setCreateTime(new Date());
        this.insert(user);

        Long id = user.getId();
        String[] roles = userVo.getRoleIds().split(",");
        SysUserRole userRole = new SysUserRole();
        for (String string : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Long.valueOf(string));
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public UserVo selectVoById(Long id) {
        return userMapper.selectUserVoById(id);
    }

    @Override
    public void updateByVo(UserVo userVo) {
        SysUser user = BeanUtils.copy(userVo, SysUser.class);
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        }
        this.updateById(user);

        Long id = userVo.getId();
        List<SysUserRole> userRoles = userRoleMapper.selectByUserId(id);
        if (userRoles != null && !userRoles.isEmpty()) {
            for (SysUserRole userRole : userRoles) {
                userRoleMapper.deleteById(userRole.getId());
            }
        }

        String[] roles = userVo.getRoleIds().split(",");
        SysUserRole userRole = new SysUserRole();
        for (String string : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Long.valueOf(string));
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void setRoles(Long userId, String roleIds) {
        List<SysUserRole> userRoles = userRoleMapper.selectByUserId(userId);
        if (userRoles != null && !userRoles.isEmpty()) {
            for (SysUserRole userRole : userRoles) {
                userRoleMapper.deleteById(userRole.getId());
            }
        }

        String[] roles = roleIds.split(",");
        SysUserRole userRole = new SysUserRole();
        for (String string : roles) {
            userRole.setUserId(userId);
            userRole.setRoleId(Long.valueOf(string));
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void updatePwdByUserId(Long userId, String md5Hex) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(md5Hex);
        this.updateById(user);
    }

    @Override
    public PageInfo selectDataGrid(PageInfo pageInfo) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageInfo.getNowpage(), pageInfo.getSize());
        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
        page.setOrderByField(orderField);
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Map<String, Object>> list = userMapper.selectUserPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public void deleteUserById(Long id) {
        this.deleteById(id);
        userRoleMapper.deleteByUserId(id);
    }

}