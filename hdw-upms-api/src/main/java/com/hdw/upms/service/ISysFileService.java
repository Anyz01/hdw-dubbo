package com.hdw.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageUtils;
import com.hdw.sys.entity.SysFile;

import java.util.List;
import java.util.Map;

/**
 * 附件表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface ISysFileService extends IService<SysFile> {

    /**
    * 多表页面信息查询
    * @param params
    * @return
    */
    PageUtils selectDataGrid(Map<String, Object> params);

    /**
    * 多表信息查询
    * @param par
    * @return
    */
    List<Map<String, Object>> selectSysFileList(Map<String, Object> par);


}

