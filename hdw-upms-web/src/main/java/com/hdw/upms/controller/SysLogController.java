package com.hdw.upms.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
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
public class SysLogController extends BaseController{

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private ISysLogService sysLogService;

	/**
	 * 日志管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "system/log/log";
	}

	/**
	 * 日志列表
	 * 
	 * @param offset
	 * @param limit
	 * @param sort
	 * @param order
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @param deptId
	 * @return
	 */
	@ApiOperation(value = "获取日志列表", notes = "获取日志列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "offset", value = "页数", dataType = "int", required = true),
			@ApiImplicitParam(name = "limit", value = "行数", dataType = "int", required = true),
			@ApiImplicitParam(name = "sort", value = "根据某属性排序", dataType = "string"),
			@ApiImplicitParam(name = "order", value = "升降序", dataType = "string"),
			@ApiImplicitParam(name = "loginName", value = "用户名", dataType = "string") })

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(Integer offset, Integer limit, String sort, String order,
			@RequestParam(required = false) String loginName, @RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime) {
	
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(startTime)) {
			condition.put("loginName", loginName);
		}
		if (StringUtils.isNotBlank(startTime)) {
			condition.put("startTime", startTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			condition.put("endTime", endTime);
		}
		
		PageInfo pageInfo = new PageInfo(offset, limit, sort, order);
		pageInfo.setCondition(condition);
		PageInfo page=sysLogService.selectDataGrid(pageInfo);
        
		return page;	
	}
}
