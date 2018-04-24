package com.hdw.upms.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysDic;


/**
 * <p>
 * 数据字典类型详情表 服务类
 * </p>
 *
 * @author TuMinglong
 * @since 2017-11-14
 */
public interface ISysDicService extends IService<SysDic> {
	
	List<SysDic> selectSysDicList(Map<String, Object> par);
	
	void selectDataGrid(PageInfo pageInfo); 
	/**
	 * 根据parentId删除
	 * @param pid
	 */
	void deleteSysDicByPid(Long pid);
	
	List<SysDic> selectSysDic(Long pid);
}
