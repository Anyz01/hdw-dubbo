package com.hdw.dubbo.upms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.upms.shiro.PasswordHash;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.hdw.dubbo.upms.entity.Role;
import com.hdw.dubbo.upms.entity.User;
import com.hdw.dubbo.upms.entity.vo.UserVo;
import com.hdw.dubbo.upms.rpc.api.IUserService;

/**
 * @description：用户管理
 * @author：TuMinglong 
 * @date：2015/10/1 14:51
 */

@Api(value = "用户管理接口类", tags = { "用户管理接口" })
@Controller
@RequestMapping("/user")
public class UserController extends CommonsController {
	@Autowired
	private IUserService userService;
	@Autowired
	private PasswordHash passwordHash;

	/**
	 * 用户管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/user/user";
	}

	/**
	 * 用户管理列表
	 *
	 * @param userVo
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@ApiOperation(value = "获取权限列表", notes = "获取权限列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", value = "页数", dataType = "int", required = true),
			@ApiImplicitParam(name = "rows", value = "行数", dataType = "int", required = true),
			@ApiImplicitParam(name = "sort", value = "根据某属性排序", dataType = "string"),
			@ApiImplicitParam(name = "order", value = "升降序", dataType = "string"),
			@ApiImplicitParam(name = "userVo", value = "升降序") })

	@PostMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(UserVo userVo, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(userVo.getName())) {
			condition.put("name", userVo.getName());
		}
		if (userVo.getOrganizationId() != null) {
			condition.put("organizationId", userVo.getOrganizationId());
		}
		if (userVo.getCreatedateStart() != null) {
			condition.put("startTime", userVo.getCreatedateStart());
		}
		if (userVo.getCreatedateEnd() != null) {
			condition.put("endTime", userVo.getCreatedateEnd());
		}
		pageInfo.setCondition(condition);
		userService.selectDataGrid(pageInfo);
		return pageInfo;
	}

	/**
	 * 添加用户页
	 *
	 * @return
	 */
	@GetMapping("/addPage")
	public String addPage() {
		return "admin/user/userAdd";
	}

	/**
	 * 添加用户
	 *
	 * @param userVo
	 * @return
	 */
	@ApiOperation(value = "添加用户", notes = "添加用户")

	@PostMapping("/add")
	@ResponseBody
	public Object add(@Valid UserVo userVo) {
		List<User> list = userService.selectByLoginName(userVo);
		if (list != null && !list.isEmpty()) {
			return renderError("登录名已存在!");
		}
//		//shiro算法
//		String salt = UUID.randomUUID().toString();
//		String pwd = passwordHash.toHex(userVo.getPassword(), salt);
//		userVo.setSalt(salt);
//		userVo.setPassword(pwd);
		
		//cas算法
		String pwd = passwordHash.toHexByCas(userVo.getPassword(), userVo.getLoginName());
		userVo.setSalt(userVo.getLoginName());
		userVo.setPassword(pwd);
		userService.insertByVo(userVo);
		return renderSuccess("添加成功");
	}

	/**
	 * 编辑用户页
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/editPage")
	public String editPage(Model model, Long id) {
		UserVo userVo = userService.selectVoById(id);
		List<Role> rolesList = userVo.getRolesList();
		List<Long> ids = new ArrayList<Long>();
		for (Role role : rolesList) {
			ids.add(role.getId());
		}
		model.addAttribute("roleIds", ids);
		model.addAttribute("user", userVo);
		return "admin/user/userEdit";
	}

	/**
	 * 编辑用户
	 *
	 * @param userVo
	 * @return
	 */
	@ApiOperation(value = "编辑用户", notes = "编辑用户")

	@RequiresRoles("admin")
	@PostMapping("/edit")
	@ResponseBody
	public Object edit(@Valid UserVo userVo) {
		List<User> list = userService.selectByLoginName(userVo);
		if (list != null && !list.isEmpty()) {
			return renderError("登录名已存在!");
		}
		// 更新密码
		if (StringUtils.isNotBlank(userVo.getPassword())) {
			User user = userService.selectById(userVo.getId());
			String salt = user.getSalt();
			String pwd = passwordHash.toHex(userVo.getPassword(), salt);
			userVo.setPassword(pwd);
		}
		userService.updateByVo(userVo);
		return renderSuccess("修改成功！");
	}

	/**
	 * 修改密码页
	 *
	 * @return
	 */
	@GetMapping("/editPwdPage")
	public String editPwdPage() {
		return "admin/user/userEditPwd";
	}

	/**
	 * 修改密码
	 *
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	@ApiOperation(value = "修改密码", notes = "修改密码")

	@PostMapping("/editUserPwd")
	@ResponseBody
	public Object editUserPwd(String oldPwd, String pwd) {
		User user = userService.selectById(getUserId());
		String salt = user.getSalt();
		if (!user.getPassword().equals(passwordHash.toHex(oldPwd, salt))) {
			return renderError("老密码不正确!");
		}
		userService.updatePwdByUserId(getUserId(), passwordHash.toHex(pwd, salt));
		return renderSuccess("密码修改成功！");
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@ApiImplicitParam(name = "id", value = "用户ID", dataType = "Long", required = true)

	@RequiresRoles("admin")
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(Long id) {
		Long currentUserId = getUserId();
		if (id == currentUserId) {
			return renderError("不可以删除自己！");
		}
		userService.deleteUserById(id);
		return renderSuccess("删除成功！");
	}

}