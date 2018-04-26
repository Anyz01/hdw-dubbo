package com.hdw.search.api;


import com.hdw.common.result.PageInfo;
import com.hdw.search.entity.RealData;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 实时数据表 服务类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-08
 */
public interface IRealDataService extends IService<RealData> {
	
	void selectDataGrid(PageInfo pageInfo);
}
