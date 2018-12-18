package com.hdw.upms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.sys.entity.SysUserEnterprise;
import com.hdw.sys.mapper.SysUserEnterpriseMapper;
import com.hdw.sys.service.ISysUserEnterpriseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 监管用户与企业关联表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@Service
public class SysUserEnterpriseServiceImpl extends ServiceImpl<SysUserEnterpriseMapper, SysUserEnterprise> implements ISysUserEnterpriseService {

    @Override
    public List<String> selectEnterpriseIdByUserId(Long userId) {
        return this.baseMapper.selectEnterpriseIdByUserId(userId);
    }

    @Override
    public void saveOrUpdateUserEnterprise(Long userId, List<String> enterpriseIdList) {
        //先删除企业与用户关系
        Map<String,Object> params=new HashMap<>();
        params.put("user_id",userId);
        this.removeByMap(params);
        if(enterpriseIdList == null || enterpriseIdList.size() == 0){
            return ;
        }

        //保存用户与企业关系
            List<SysUserEnterprise> list = new ArrayList<>(enterpriseIdList.size());
            for(String enterpriseId : enterpriseIdList){
                SysUserEnterprise sysUserEnterprise=new SysUserEnterprise();
                sysUserEnterprise.setUserId(userId);
                sysUserEnterprise.setEnterpriseId(enterpriseId);
                list.add(sysUserEnterprise);
            }
            this.saveBatch(list);
    }

    @Override
    public void deleteBatchByUserIds(Long[] userIds) {
        this.baseMapper.deleteBatchByUserIds(userIds);
    }

    @Override
    public void deleteBatchByEnterpriseIds(String[] enterpriseIds) {
        this.baseMapper.deleteBatchByEnterpriseIds(enterpriseIds);
    }

}
