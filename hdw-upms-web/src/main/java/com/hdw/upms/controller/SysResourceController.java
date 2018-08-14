package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysResource;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.ISysResourceService;
import com.hdw.upms.service.IUpmsApiService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;

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
 * @description 资源管理
 * @date 2018年3月6日 上午9:43:01
 */

@Api(value = "资源管理接口类" , tags = {"资源管理接口"})
@Controller
@RequestMapping("/resource")
public class SysResourceController extends BaseController {

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private IUpmsApiService upmsApiService;

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysResourceService resourceService;

    /**
     * 菜单树
     *
     * @return
     */
    @ApiOperation(value = "获取菜单树" , notes = "获取菜单树")
    @GetMapping("/tree")
    @ResponseBody
    public Object tree() {
        ShiroUser shiroUser = ShiroKit.getUser();
        UserVo userVo = upmsApiService.selectByLoginName(shiroUser.getLoginName());
        return resourceService.selectTree(userVo);
    }

    /**
     * 资源管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "system/resource/resource" ;
    }

    /**
     * 资源管理列表
     *
     * @return
     */
    @ApiOperation(value = "获取资源树表" , notes = "获取资源树表")
    @RequestMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid(@RequestParam(required = false) String menuName) {
        Map<String, Object> par = new HashMap<>();
        if (StringUtils.isNotBlank(menuName)) {
            par.put("name" , menuName);
        }
        return resourceService.selectTreeGrid(par);
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     */
    @ApiOperation(value = "获取菜单列表" , notes = "获取菜单列表")
    @RequestMapping(value = "/selectMenuTree")
    @ResponseBody
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = resourceService.selectMenuTree();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 添加资源页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "system/resource/resourceAdd" ;
    }

    /**
     * 添加资源
     *
     * @param resource
     * @return
     */
    @ApiOperation(value = "添加资源信息" , notes = "添加资源信息")
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SysResource resource) throws RuntimeException {
        try {
            resource.setCreateTime(new Date());
            resource.setUpdateTime(new Date());
            resourceService.insert(resource);
            return renderSuccess("添加成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("添加失败，请联系管理员");
        }

    }

    /**
     * 编辑资源页
     *
     * @param model
     * @param resourceId
     * @return
     */
    @GetMapping("/editPage/{resourceId}")
    public String editPage(Model model, @PathVariable("resourceId") Long resourceId) {
        SysResource resource = resourceService.selectById(resourceId);
        // 获取父级菜单名称
        SysResource resource2 = resourceService.selectById(resource.getPid());
        if (resource2 != null) {
            resource.setPname(resource2.getName());
        } else {
            resource.setPname("顶级");
        }

        model.addAttribute("resource" , resource);
        return "system/resource/resourceEdit" ;
    }

    /**
     * 编辑资源
     *
     * @param resource
     * @return
     */
    @ApiOperation(value = "编辑资源信息" , notes = "编辑资源信息")

    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SysResource resource) throws RuntimeException {
        try {
            resource.setUpdateTime(new Date());
            resourceService.updateById(resource);
            return renderSuccess("编辑成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("编辑失败，请联系管理员");
        }

    }

    /**
     * 删除资源
     *
     * @param resourceId
     * @return
     */
    @ApiOperation(value = "删除资源信息" , notes = "删除资源信息")
    @ApiImplicitParam(name = "resourceId" , value = "资源ID" , dataType = "Long" , required = true)
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long resourceId) throws RuntimeException {
        try {
            Map<String, Object> par = new HashMap<>();
            resourceService.deleteById(resourceId);
            par.put("pid" , resourceId);
            resourceService.deleteByMap(par);
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("删除失败，请联系管理员");
        }
    }

}
