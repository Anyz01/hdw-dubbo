package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysOrganization;
import com.hdw.upms.service.ISysOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TuMinglong
 * @description 部门管理
 * @date 2018年3月6日 上午10:01:40
 */

@Api(value = "部门管理接口类" , tags = {"部门管理接口"})
@Controller
@RequestMapping("/organization")
public class SysOrganizationController extends BaseController {

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysOrganizationService organizationService;

    /**
     * 部门管理主页
     *
     * @return
     */

    @GetMapping(value = "/manager")
    public String manager() {
        return "system/organization/organization" ;
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @ApiOperation(value = "获取部门资源树" , notes = "获取部门资源树")
    @PostMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = organizationService.selectTree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 部门树表
     *
     * @return
     */
    @ApiOperation(value = "获取部门资源树表" , notes = "获取部门资源树表")
    @RequestMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid(@RequestParam(required = false) String deptName,
                           @RequestParam(required = false) String deptAddress) {
        Map<String, Object> par = new HashMap<>();
        if (StringUtils.isNotBlank(deptName)) {
            par.put("name" , deptName);
        }
        if (StringUtils.isNotBlank(deptAddress)) {
            par.put("address" , deptAddress);
        }
        return organizationService.selectTreeGrid(par);
    }

    /**
     * 添加部门页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "system/organization/organizationAdd" ;
    }

    /**
     * 添加部门
     *
     * @param organization
     * @return
     */
    @ApiOperation(value = "添加部门信息" , notes = "添加部门信息")

    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SysOrganization organization) throws RuntimeException {
        try {
            organization.setCreateTime(new Date());
            organization.setUpdateTime(new Date());
            organizationService.insert(organization);
            return renderSuccess("添加成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("添加错误，请联系管理员");
        }

    }

    /**
     * 编辑部门页
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage/{id}")
    public String editPage(Model model, @PathVariable("id") Long id) {
        SysOrganization organization = organizationService.selectById(id);
        SysOrganization organization2 = organizationService.selectById(organization.getPid());
        if (organization2 != null) {
            organization.setPname(organization2.getName());
        } else {
            organization.setPname("顶级");
        }
        model.addAttribute("dept" , organization);
        return "system/organization/organizationEdit" ;
    }

    /**
     * 编辑部门
     *
     * @param organization
     * @return
     */
    @ApiOperation(value = "编辑部门信息" , notes = "编辑部门信息")

    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SysOrganization organization) throws RuntimeException {
        try {
            organization.setUpdateTime(new Date());
            organizationService.updateById(organization);
            return renderSuccess("编辑成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("编辑错误，请联系管理员");
        }

    }

    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除部门信息" , notes = "删除部门信息")
    @ApiImplicitParam(name = "deptId" , value = "部门ID" , dataType = "long" , required = true)

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long deptId) throws RuntimeException {
        try {
            organizationService.deleteById(deptId);
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("删除错误，请联系管理员");
        }

    }
}