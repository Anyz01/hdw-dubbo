package com.hdw.dubbo.upms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.upms.entity.SysDic;
import com.hdw.dubbo.upms.rpc.api.ISysDicService;

/**
 * 
 * @description  数据字典类型详情表 前端控制器
 * @author TuMinglong
 * @date 2018年3月6日 上午10:02:48
 */
@Controller
@RequestMapping("/enumerationValue")
public class SysDicController extends CommonsController {

    @Autowired private ISysDicService sysDicService;
    
    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Integer page, Integer rows, String sort,String order, String name, Integer parentId) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(name)) {
        	condition.put("name", name);
        }
        if(parentId != null) {
        	condition.put("parentId", parentId);
        }
        pageInfo.setCondition(condition);
        sysDicService.selectDataGrid(pageInfo);
        return pageInfo;
    }
    
    /**
     * 添加页面
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model, Long parentId) {
    	SysDic sysDic = new SysDic();
    	sysDic.setParentId(parentId);
    	model.addAttribute("sysDic", sysDic);
        return "admin/sysDicAdd";
    }
    
    /**
     * 添加
     * @param 
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SysDic sysDic) {
        
        boolean b = sysDicService.insert(sysDic);
        if (b) {
            return renderSuccess("添加成功！");
        } else {
            return renderError("添加失败！");
        }
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam Long[] ids) {
    	List<Long> idList = new ArrayList<Long>();
    	Collections.addAll(idList, ids);
    	if (idList != null && !idList.isEmpty()) {
    		sysDicService.deleteBatchIds(idList);
            return renderSuccess("删除成功！");
        } else {
            return renderError("删除失败！");
        }
    }
    
    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
    	SysDic sysDic = sysDicService.selectById(id);
        model.addAttribute("sysDic", sysDic);
        return "admin/sysDicEdit";
    }
    
    /**
     * 编辑
     * @param 
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SysDic sysDic) {
        boolean b = sysDicService.updateById(sysDic);
        if (b) {
            return renderSuccess("编辑成功！");
        } else {
            return renderError("编辑失败！");
        }
    }
    
}
