package com.hdw.dubbo.upms.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdw.dubbo.upms.entity.Organization;
import com.hdw.dubbo.upms.rpc.api.IOrganizationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @description 部门管理
 * @author TuMinglong
 * @date 2018年3月6日 上午10:01:40
 */

@Api(value = "部门管理接口类", tags = { "部门管理接口" })
@Controller
@RequestMapping("/organization")
public class OrganizationController extends CommonsController {

	@Autowired
	private IOrganizationService organizationService;

	/**
	 * 部门管理主页
	 *
	 * @return
	 */

	@GetMapping(value = "/manager")
	public String manager() {
		return "admin/organization/organization";
	}

	/**
	 * 部门资源树
	 *
	 * @return
	 */
	@ApiOperation(value = "获取部门资源树", notes = "获取部门资源树")

	@GetMapping(value = "/tree")
	@ResponseBody
	public Object tree() {
		return organizationService.selectTree();
	}

	/**
	 * 部门列表
	 *
	 * @return
	 */
	@ApiOperation(value = "获取部门资源树表", notes = "获取部门资源树表")
	@GetMapping("/treeGrid")
	@ResponseBody
	public Object treeGrid() {
		return organizationService.selectTreeGrid();
	}

	/**
	 * 添加部门页
	 *
	 * @return
	 */
	@GetMapping("/addPage")
	public String addPage() {
		return "admin/organization/organizationAdd";
	}

	/**
	 * 添加部门
	 *
	 * @param organization
	 * @return
	 */
	@ApiOperation(value = "添加部门信息", notes = "添加部门信息")

	@PostMapping("/add")
	@ResponseBody
	public Object add(@Valid Organization organization) {
		organization.setCreateTime(new Date());
		organizationService.insert(organization);
		return renderSuccess("添加成功！");
	}

	/**
	 * 编辑部门页
	 *
	 * @param request
	 * @param id
	 * @return
	 */
	@GetMapping("/editPage")
	public String editPage(Model model, Long id) {
		Organization organization = organizationService.selectById(id);
		model.addAttribute("organization", organization);
		return "admin/organization/organizationEdit";
	}

	/**
	 * 编辑部门
	 *
	 * @param organization
	 * @return
	 */
	@ApiOperation(value = "编辑部门信息", notes = "编辑部门信息")

	@PostMapping("/edit")
	@ResponseBody
	public Object edit(@Valid Organization organization) {
		organizationService.updateById(organization);
		return renderSuccess("编辑成功！");
	}

	/**
	 * 删除部门
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除部门信息", notes = "删除部门信息")
	@ApiImplicitParam(name = "id", value = "部门ID", dataType = "long", required = true)

	@GetMapping("/delete")
	@ResponseBody
	public Object delete(Long id) {
		organizationService.deleteById(id);
		return renderSuccess("删除成功！");
	}
}