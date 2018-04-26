package com.hdw.upms.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdw.upms.entity.Resource;
import com.hdw.upms.entity.ShiroUser;
import com.hdw.upms.service.IResourceService;
import com.alibaba.dubbo.config.annotation.Reference;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @description 资源管理
 * @author TuMinglong
 * @date 2018年3月6日 上午9:43:01
 */

@Api(value = "资源管理接口类", tags = { "资源管理接口" })
@Controller
@RequestMapping("/resource")
public class ResourceController extends CommonController {

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private IResourceService resourceService;

	/**
	 * 菜单树
	 *
	 * @return
	 */
	@ApiOperation(value = "获取资源树", notes = "获取资源树")

	@GetMapping("/tree")
	@ResponseBody
	public Object tree() {
		ShiroUser shiroUser = getShiroUser();
		return resourceService.selectTree(shiroUser);
	}

	/**
	 * 资源管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/resource/resource";
	}

	/**
	 * 资源管理列表
	 *
	 * @return
	 */
	@ApiOperation(value = "获取资源树表", notes = "获取资源树表")
	@GetMapping("/treeGrid")
	@ResponseBody
	public Object treeGrid() {
		return resourceService.selectAll();
	}

	/**
	 * 添加资源页
	 *
	 * @return
	 */
	@GetMapping("/addPage")
	public String addPage() {
		return "admin/resource/resourceAdd";
	}

	/**
	 * 添加资源
	 *
	 * @param resource
	 * @return
	 */
	@ApiOperation(value = "添加资源信息", notes = "添加资源信息")

	@PostMapping("/add")
	@ResponseBody
	public Object add(@Valid Resource resource) {
		resource.setCreateTime(new Date());
		// 选择菜单时将openMode设置为null
		Integer type = resource.getResourceType();
		if (null != type && type == 0) {
			resource.setOpenMode(null);
		}
		resourceService.insert(resource);
		return renderSuccess("添加成功！");
	}

	/**
	 * 查询所有的菜单
	 */
	@ApiOperation(value = "获取所有的菜单", notes = "获取所有的菜单")

	@GetMapping("/allTree")
	@ResponseBody
	public Object allMenu() {
		return resourceService.selectAllMenu();
	}

	/**
	 * 查询所有的资源tree
	 */
	@ApiOperation(value = "获取所有的资源树", notes = "获取所有的资源树")

	@GetMapping("/allTrees")
	@ResponseBody
	public Object allTree() {
		return resourceService.selectAllTree();
	}

	/**
	 * 编辑资源页
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/editPage")
	public String editPage(Model model, Long id) {
		Resource resource = resourceService.selectById(id);
		model.addAttribute("resource", resource);
		return "admin/resource/resourceEdit";
	}

	/**
	 * 编辑资源
	 *
	 * @param resource
	 * @return
	 */
	@ApiOperation(value = "编辑资源信息", notes = "编辑资源信息")

	@GetMapping("/edit")
	@ResponseBody
	public Object edit(@Valid Resource resource) {
		resourceService.updateById(resource);
		return renderSuccess("编辑成功！");
	}

	/**
	 * 删除资源
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除资源信息", notes = "删除资源信息")
	@ApiImplicitParam(name = "id", value = "资源ID", dataType = "Long", required = true)

	@GetMapping("/delete")
	@ResponseBody
	public Object delete(Long id) {
		resourceService.deleteById(id);
		return renderSuccess("删除成功！");
	}

}
