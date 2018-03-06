package com.hdw.dubbo.upms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.upms.entity.Company;
import com.hdw.dubbo.upms.entity.ShiroUser;
import com.hdw.dubbo.upms.entity.User;
import com.hdw.dubbo.upms.rpc.api.ICompanyService;
import com.hdw.dubbo.upms.rpc.api.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * @description 企业信息控制层
 * @author TuMinglong
 * @date 2018年3月6日 上午10:53:25
 */
@Api(value="企业信息业务接口类",tags= {"企业信息接口"})
@Controller
@RequestMapping("/company")
public class CompanyController extends CommonsController {

	@Autowired
	private ICompanyService companyService;
	@Autowired
	private IUserService userService;

	@GetMapping("/manager")
	public String manager() {
		return "company/company";
	}

	@ApiOperation(value="查询企业信息接口",notes="查询企业信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page",value="页数",dataType="int", required=true),
		@ApiImplicitParam(name="rows",value="行数",dataType="int", required=true),
		@ApiImplicitParam(name="sort",value="根据某属性排序",dataType="string"),
		@ApiImplicitParam(name="order",value="升降序",dataType="string"),
		@ApiImplicitParam(name="name",value="企业名称",dataType="string")})
	
	@PostMapping("/dataGrid")
	@ResponseBody
	public PageInfo dataGrid(Integer page, Integer rows, String sort, String order, String name) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(name)) {
			condition.put("name", name);
		}
		pageInfo.setCondition(condition);
		companyService.selectDataGrid(pageInfo);
		return pageInfo;
	}

	/**
	 * 添加
	 * 
	 * @param
	 * @return
	 */
	@ApiOperation(value="添加企业信息",notes="添加企业信息")
	
	@PostMapping("/add")
	@ResponseBody
	public Object add(@Valid Company company) {
		company.setCreateDate(new Date());
		ShiroUser shiroUser = getShiroUser();
		User user = userService.selectById(shiroUser.getId());
		company.setCreateUser(user.getName());
		boolean b = companyService.insert(company);
		if (b) {
			return renderSuccess("添加成功！");
		} else {
			return renderError("添加失败！");
		}
	}

	/**
	 * 编辑
	 * 
	 * @param
	 * @return
	 */
	@ApiOperation(value="编辑企业信息",notes="编辑企业信息")
	
	@PostMapping("/edit")
	@ResponseBody
	public Object edit(@Valid Company company) {
		company.setLastUpdateDate(new Date());
		ShiroUser shiroUser = getShiroUser();
		User user = userService.selectById(shiroUser.getId());
		company.setLastUpdateUser(user.getName());
		boolean b = companyService.updateById(company);
		if (b) {
			return renderSuccess("编辑成功！");
		} else {
			return renderError("编辑失败！");
		}
	}

	/**
	 * 删除
	 * 
	 * @param
	 * @return
	 */
	@ApiOperation(value="删除企业信息",notes="删除企业信息")
	@ApiImplicitParam(name="ids",value="企业ID数组", paramType="ids" ,required=true)
	
	@GetMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam String[] ids) {
		List<String> idList = new ArrayList<String>();
		Collections.addAll(idList, ids);
		if (idList != null && idList.size() > 0) {
			companyService.deleteBatchIds(idList);
			return renderSuccess("删除成功!");
		} else {
			return renderError("删除失败!");
		}
	}
}
