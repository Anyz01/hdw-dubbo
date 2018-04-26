package com.hdw.upms.service;

import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysDic;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
public interface ISysDicService extends IService<SysDic> {
	
	void selectDataGrid(PageInfo pageInfo);
}
