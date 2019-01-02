package com.hdw.enterprise.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.result.PageUtils;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.SelectTreeNode;
import com.hdw.common.result.TreeNode;
import com.hdw.common.util.UUIDGenerator;
import com.hdw.enterprise.entity.Enterprise;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.upms.controller.UpLoadController;
import com.hdw.upms.entity.SysFile;
import com.hdw.upms.service.ISysDicService;
import com.hdw.upms.service.ISysFileService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class EnterpriseController extends UpLoadController {
    @Reference
    private ISysDicService sysDicService;

    @Reference
    private IEnterpriseService enterpriseService;

    @Reference
    private ISysFileService sysFileService;

    private Map<String, List<Map<String, String>>> uploadFileUrls = new HashMap<String, List<Map<String, String>>>();

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
    public Object save(@Valid @RequestBody Enterprise enterprise) {
        try {
            enterprise.setId(UUIDGenerator.getOrderNo());
            enterprise.setCreateTime(new Date());
            enterprise.setCreateUser(ShiroKit.getUser().getLoginName());
            boolean b = enterpriseService.save(enterprise);
            saveFile(enterprise.getId());
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
            saveFile(enterprise.getId());
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

    /**
     * 企业树（按行业）
     *
     * @return
     */
    @ApiOperation(value = "企业树（按行业）", notes = "企业树（按行业）")
    @GetMapping("/selectTreeNodeByAreaCode")
    public ResultMap selectTreeNodeByAreaCode() {
        ShiroUser shiroUser = ShiroKit.getUser();
        List<SelectTreeNode> treeNodeList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", "16");
        List<Map<String, Object>> dicList = sysDicService.selectTreeByParentId(params);
        if (!dicList.isEmpty() && dicList.size() > 0) {
            for (Map<String, Object> map : dicList) {
                SelectTreeNode treeNode = new SelectTreeNode();
                treeNode.setId(map.get("id").toString());
                treeNode.setLabel(map.get("varName").toString());
                List<SelectTreeNode> childList = new ArrayList<>();
                params.clear();
                // 不是管理员
                if (shiroUser.getUserType() != 0) {
                    params.put("userId", ShiroKit.getUser().getId());
                }
                params.put("areaCode", treeNode.getId());
                List<Map<String, Object>> list = enterpriseService.selectEnterpriseList(params);
                if (!list.isEmpty() && list.size() > 0) {
                    for (Map<String, Object> par : list) {
                        SelectTreeNode childNode = new SelectTreeNode();
                        childNode.setId(par.get("id").toString());
                        childNode.setLabel(par.get("enterpriseName").toString());
                        childList.add(childNode);
                    }
                }
                treeNode.setChildren(childList);
                treeNodeList.add(treeNode);
            }
        }
        return ResultMap.ok().put("list", treeNodeList);
    }


    /**
     * 上传附件
     */
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile[] files) {
        try {
            List<Map<String, String>> uploadFileUrl = uploads(files, "enterprise");
            String fileName = "";
            String filePath = "";
            if (!uploadFileUrl.isEmpty() && uploadFileUrl.size() > 0) {
                for (Map<String, String> map : uploadFileUrl) {
                    fileName = map.get("fileName");
                    filePath = map.get("filePath");
                }
                setUploadFile(uploadFileUrl);
                return ResultMap.ok().put("filePath", filePath);
            } else {
                return ResultMap.ok().put("filePath", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("上传失败，请联系管理员");
        }
    }

    /**
     * 列示附件
     */
    @GetMapping("/selectFile/{id}")
    public Object listFile(@PathVariable("id") String id) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tableId", "t_enterprise");
        params.put("recordId", id);
        List<SysFile> fileList = sysFileService.selectFileListByTableIdAndRecordId(params);
        return ResultMap.ok().put("fileList", fileList);
    }

    /**
     * 删除附件
     */
    @GetMapping("/deleteFileById/{id}")
    public Object deleteFileById(@PathVariable("id") String id) {
        try {
            SysFile sysFile = sysFileService.getById(id);
            if (sysFile != null) {
                sysFileService.removeById(sysFile.getId());
                deleteFileFromLocal(sysFile.getAttachmentPath());
            }
            return ResultMap.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("删除失败，请联系管理员");
        }
    }

    /**
     * 删除附件(刚上传到后端的附件)
     */
    @GetMapping("/deleteFileByName")
    public Object deleteFileByName(@RequestParam String fileName) {
        try {
            List<Map<String, String>> list = getUploadFile();
            if (StringUtils.isNotBlank(fileName) && list != null && !list.isEmpty()) {
                for (Map<String, String> uploadFileUrl : list) {
                    boolean canDel = false;
                    if (uploadFileUrl.get("fileName").equalsIgnoreCase(fileName)) {
                        deleteFileFromLocal(uploadFileUrl.get("filePath"));
                        canDel = true;
                        break;
                    }
                    if (canDel) {
                        list.remove(uploadFileUrl);
                        break;
                    }
                }
            }
            return ResultMap.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("删除失败,请联系管理员");
        }
    }

    public Object saveFile(String id) {
        try {
            if (getUploadFile() != null) {
                ShiroUser user = ShiroKit.getUser();
                //获取企业ID前缀，生成UUID
                String prefix = "HDWX";
                for (Map<String, String> uploadFileUrl : getUploadFile()) {
                    String fileName = uploadFileUrl.get("fileName");
                    String filePah = uploadFileUrl.get("filePath");
                    SysFile sysFile = new SysFile();
                    sysFile.setId(UUIDGenerator.getEnterpriseId(prefix));
                    sysFile.setRecordId(id);
                    sysFile.setTableId("t_enterprise");
                    sysFile.setAttachmentGroup("企业");
                    sysFile.setAttachmentName(fileName);
                    sysFile.setAttachmentPath(filePah);
                    //附件类型(0-word,1-excel,2-pdf,3-jpg,png,4-其他等)
                    String fileType = fileName.substring(fileName.indexOf("."));
                    if ("doc".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(0);
                    } else if ("xlsx".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(1);
                    } else if ("pdf".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(2);
                    } else if ("jpg".equals(fileType.toLowerCase()) || "png".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(3);
                    } else {
                        sysFile.setAttachmentType(4);
                    }
                    sysFile.setSaveType(0);
                    sysFile.setIsSync(0);
                    sysFile.setCreateTime(new Date());
                    sysFile.setCreateUser(ShiroKit.getUser().getLoginName());
                    sysFileService.save(sysFile);
                }
                resetUploadFile();
            }
            return ResultMap.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("保存失败，请联系管理员");
        }
    }

    private void setUploadFile(List<Map<String, String>> uploadFileUrl) {
        ShiroUser user = ShiroKit.getUser();
        Object o = uploadFileUrls.get(user.getId().toString());
        if (o == null) {
            uploadFileUrls.put(user.getId().toString(), new ArrayList<>());
        }
        uploadFileUrls.get(user.getId().toString()).addAll(uploadFileUrl);
    }

    private List<Map<String, String>> getUploadFile() {
        ShiroUser user = ShiroKit.getUser();
        return uploadFileUrls.get(user.getId().toString());
    }

    private void resetUploadFile() {
        ShiroUser user = ShiroKit.getUser();
        uploadFileUrls.remove(user.getId().toString());
    }
}
