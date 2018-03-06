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

/**
 * @Author ChenShi
 * @Date 2017年12月26日
 * @Version 1.0V 类说明 企业信息控制层
 */
@Controller
@RequestMapping("/company")
public class CompanyController extends CommonsController {

	@Autowired
	private ICompanyService companyService;
	@Autowired
	private IUserService userService;

	@GetMapping("/manager")
	public String manager() {
		return "admin/company/company";
	}

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
	@PostMapping("/delete")
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
