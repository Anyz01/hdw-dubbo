package com.hdw.upms.service;

import com.hdw.upms.entity.SysLog;
import com.hdw.upms.entity.vo.UserVo;


/**
 * 
 * @description upms接口
 * @author TuMinglong
 * @date 2018年3月7日 下午9:40:10
 * @version 1.0.0
 */
public interface IUpmsApiService {

	UserVo selectByLoginName(String loginName);
	  /**
     * 写入操作日志
     * @param record
     * @return
     */
    int insertSysLog(SysLog sysLog);

}
