package com.hdw.upms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.common.result.PageInfo;
import com.hdw.common.util.BeanUtils;
import com.hdw.upms.entity.User;
import com.hdw.upms.entity.UserRole;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.mapper.UserMapper;
import com.hdw.upms.mapper.UserRoleMapper;
import com.hdw.upms.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service(
		application = "${dubbo.application.id}",
		protocol = "${dubbo.protocol.id}",
		registry = "${dubbo.registry.id}",
		group = "hdw-upms"
)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;

	@Override
	public UserVo selectByLoginName(String loginName) {

		return userMapper.selectByLoginName(loginName);
	}

	@Override
	public void insertByVo(UserVo userVo) {
		User user = BeanUtils.copy(userVo, User.class);
		user.setCreateTime(new Date());
		this.insert(user);

		Long id = user.getId();
		String[] roles = userVo.getRoleIds().split(",");
		UserRole userRole = new UserRole();
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
		User user = BeanUtils.copy(userVo, User.class);
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword(null);
		}
		this.updateById(user);

		Long id = userVo.getId();
		List<UserRole> userRoles = userRoleMapper.selectByUserId(id);
		if (userRoles != null && !userRoles.isEmpty()) {
			for (UserRole userRole : userRoles) {
				userRoleMapper.deleteById(userRole.getId());
			}
		}

		String[] roles = userVo.getRoleIds().split(",");
		UserRole userRole = new UserRole();
		for (String string : roles) {
			userRole.setUserId(id);
			userRole.setRoleId(Long.valueOf(string));
			userRoleMapper.insert(userRole);
		}
	}

	@Override
	public void setRoles(Long userId, String roleIds) {
		List<UserRole> userRoles = userRoleMapper.selectByUserId(userId);
		if (userRoles != null && !userRoles.isEmpty()) {
			for (UserRole userRole : userRoles) {
				userRoleMapper.deleteById(userRole.getId());
			}
		}

		String[] roles = roleIds.split(",");
		UserRole userRole = new UserRole();
		for (String string : roles) {
			userRole.setUserId(userId);
			userRole.setRoleId(Long.valueOf(string));
			userRoleMapper.insert(userRole);
		}
	}

	@Override
	public void updatePwdByUserId(Long userId, String md5Hex) {
		User user = new User();
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