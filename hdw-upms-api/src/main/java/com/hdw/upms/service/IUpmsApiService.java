package com.hdw.upms.service;

import com.hdw.upms.entity.SysLog;
import com.hdw.upms.entity.vo.UserVo;


/**
 * @author TuMinglong
 * @version 1.0.0
 * @description upms接口
 * @date 2018年3月7日 下午9:40:10
 */
public interface IUpmsApiService {

    UserVo selectByLoginName(String loginName);

    /**
     * 写入操作日志
     *
     * @param record
     * @return
     */
    int insertSysLog(SysLog sysLog);

}
