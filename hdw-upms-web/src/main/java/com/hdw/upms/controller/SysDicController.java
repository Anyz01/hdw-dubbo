package com.hdw.upms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.SelectNode;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.upms.entity.SysDic;
import com.hdw.upms.service.ISysDicService;
import com.hdw.upms.shiro.ShiroKit;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

/**
 * @author TuMinglong
 * @description 数据字典类 前端控制器
 * @date 2018年3月6日 上午10:02:48
 */
@RestController
@RequestMapping("/sys/dic")
public class SysDicController extends BaseController {

    @Reference
    private ISysDicService sysDicService;

    @Reference
    private IEnterpriseService enterpriseService;

    /**
     * 数据字典树表
     *
     * @return
     */
    @GetMapping("/list")
    public Object treeGrid(@RequestParam(required = false) String dicName,
                           @RequestParam(required = false) String dicCode) {
        Map<String, Object> par = new HashMap<>();
        if (StringUtils.isNotBlank(dicName)) {
            par.put("varName", StringUtils.deleteWhitespace(dicName));
        }
        if (StringUtils.isNotBlank(dicCode)) {
            par.put("varCode", StringUtils.deleteWhitespace(dicCode));
        }
        return sysDicService.selectTreeGrid(par);
    }

    /**
     * 选择字典（添加、修改）
     *
     * @return
     */
    @GetMapping("/select/{pid}")
    public ResultMap select(@PathVariable("pid") Long pid) {
        Map<String, Object> par = new HashMap<>();
        if (pid != null && 0 != pid) {
            par.put("parentId", pid);
        }
        List<TreeNode> tree = sysDicService.selectTree(par);
        tree.add(TreeNode.createParent());
        return ResultMap.ok().put("dicList", tree);
    }

    @GetMapping("/info/{dicId}")
    public ResultMap info(@PathVariable("dicId") Long dicId) {
        SysDic sysDic = sysDicService.getById(dicId);
        SysDic sysDic2 = sysDicService.getById(sysDic.getParentId());
        if (sysDic2 != null) {
            sysDic.setParentName(sysDic.getVarName());
        } else {
            sysDic.setParentName("顶级");
        }
        return ResultMap.ok().put("dic", sysDic);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @PostMapping("/save")
    public Object save(@Valid @RequestBody SysDic sysDic) {
        try {
            sysDic.setCreateTime(new Date());
            sysDic.setCreateUser(ShiroKit.getUser().getLoginName());
            boolean b = sysDicService.save(sysDic);
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

    @PostMapping("/update")
    public Object update(@Valid @RequestBody SysDic sysDic) {
        try {
            sysDic.setUpdateTime(new Date());
            sysDic.setUpdateUser(ShiroKit.getUser().getLoginName());
            boolean b = sysDicService.updateById(sysDic);
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
     * @param dicId
     * @return
     */
    @PostMapping("/delete/{dicId}")
    public ResultMap delete(@PathVariable("dicId") Long dicId) {
        try {
            sysDicService.removeById(dicId);
            QueryWrapper<SysDic> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", dicId);
            sysDicService.remove(wrapper);
            return ResultMap.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("批量删除失败，请联系管理员");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public ResultMap deleteBatchIds(@RequestParam Long[] ids) {
        try {
            List<Long> idList = new ArrayList<Long>();
            Collections.addAll(idList, ids);
            if (idList != null && !idList.isEmpty()) {
                sysDicService.removeByIds(Arrays.asList(ids));
                for (Long id : idList) {
                    QueryWrapper<SysDic> wrapper = new QueryWrapper<>();
                    wrapper.eq("parent_id", id);
                    sysDicService.remove(wrapper);
                }
                return ResultMap.ok("删除成功！");
            } else {
                return ResultMap.error("删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("批量删除失败，请联系管理员");
        }
    }

    /**
     * 获取数据字典select树
     *
     * @return
     */
    @GetMapping("/selectNode/{pid}")
    public Object selectTree(@PathVariable("parentId") Long parentId) {
        List<SelectNode> tree = new ArrayList<>();
        Map<String, Object> par = new HashMap<>();
        par.put("parentId", parentId);
        List<Map<String, Object>> list = sysDicService.selectTreeByParentId(par);
        if (!list.isEmpty()) {
            for (Map<String, Object> map : list) {
                SelectNode selectNode = new SelectNode();
                selectNode.setLabel(map.get("varName").toString());
                selectNode.setValue(map.get("id").toString());
                tree.add(selectNode);
            }
        }
        return ResultMap.ok().put("list", tree);
    }
}
