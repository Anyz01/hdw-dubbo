package com.hdw.upms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysDic;
import com.hdw.upms.mapper.SysDicMapper;
import com.hdw.upms.api.ISysDicService;


/**
 * <p>
 * 数据字典类型详情表 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2017-11-14
 */
public class SysDicServiceImpl extends ServiceImpl<SysDicMapper, SysDic>
		implements ISysDicService {

	@Autowired
	private SysDicMapper SysDicMapper;

	@Override
	public List<SysDic> selectSysDicList(Map<String, Object> par) {

		return SysDicMapper.selectSysDicList(par);
	}

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<SysDic> page = new Page<SysDic>(pageInfo.getNowpage(), pageInfo.getSize());
		String orderField = StringUtils.camelToUnderline(pageInfo.getSort());
		page.setOrderByField(orderField);
		page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
		List<SysDic> list = SysDicMapper.selectSysDicPage(page, pageInfo.getCondition());
		pageInfo.setRows(list);
		pageInfo.setTotal(page.getTotal());
	}

	@Override
	public void deleteSysDicByPid(Long pid) {
		Map<String, Object> par = new HashMap<String, Object>();
		par.put("parent_id", pid);
		SysDicMapper.deleteByMap(par);

	}

	// 通过父类ID查询子类信息
	@Override
	public List<SysDic> selectSysDic(Long pid) {
		Map<String, Object> par = new HashMap<String, Object>();
		par.put("parentId", pid);
		return SysDicMapper.selectSysDic(par);
	}
}
