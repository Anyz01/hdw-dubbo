package com.hdw.enterprise.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageUtils;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.TreeNode;
import com.hdw.common.util.UUIDGenerator;
import com.hdw.enterprise.entity.EnterpriseJob;
import com.hdw.enterprise.service.IEnterpriseJobService;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

/**
 * @Description com.hdw.enterprise.controller
 * @Author TuMinglong
 * @Date 2018/12/17 11:30
 */
@Api(value = "企业职务配置表接口", tags = {"企业职务配置表接口"})
@RestController
@RequestMapping("enterprise/enterpriseJob")
public class EnterpriseJobController extends BaseController {
    @Reference
    private IEnterpriseJobService enterpriseJobService;

    @Reference
    private IEnterpriseService enterpriseService;

    /**
     * 列表
     */
    @ApiOperation(value = "企业职务配置表列表", notes = "企业职务配置表列表")
    @GetMapping("/list")
    @RequiresPermissions("enterprise/enterpriseJob/list")
    public Object list(@RequestParam Map<String, Object> params) {
        ShiroUser shiroUser = ShiroKit.getUser();
        // 不是管理员
        if (shiroUser.getUserType() != 0) {
            params.put("userId", ShiroKit.getUser().getId());
        }
        PageUtils<EnterpriseJob> page = enterpriseJobService.selectDataGrid(params);
        return ResultMap.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取企业职务配置表", notes = "获取企业职务配置表")
    @GetMapping("/info/{id}")
    @RequiresPermissions("enterprise/enterpriseJob/info")
    public Object info(@PathVariable("id") String id) {
        EnterpriseJob enterpriseJob = enterpriseJobService.getById(id);
        return ResultMap.ok().put("enterpriseJob", enterpriseJob);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存企业职务配置表", notes = "保存企业职务配置表")
    @PostMapping("/save")
    @RequiresPermissions("enterprise/enterpriseJob/save")
    public Object save(@Valid @RequestBody EnterpriseJob enterpriseJob) {
        try {
            //获取企业ID前缀，生成UUID
            ShiroUser shiroUser = ShiroKit.getUser();
            String prefix = "HDWX";
            if (shiroUser.getUserType() == 1) {
                prefix = enterpriseService.getById(ShiroKit.getUser().getEnterpriseId()).getPrefix();
            } else if (shiroUser.getUserType() == 2) {
                prefix = enterpriseService.getById(ShiroKit.getEnterpriseIdByUser().get(0)).getPrefix();
            }
            enterpriseJob.setId(UUIDGenerator.getEnterpriseId(prefix));
            enterpriseJob.setCreateTime(new Date());
            enterpriseJob.setCreateUser(ShiroKit.getUser().getLoginName());
            enterpriseJobService.save(enterpriseJob);
            return ResultMap.ok("添加成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改企业职务配置表", notes = "修改企业职务配置表")
    @PostMapping("/update")
    @RequiresPermissions("enterprise/enterpriseJob/update")
    public Object update(@Valid @RequestBody EnterpriseJob enterpriseJob) {
        try {
            enterpriseJob.setUpdateUser(ShiroKit.getUser().getLoginName());
            enterpriseJob.setUpdateTime(new Date());
            enterpriseJobService.updateById(enterpriseJob);
            return ResultMap.ok("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }

    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除企业职务配置表", notes = "删除企业职务配置表")
    @PostMapping("/delete")
    @RequiresPermissions("enterprise/enterpriseJob/delete")
    public Object delete(@RequestBody String[] ids) {
        try {
            enterpriseJobService.removeByIds(Arrays.asList(ids));
            return ResultMap.ok("删除成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 选择企业职位（添加、修改）
     *
     * @return
     */
    @ApiOperation(value = "选择企业职位（添加、修改）", notes = "选择企业职位（添加、修改）")
    @GetMapping("/select/{enterpriseId}")
    public ResultMap select(@PathVariable("enterpriseId") String enterpriseId) {
        Map<String, Object> par = new HashMap<>();
        if (StringUtils.isNotBlank(enterpriseId) && !"0".equals(enterpriseId) && !"undefined".equals(enterpriseId)) {
            par.put("enterpriseId", enterpriseId);
        }
        par.put("enterpriseId", enterpriseId);
        List<TreeNode> tree = enterpriseJobService.selectTree(par);
        tree.add(TreeNode.createParent());
        return ResultMap.ok().put("jobList", tree);
    }

}
