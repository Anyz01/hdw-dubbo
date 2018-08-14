package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.Select2Node;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysRole;
import com.hdw.upms.service.ISysResourceService;
import com.hdw.upms.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description：权限管理
 * @author：TuMinglong @date：2015/10/1 14:51
 */

@Api(value = "权限管理类" , tags = {"权限管理接口"})
@Controller
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysRoleService roleService;

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysResourceService resourceService;

    /**
     * 权限管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "system/role/role" ;
    }

    /**
     * 角色列表
     *
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @param name
     * @param startTime
     * @param endTime
     * @param deptId
     * @return
     */
    @ApiOperation(value = "获取角色列表" , notes = "获取角色列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "offset" , value = "页数" , dataType = "int" , required = true),
            @ApiImplicitParam(name = "limit" , value = "行数" , dataType = "int" , required = true),
            @ApiImplicitParam(name = "sort" , value = "根据某属性排序" , dataType = "string"),
            @ApiImplicitParam(name = "order" , value = "升降序" , dataType = "string"),
            @ApiImplicitParam(name = "roleName" , value = "角色名称" , dataType = "string")})
    @RequestMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Integer offset, Integer limit, String sort, String order, String roleName) {
        PageInfo pageInfo = new PageInfo(offset, limit, sort, order);
        Map<String, Object> par = new HashMap<>();
        if (StringUtils.isNotBlank(roleName)) {
            par.put("name" , roleName);
        }
        pageInfo.setCondition(par);
        PageInfo page = roleService.selectDataGrid(pageInfo);
        System.out.println("到这里前端：" + page.getRows().toString());
        return page;
    }

    /**
     * 权限树
     *
     * @return
     */
    @ApiOperation(value = "获取权限树" , notes = "获取权限树")
    @PostMapping("/tree")
    @ResponseBody
    public List<Select2Node> tree() {
        List<Select2Node> trees = roleService.selectTree();
        return trees;
    }

    /**
     * 用户Id根据获取权限树
     *
     * @return
     */
    @ApiOperation(value = "用户Id根据获取权限树" , notes = "用户Id根据获取权限树")
    @RequestMapping("/selectTreeByUserId/{userId}")
    @ResponseBody
    public List<Select2Node> selectTreeByUserId(@PathVariable("userId") Long userId) {
        List<Select2Node> trees = roleService.selectTreeByUserId(userId);
        return trees;
    }

    /**
     * 添加角色页
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage() {
        return "system/role/roleAdd" ;
    }

    /**
     * 编辑角色页
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/editPage/{roleId}")
    public String editPage(Model model, @PathVariable("roleId") Long roleId) {
        SysRole role = roleService.selectById(roleId);
        model.addAttribute("role" , role);
        return "system/role/roleEdit" ;
    }

    /**
     * 编辑角色
     *
     * @param role
     * @return
     */
    @ApiOperation(value = "编辑角色" , notes = "编辑角色")
    @RequiresRoles("admin")
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SysRole role) throws RuntimeException {
        try {
            if (role.getId() != null) {
                role.setUpdateTime(new Date());
                roleService.updateById(role);
                return renderSuccess("编辑成功！");
            } else {
                Map<String, Object> par = new HashMap<String, Object>();
                par.put("name" , role.getName());
                List<SysRole> list = roleService.selectByMap(par);
                if (list != null && !list.isEmpty()) {
                    return renderError("角色已存在！");
                } else {
                    role.setCreateTime(new Date());
                    role.setUpdateTime(new Date());
                    roleService.insert(role);
                    return renderSuccess("添加成功！");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("运行异常，请联系管理员");
        }
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除权限" , notes = "删除权限")
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long roleId) {
        roleService.deleteById(roleId);
        return renderSuccess("删除成功！");
    }

    /**
     * 授权页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/grantPage/{roleId}")
    public String grantPage(Model model, @PathVariable("roleId") Long roleId) {
        model.addAttribute("roleId" , roleId);
        return "system/role/roleGrant" ;
    }

    /**
     * 授权页面根据角色查询资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/findResourceIdListByRoleId/{roleId}")
    @ResponseBody
    public List<ZTreeNode> findResourceByRoleId(@PathVariable("roleId") Long roleId) {
        List<ZTreeNode> list = resourceService.selectTree();
        List<Long> resources = roleService.selectResourceIdListByRoleId(roleId);
        List<ZTreeNode> trees = new ArrayList<>();
        for (ZTreeNode zn : list) {
            if (resources.contains(zn.getId())) {
                zn.setChecked(true);
            }
            trees.add(zn);
        }
        return trees;
    }

    /**
     * 授权
     *
     * @param id
     * @param resourceIds
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("/grant")
    @ResponseBody
    public Object grant(Long roleId, String resourceIds) throws RuntimeException {
        try {
            roleService.updateRoleResource(roleId, resourceIds);
            return renderSuccess("授权成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("授权失败,请联系管理员");
        }

    }
}
