package com.hdw.upms.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hdw.upms.entity.SysLog;
import com.hdw.upms.entity.User;
import com.hdw.upms.entity.vo.UserVo;

/**
 * 
 * @description upms接口
 * @author TuMinglong
 * @date 2018年3月7日 下午9:40:10
 * @version 1.0.0
 */
public interface IUpmsApiService {

	List<User> selectByLoginName(UserVo userVo);

	void insertByVo(UserVo userVo);

	UserVo selectVoById(Long id);

	void updateByVo(UserVo userVo);

	void updatePwdByUserId(Long userId, String md5Hex);

	void deleteUserById(Long id);

	Object selectTree();

	List<Long> selectResourceIdListByRoleId(Long id);

	void updateRoleResource(Long id, String resourceIds);

	Map<String, Set<String>> selectResourceMapByUserId(Long userId);
	
	  /**
     * 写入操作日志
     * @param record
     * @return
     */
    int insertSysLog(SysLog sysLog);

}
