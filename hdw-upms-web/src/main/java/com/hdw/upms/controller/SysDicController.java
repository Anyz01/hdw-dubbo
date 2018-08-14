package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.Select2Node;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysDic;
import com.hdw.upms.service.ISysDicService;
import com.hdw.upms.shiro.ShiroKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author TuMinglong
 * @description 数据字典类 前端控制器
 * @date 2018年3月6日 上午10:02:48
 */

@Api(value = " 数据字典类" , tags = {" 数据字典接口"})
@Controller
@RequestMapping("/dic")
public class SysDicController extends BaseController {

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysDicService sysDicService;


    /**
     * 获取数据字典select2树
     *
     * @return
     */
    @ApiOperation(value = "获取数据字典select2树" , notes = "获取数据字典select2树")
    @RequestMapping("/select2Tree/{pid}")
    @ResponseBody
    public Object select2Tree(@PathVariable("pid") Long pid) {
        List<Select2Node> s2tList = new ArrayList<>();
        Map<String, Object> par = new HashMap<>();
        par.put("pid" , pid);
        List<ZTreeNode> tree = sysDicService.selectTree(par);
        if (tree != null && !tree.isEmpty()) {
            for (ZTreeNode zn : tree) {
                Select2Node s2n = new Select2Node();
                s2n.setId(zn.getId());
                s2n.setText(zn.getName());
                s2tList.add(s2n);
            }
        }
        return s2tList;
    }

    /**
     * 菜单树
     *
     * @return
     */
    @ApiOperation(value = "获取数据字典树" , notes = "获取数据字典树")
    @RequestMapping("/tree")
    @ResponseBody
    public Object tree() {
        Map<String, Object> par = new HashMap<>();
        List<ZTreeNode> tree = sysDicService.selectTree(par);
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 根据父Id获取菜单树
     *
     * @return
     */
    @ApiOperation(value = "根据父Id获取数据字典树" , notes = "根据父Id获取数据字典树")
    @PostMapping("/selectTreeByPid/{pid}")
    @ResponseBody
    public Object selectTreeByPid(@PathVariable("pid") Long pid) {
        Map<String, Object> par = new HashMap<>();
        par.put("pid" , pid);
        List<ZTreeNode> tree = sysDicService.selectTree(par);
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 资源管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "system/dic/dic" ;
    }

    /**
     * 数据字典树表
     *
     * @return
     */
    @ApiOperation(value = "获取数据字典树表" , notes = "获取数据字典树表")
    @RequestMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid(@RequestParam(required = false) String dicName,
                           @RequestParam(required = false) String dicCode) {
        Map<String, Object> par = new HashMap<>();
        if (StringUtils.isNotBlank(dicName)) {
            par.put("varName" , dicName);
        }
        if (StringUtils.isNotBlank(dicName)) {
            par.put("varCode" , dicCode);
        }
        return sysDicService.selectTreeGrid(par);
    }

    /**
     * 数据列表
     *
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @param dicName
     * @param dicCode
     * @return
     */
    @ApiOperation(value = "获取数据列表" , notes = "获取数据列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "offset" , value = "页数" , dataType = "int" , required = true),
            @ApiImplicitParam(name = "limit" , value = "行数" , dataType = "int" , required = true),
            @ApiImplicitParam(name = "sort" , value = "根据某属性排序" , dataType = "string"),
            @ApiImplicitParam(name = "order" , value = "升降序" , dataType = "string"),
            @ApiImplicitParam(name = "dicName" , value = "字典名称" , dataType = "string"),
            @ApiImplicitParam(name = "dicCode" , value = "字典代码" , dataType = "string")})
    @RequestMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Integer offset, Integer limit, String sort, String order,
                           @RequestParam(required = false) String dicName, @RequestParam(required = false) String dicCode) {
        PageInfo pageInfo = new PageInfo(offset, limit, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dicName)) {
            condition.put("varName" , dicName);
        }
        if (StringUtils.isNotBlank(dicCode)) {
            condition.put("varCode" , dicCode);
        }

        pageInfo.setCondition(condition);
        PageInfo page = sysDicService.selectDataGrid(pageInfo);
        return page;
    }

    /**
     * 添加页面
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model) {
        return "system/dic/dicAdd" ;
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @ApiOperation(value = "添加数据字典信息" , notes = "添加数据字典信息")

    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SysDic sysDic) throws RuntimeException {
        try {
            sysDic.setCreateTime(new Date());
            sysDic.setUpdateTime(new Date());
            sysDic.setCreateUser(ShiroKit.getUser().getLoginName());
            sysDic.setUpdateUser(ShiroKit.getUser().getLoginName());
            boolean b = sysDicService.insert(sysDic);
            if (b) {
                return renderSuccess("添加成功！");
            } else {
                return renderError("添加失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("添加失败，请联系管理员");
        }

    }

    /**
     * 编辑页面
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage/{dicId}")
    public String editPage(Model model, @PathVariable("dicId") Long dicId) {
        SysDic sysDic = sysDicService.selectById(dicId);
        SysDic sysDic2 = sysDicService.selectById(sysDic.getParentId());
        if (sysDic2 != null) {
            sysDic.setPname(sysDic.getVarName());
        } else {
            sysDic.setPname("顶级");
        }
        model.addAttribute("dic" , sysDic);
        return "system/dic/dicEdit" ;
    }

    /**
     * 编辑
     *
     * @param
     * @return
     */
    @ApiOperation(value = "编辑数据字典信息" , notes = "编辑数据字典信息")
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SysDic sysDic) throws RuntimeException {
        try {
            sysDic.setUpdateTime(new Date());
            sysDic.setUpdateUser(ShiroKit.getUser().getLoginName());
            boolean b = sysDicService.updateById(sysDic);
            if (b) {
                return renderSuccess("编辑成功！");
            } else {
                return renderError("编辑失败！");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("编辑失败，请联系管理员");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除数据字典信息" , notes = "批量删除数据字典信息")
    @ApiImplicitParam(name = "ids" , value = "字典Id数组" , paramType = "ids" , required = true)

    @PostMapping("/deleteBatchIds")
    @ResponseBody
    public Object deleteBatchIds(@RequestParam Long[] ids) throws RuntimeException {
        try {
            List<Long> idList = new ArrayList<Long>();
            Collections.addAll(idList, ids);
            if (idList != null && !idList.isEmpty()) {
                sysDicService.deleteBatchIds(idList);
                return renderSuccess("删除成功！");
            } else {
                return renderError("删除失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("批量删除失败，请联系管理员");
        }
    }

    /**
     * 删除
     *
     * @param dicId
     * @return
     */
    @ApiOperation(value = "删除数据字典信息" , notes = "删除数据字典信息")
    @ApiImplicitParam(name = "dicId" , value = "字典Id" , paramType = "ids" , required = true)

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long dicId) throws RuntimeException {
        try {
            Map<String, Object> par = new HashMap<>();
            sysDicService.deleteById(dicId);
            par.put("parent_id" , dicId);
            sysDicService.deleteByMap(par);
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("删除失败，请联系管理员");
        }
    }

}
