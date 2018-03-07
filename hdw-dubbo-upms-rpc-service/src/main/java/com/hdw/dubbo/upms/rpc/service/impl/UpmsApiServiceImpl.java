package com.hdw.dubbo.upms.rpc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hdw.dubbo.common.result.Tree;
import com.hdw.dubbo.common.util.BeanUtils;
import com.hdw.dubbo.upms.entity.Role;
import com.hdw.dubbo.upms.entity.RoleResource;
import com.hdw.dubbo.upms.entity.SysLog;
import com.hdw.dubbo.upms.entity.User;
import com.hdw.dubbo.upms.entity.UserRole;
import com.hdw.dubbo.upms.entity.vo.UserVo;
import com.hdw.dubbo.upms.mapper.RoleMapper;
import com.hdw.dubbo.upms.mapper.RoleResourceMapper;
import com.hdw.dubbo.upms.mapper.SysLogMapper;
import com.hdw.dubbo.upms.mapper.UserMapper;
import com.hdw.dubbo.upms.mapper.UserRoleMapper;
import com.hdw.dubbo.upms.rpc.api.IUpmsApiService;

/**
 * 
 * @description UpmsApi接口实现层
 * @author TuMinglong
 * @date 2018年3月7日 下午9:45:27
 * @version 1.0.0
 */
public class UpmsApiServiceImpl implements IUpmsApiService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleResourceMapper roleResourceMapper;
	@Autowired
	private SysLogMapper sysLogMapper;

	@Override
	public List<User> selectByLoginName(UserVo userVo) {
		User user = new User();
		user.setLoginName(userVo.getLoginName());
		EntityWrapper<User> wrapper = new EntityWrapper<User>(user);
		if (null != userVo.getId()) {
			wrapper.where("id != {0}", userVo.getId());
		}
		return userMapper.selectList(wrapper);
	}

	@Override
	public void insertByVo(UserVo userVo) {
		User user = BeanUtils.copy(userVo, User.class);
		user.setCreateTime(new Date());
		userMapper.insert(user);

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
		userMapper.updateById(user);

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
	public void updatePwdByUserId(Long userId, String md5Hex) {
		User user = new User();
		user.setId(userId);
		user.setPassword(md5Hex);
		userMapper.updateById(user);
	}

	

	@Override
	public void deleteUserById(Long id) {
		userMapper.deleteById(id);
		userRoleMapper.deleteByUserId(id);
	}

	public List<Role> selectAll() {
		EntityWrapper<Role> wrapper = new EntityWrapper<Role>();
		wrapper.orderBy("seq");
		return roleMapper.selectList(wrapper);
	}

	@Override
	public Object selectTree() {
		List<Tree> trees = new ArrayList<Tree>();
		List<Role> roles = this.selectAll();
		for (Role role : roles) {
			Tree tree = new Tree();
			tree.setId(role.getId());
			tree.setText(role.getName());

			trees.add(tree);
		}
		return trees;
	}

	@Override
	public void updateRoleResource(Long roleId, String resourceIds) {
		// 先删除后添加,有点爆力
		RoleResource roleResource = new RoleResource();
		roleResource.setRoleId(roleId);
		roleResourceMapper.delete(new EntityWrapper<RoleResource>(roleResource));

		// 如果资源id为空，判断为清空角色资源
		if (StringUtils.isBlank(resourceIds)) {
			return;
		}

		String[] resourceIdArray = resourceIds.split(",");
		for (String resourceId : resourceIdArray) {
			roleResource = new RoleResource();
			roleResource.setRoleId(roleId);
			roleResource.setResourceId(Long.parseLong(resourceId));
			roleResourceMapper.insert(roleResource);
		}
	}

	@Override
	public List<Long> selectResourceIdListByRoleId(Long id) {
		return roleMapper.selectResourceIdListByRoleId(id);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public Map<String, Set<String>> selectResourceMapByUserId(Long userId) {
		Map<String, Set<String>> resourceMap = new HashMap<String, Set<String>>();
		List<Long> roleIdList = userRoleMapper.selectRoleIdListByUserId(userId);
		Set<String> urlSet = new HashSet<String>();
		Set<String> roles = new HashSet<String>();
		for (Long roleId : roleIdList) {
			List<Map<Long, String>> resourceList = roleMapper.selectResourceListByRoleId(roleId);
			if (resourceList != null) {
				for (Map<Long, String> map : resourceList) {
					if (StringUtils.isNotBlank(map.get("url"))) {
						urlSet.add(map.get("url"));
					}
				}
			}
			Role role = roleMapper.selectById(roleId);
			if (role != null) {
				roles.add(role.getName());
			}
		}
		resourceMap.put("urls", urlSet);
		resourceMap.put("roles", roles);
		return resourceMap;
	}

	@Override
	public int insertSysLog(SysLog sysLog) {
		
		return sysLogMapper.insert(sysLog);
	}
}
