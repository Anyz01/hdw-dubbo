package com.hdw.dubbo.upms.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdw.dubbo.common.base.BaseController;
import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.upms.entity.Role;
import com.hdw.dubbo.upms.rpc.api.IRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @description：权限管理
 * @author：TuMinglong
 * @date：2015/10/1 14:51
 */

@Api(value = "权限管理类", tags = { "权限管理接口" })
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;

	/**
	 * 权限管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/role/role";
	}

	/**
	 * 权限列表
	 *
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
			@ApiImplicitParam(name = "order", value = "升降序", dataType = "string") })

	@PostMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		roleService.selectDataGrid(pageInfo);
		return pageInfo;
	}

	/**
	 * 权限树
	 *
	 * @return
	 */
	@ApiOperation(value = "获取权限树", notes = "获取权限树")

	@PostMapping("/tree")
	@ResponseBody
	public Object tree() {
		return roleService.selectTree();
	}

	/**
	 * 添加权限页
	 *
	 * @return
	 */
	@GetMapping("/addPage")
	public String addPage() {
		return "admin/role/roleAdd";
	}

	/**
	 * 添加权限
	 *
	 * @param role
	 * @return
	 */
	@ApiOperation(value = "添加权限", notes = "添加权限")

	@PostMapping("/add")
	@ResponseBody
	public Object add(@Valid Role role) {
		roleService.insert(role);
		return renderSuccess("添加成功！");
	}

	/**
	 * 删除权限
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除权限", notes = "删除权限")
	@ApiImplicitParam(name = "id", value = "权限ID", dataType = "Long", required = true)

	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(Long id) {
		roleService.deleteById(id);
		return renderSuccess("删除成功！");
	}

	/**
	 * 编辑权限页
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(Model model, Long id) {
		Role role = roleService.selectById(id);
		model.addAttribute("role", role);
		return "admin/role/roleEdit";
	}

	/**
	 * 删除权限
	 *
	 * @param role
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(@Valid Role role) {
		roleService.updateById(role);
		return renderSuccess("编辑成功！");
	}

	/**
	 * 授权页面
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/grantPage")
	public String grantPage(Model model, Long id) {
		model.addAttribute("id", id);
		return "admin/role/roleGrant";
	}

	/**
	 * 授权页面页面根据角色查询资源
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/findResourceIdListByRoleId")
	@ResponseBody
	public Object findResourceByRoleId(Long id) {
		List<Long> resources = roleService.selectResourceIdListByRoleId(id);
		return renderSuccess(resources);
	}

	/**
	 * 授权
	 *
	 * @param id
	 * @param resourceIds
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/grant")
	@ResponseBody
	public Object grant(Long id, String resourceIds) {
		roleService.updateRoleResource(id, resourceIds);
		return renderSuccess("授权成功！");
	}

}
