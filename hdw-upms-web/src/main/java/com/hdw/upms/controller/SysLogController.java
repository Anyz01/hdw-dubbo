package com.hdw.upms.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.service.ISysLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @description 日志管理
 * @author TuMinglong
 * @date 2018年3月6日 上午9:42:00
 */

@Api(value = "日志管理接口类", tags = { "日志管理接口" })
@Controller
@RequestMapping("/sysLog")
public class SysLogController {

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private ISysLogService sysLogService;

	@GetMapping("/manager")
	public String manager() {
		return "admin/syslog";
	}

	@ApiOperation(value = "获取数据字典信息", notes = "获取数据字典信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
			@ApiImplicitParam(name = "rows", value = "行数", dataType = "int"),
			@ApiImplicitParam(name = "sort", value = "根据某属性排序", dataType = "string", paramType = "sort"),
			@ApiImplicitParam(name = "order", value = "升降序", dataType = "string", paramType = "order"),
			@ApiImplicitParam(name = "name", value = "字典名称", dataType = "string") })

	@PostMapping("/dataGrid")
	@ResponseBody
	public PageInfo dataGrid(Integer page, Integer rows,
			@RequestParam(value = "sort", defaultValue = "create_time") String sort,
			@RequestParam(value = "order", defaultValue = "DESC") String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		sysLogService.selectDataGrid(pageInfo);
		return pageInfo;
	}
}
