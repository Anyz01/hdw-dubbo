package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.csrf.CsrfToken;
import com.hdw.common.result.MenuNode;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.ISysResourceService;
import com.hdw.upms.service.IUpmsApiService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author TuMinglong
 * @description 登录退出
 * @date 2018年3月6日 上午9:55:46
 */

@Api(value = "登录接口类" , tags = {"登录接口"})
@Controller
public class SysLoginController extends BaseController {

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private IUpmsApiService upmsApiService;

    @Reference(application = "${dubbo.application.id}" , group = "hdw-upms")
    private ISysResourceService resourceService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index" ;
    }

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        ShiroUser shiroUser = ShiroKit.getUser();
        if (shiroUser == null) {
            return "error/403" ;
        }
        UserVo userVo = upmsApiService.selectByLoginName(shiroUser.getLoginName());
        if (shiroUser.getRoles().isEmpty() && userVo.getRolesList().isEmpty()) {
            return "error/403" ;
        } else {
            List<MenuNode> list = resourceService.selectTree(userVo);
            model.addAttribute("loginName" , userVo.getLoginName());
            model.addAttribute("roleName" , userVo.getRolesList().get(0).getDescription());
            model.addAttribute("resource" , list);
            return "index" ;
        }
    }


    /**
     * GET 登录
     *
     * @return
     */
    @ApiOperation(value = "GET 登录" , notes = "GET 登录")

    @GetMapping("/login")
    @CsrfToken(create = true)
    public String login() {
        logger.info("GET请求登录");
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index" ;
        }
        return "login" ;
    }

    /**
     * POST请求登录
     *
     * @param request
     * @param response
     * @return
     * @throws RuntimeException
     */
    @ApiOperation(value = "POST 登录 " , notes = "POST 登录 ")

    @PostMapping("/login")
    @CsrfToken(remove = true)
    @ResponseBody
    public Object loginPost(HttpServletRequest request, HttpServletResponse response, String username, String password,
                            String captcha, @RequestParam(value = "rememberMe" , defaultValue = "0") Integer rememberMe) throws RuntimeException {
        logger.info("POST请求登录");
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new RuntimeException("密码不能为空");
        }
        if (StringUtils.isBlank(captcha)) {
            throw new RuntimeException("验证码不能为空");
        }
        String sessionCaptcha = (String) request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        logger.info("session中的图形码字符串:" + sessionCaptcha);

        //比对
        if (captcha == null || sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            throw new RuntimeException("验证码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 设置记住密码
        token.setRememberMe(1 == rememberMe);
        try {
            subject.login(token);
            //验证成功,把用户信息放到session中
            ShiroUser user = ShiroKit.getUser();
            request.getSession().setAttribute("user" , user);
            return renderSuccess("登录成功");
        } catch (UnknownAccountException e) {
            throw new RuntimeException("账号不存在！");
        } catch (DisabledAccountException e) {
            throw new RuntimeException("账号未启用！");
        } catch (IncorrectCredentialsException e) {
            throw new RuntimeException("密码错误！" , e);
        } catch (ExcessiveAttemptsException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException("发生未知错误，请联系管理员");
        }
    }


    /**
     * 未授权
     *
     * @return
     */
    @ApiOperation(value = "未授权 " , notes = "未授权 ")

    @GetMapping("/unauth")
    public String unauth() {
        if (SecurityUtils.getSubject().isAuthenticated() == false) {
            return "redirect:/login" ;
        }
        return "error/403" ;
    }


    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "退出 " , notes = "退出")

    @GetMapping("/logout")
    public String logout() {
        logger.info("登出");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/index" ;
    }

}
