package com.hdw.upms.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdw.upms.entity.SysLog;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.mapper.SysLogMapper;
import com.hdw.upms.mapper.UserMapper;
import com.hdw.upms.service.IUpmsApiService;


/**
 * 
 * @description UpmsApi接口实现层
 * @author TuMinglong
 * @date 2018年3月7日 下午9:45:27
 * @version 1.0.0
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
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
