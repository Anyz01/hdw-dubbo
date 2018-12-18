package com.hdw.enterprise.controller;

import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageUtils;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.entity.Enterprise;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.sys.service.ISysDicService;
import com.hdw.sys.shiro.ShiroKit;
import com.hdw.sys.shiro.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.*;

/**
 * @Description com.hdw.enterprise.controller
 * @Author TuMinglong
 * @Date 2018/12/17 11:14
 */
@Api(value = "企业表接口", tags = {"企业表接口"})
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController extends BaseController {
    @Autowired
    private ISysDicService sysDicService;

    @Autowired
    private IEnterpriseService enterpriseService;

    /**
     * 数据字典树表
     *
     * @return
     */
    @ApiOperation(value = "企业表列表", notes = "企业表列表")
    @GetMapping("/list")
    @RequiresPermissions("enterprise/enterprise/list")
    public Object treeGrid(@RequestParam Map<String,Object> params) {
        ShiroUser shiroUser = ShiroKit.getUser();
        // 不是管理员
        if (shiroUser.getUserType() != 0) {
            params.put("userId", ShiroKit.getUser().getId());
        }
        PageUtils<Map<String, Object>> page = enterpriseService.selectDataGrid(params);
        return ResultMap.ok().put("page", page);
    }

    @ApiOperation(value = "企业信息", notes = "企业信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("enterprise/enterprise/info")
    public ResultMap info(@PathVariable("id") String id) {
       Enterprise enterprise=enterpriseService.getById(id);
       return ResultMap.ok().put("enterprise", enterprise);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @ApiOperation(value = "保存企业表信息", notes = "保存企业表信息")
    @PostMapping("/save")
    @RequiresPermissions("enterprise/enterprise/save")
    public Object save(@Valid @RequestBody  Enterprise enterprise) {
        try {
            enterprise.setCreateTime(new Date());
            enterprise.setCreateUser(ShiroKit.getUser().getLoginName());
            boolean b = enterpriseService.save(enterprise);
            if (b) {
                return ResultMap.ok("添加成功！");
            } else {
                return ResultMap.ok("添加失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("添加失败，请联系管理员");
        }

    }

    @ApiOperation(value = "修改企业表信息", notes = "修改企业表信息")
    @PostMapping("/update")
    @RequiresPermissions("enterprise/enterprise/update")
    public Object update(@Valid @RequestBody Enterprise enterprise) {
        try {
            enterprise.setUpdateTime(new Date());
            enterprise.setUpdateUser(ShiroKit.getUser().getLoginName());
            boolean b = enterpriseService.updateById(enterprise);
            if (b) {
                return ResultMap.ok("修改成功！");
            } else {
                return ResultMap.error("修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("编辑失败，请联系管理员");
        }
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除企业表信息", notes = "删除企业表信息")
    @PostMapping("/delete")
    @RequiresPermissions("enterprise/enterprise/delete")
    public ResultMap deleteBatchIds(@RequestParam Long[] ids) {
        enterpriseService.removeByIds(Arrays.asList(ids));
        return ResultMap.ok("删除成功！");
    }

    /**
     * 选择企业（根据区域选择）
     *
     * @return
     */
    @ApiOperation(value = "根据区域选择企业", notes = "根据区域选择企业")
    @GetMapping("/select/areaCode/{areaCode}")
    public ResultMap selectByAreaCode(@PathVariable("areaCode") Long areaCode) {
        Map<String,Object> params=new HashMap<>();
        ShiroUser shiroUser = ShiroKit.getUser();
        // 不是管理员
        if (shiroUser.getUserType() != 0) {
            params.put("userId", ShiroKit.getUser().getId());
        }
        params.put("areaCode",areaCode);
        List<Map<String,Object>> list=enterpriseService.selectEnterpriseList(params);
        return ResultMap.ok().put("list", list);
    }

    /**
     * 选择企业（根据行业选择）
     *
     * @return
     */
    @ApiOperation(value = "根据行业选择企业", notes = "根据行业选择企业")
    @GetMapping("/select/industryCode/{industryCode}")
    public ResultMap selectByIndustryCode(@PathVariable("industryCode") Long industryCode) {
        Map<String,Object> params=new HashMap<>();
        ShiroUser shiroUser = ShiroKit.getUser();
        // 不是管理员
        if (shiroUser.getUserType() != 0) {
            params.put("userId", ShiroKit.getUser().getId());
        }
        params.put("industryCode",industryCode);
        List<Map<String,Object>> list=enterpriseService.selectEnterpriseList(params);
        return ResultMap.ok().put("list", list);
    }


    /**
     * 企业树（按行业）
     *
     * @return
     */
    @ApiOperation(value = "企业树（按行业）", notes = "企业树（按行业）")
    @GetMapping("/selectTreeByAreaCode")
    public ResultMap selectTreeByAreaCode() {
        ShiroUser shiroUser = ShiroKit.getUser();
        List<TreeNode> treeNodeList=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();
        params.put("parentId","16");
        List<Map<String,Object>> dicList=sysDicService.selectTreeByParentId(params);
        if(!dicList.isEmpty() && dicList.size()>0){
            for (Map<String,Object> map: dicList) {
                TreeNode treeNode=new TreeNode();
                treeNode.setId(map.get("id").toString());
                treeNode.setParentId("0");
                treeNode.setName(map.get("varName").toString());

                params.clear();
                // 不是管理员
                if (shiroUser.getUserType() != 0) {
                    params.put("userId", ShiroKit.getUser().getId());
                }
                params.put("areaCode",treeNode.getId());
                List<Map<String,Object>> list=enterpriseService.selectEnterpriseList(params);
                if(!list.isEmpty()&&list.size()>0){
                    for (Map<String,Object> par: list) {
                        TreeNode childNode=new TreeNode();
                        childNode.setId(par.get("id").toString());
                        childNode.setParentId(treeNode.getId());
                        childNode.setName(par.get("enterpriseName").toString());
                        treeNodeList.add(childNode);
                    }
                }
                treeNodeList.add(treeNode);
            }
        }
        return ResultMap.ok().put("list", treeNodeList);
    }
}
