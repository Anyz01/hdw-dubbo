package com.hdw.upms.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdw.upms.entity.SysLog;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.mapper.SysLogMapper;
import com.hdw.upms.mapper.UserMapper;
import com.hdw.upms.service.IUpmsApiService;


/**
 * @author TuMinglong
 * @version 1.0.0
 * @description UpmsApi接口实现层
 * @date 2018年3月7日 下午9:45:27
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}" ,
        group = "hdw-upms"
)
public class UpmsApiServiceImpl implements IUpmsApiService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public UserVo selectByLoginName(String loginName) {

        return userMapper.selectByLoginName(loginName);
    }

    @Override
    public int insertSysLog(SysLog sysLog) {

        return sysLogMapper.insert(sysLog);
    }
}
