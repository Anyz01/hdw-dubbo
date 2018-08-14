package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.ISysUserService;
import com.hdw.upms.shiro.ShiroKit;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description：用户管理
 * @author：TuMinglong
 * @date：2015/10/1 14:51
 */

@Api(value = "用户管理接口类" , tags = {"用户管理接口"})
@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysUserService userService;

    /**
     * 用户管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "system/user/user" ;
    }

    /**
     * 用户管理列表
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
    @ApiOperation(value = "获取权限列表" , notes = "获取权限列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "offset" , value = "页数" , dataType = "int" , required = true),
            @ApiImplicitParam(name = "limit" , value = "行数" , dataType = "int" , required = true),
            @ApiImplicitParam(name = "sort" , value = "根据某属性排序" , dataType = "string"),
            @ApiImplicitParam(name = "order" , value = "升降序" , dataType = "string"),
            @ApiImplicitParam(name = "name" , value = "账号/姓名" , dataType = "string"),
            @ApiImplicitParam(name = "deptId" , value = "部门编号" , dataType = "int")
    })

    @RequestMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Integer offset, Integer limit, String sort, String order,
                           @RequestParam(required = false) String name, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptId) {
        PageInfo pageInfo = new PageInfo(offset, limit, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(name)) {
            condition.put("name" , name);
        }
        if (deptId != null) {
            condition.put("organizationId" , deptId);
        }
        if (StringUtils.isNotBlank(startTime)) {
            condition.put("startTime" , startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            condition.put("endTime" , endTime);
        }
        pageInfo.setCondition(condition);
        PageInfo page = userService.selectDataGrid(pageInfo);
        return page;
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "system/user/userAdd" ;
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "添加用户" , notes = "添加用户")
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid SysUser user) throws RuntimeException {
        try {
            UserVo u = userService.selectByLoginName(user.getLoginName());
            if (u != null) {
                return renderError("登录名已存在!");
            }
            String salt = ShiroKit.getRandomSalt(16);
            user.setSalt(salt);
            String pwd = ShiroKit.md5(user.getPassword(), user.getLoginName() + salt);
            user.setPassword(pwd);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userService.insert(user);
            return renderSuccess("添加成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("运行异常，请联系管理员");
        }

    }

    /**
     * 编辑用户页
     *
     * @return
     */
    @GetMapping("/editPage/{userId}")
    public String editPage(Model model, @PathVariable("userId") Long userId) {
        SysUser user = userService.selectById(userId);
        model.addAttribute("user" , user);
        return "system/user/userEdit" ;
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "编辑用户" , notes = "编辑用户")
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid SysUser user) throws RuntimeException {
        try {
            UserVo u = userService.selectByLoginName(user.getLoginName());
            String pwd = ShiroKit.md5(user.getPassword(), u.getLoginName() + u.getSalt());
            user.setPassword(pwd);
            user.setUpdateTime(new Date());
            userService.updateById(user);
            return renderSuccess("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("运行异常，请联系管理员");
        }
    }


    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "删除用户" , notes = "删除用户")
    @ApiImplicitParam(name = "userId" , value = "用户ID" , dataType = "Long" , required = true)

    @RequiresRoles("admin")
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long userId) throws RuntimeException {
        try {
            Long currentUserId = ShiroKit.getUser().getId();
            if (userId == currentUserId) {
                return renderError("不可以删除自己！");
            }
            userService.deleteUserById(userId);
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("运行异常，请联系管理员");
        }

    }

    /**
     * 冻结账户
     *
     * @param userId
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("/freeze")
    @ResponseBody
    public Object freeze(Long userId) throws RuntimeException {
        try {
            SysUser user = userService.selectById(userId);
            if (null == user) {
                return renderError("账户不存在");
            }
            user.setStatus(1);
            user.setUpdateTime(new Date());
            userService.updateById(user);
            return renderSuccess("冻结成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("运行异常，请联系管理员");
        }
    }


    /**
     * 解冻账户
     *
     * @param userId
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("/unfreeze")
    @ResponseBody
    public Object unfreeze(Long userId) throws RuntimeException {
        try {
            SysUser user = userService.selectById(userId);
            if (null == user) {
                return renderError("账户不存在");
            }
            user.setStatus(0);
            user.setUpdateTime(new Date());
            userService.updateById(user);
            return renderSuccess("冻结成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("运行异常请联系管理员");
        }

    }

    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    @PostMapping("/reset")
    @ResponseBody
    public Object reset(Long userId, String pwd, String loginName) throws RuntimeException {
        try {
            SysUser user = null;
            if (userId != null) {
                user = userService.selectById(userId);
            }
            if (StringUtils.isNotBlank(loginName)) {
                user = userService.selectByLoginName(loginName);
            }
            if (null == user) {
                return renderError("账户不存在");
            }
            if (StringUtils.isNotBlank(pwd)) {
                user.setPassword(ShiroKit.md5(pwd, user.getLoginName() + user.getSalt()));
            } else {
                user.setPassword(ShiroKit.md5("123456" , user.getLoginName() + user.getSalt()));
            }
            user.setUpdateTime(new Date());
            userService.updateById(user);
            return renderSuccess("重置密码成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("运行异常，联系管理员");
        }

    }

    /**
     * 跳转到角色分配页面
     *
     * @param userId
     * @param model
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping("roleAssign/{userId}")
    public String roleAssign(@PathVariable("userId") Long userId, Model model) {
        UserVo user = userService.selectVoById(userId);
        model.addAttribute("userId" , userId);
        model.addAttribute("roleList" , user.getRolesList());
        return "system/user/userRoleAssign" ;
    }

    /**
     * 设置角色
     *
     * @param userId
     * @param roleIds
     * @return
     * @throws RuntimeException
     */
    @RequestMapping("/setRole")
    @ResponseBody
    @RequiresRoles("admin")
    public Object setRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") String roleIds) throws RuntimeException {
        try {
            if (userId == null && StringUtils.isBlank(roleIds)) {
                return renderError("授权失败");
            }
            userService.setRoles(userId, roleIds);
            return renderSuccess();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }


}