package com.hdw.upms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.csrf.CsrfToken;
import com.hdw.common.result.MenuNode;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IResourceService;
import com.hdw.upms.shiro.IncorrectCaptchaException;
import com.hdw.upms.shiro.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
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
 * 
 * @description 登录退出
 * @author TuMinglong
 * @date 2018年3月6日 上午9:55:46
 */

@Api(value = "登录接口类", tags = { "登录接口" })
@Controller
public class LoginController extends BaseController {

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private IResourceService resourceService;

	/**
	 * 首页
	 *
	 * @return
	 */
	@GetMapping("/")
	public String index() {
		return "redirect:/index";
	}

	/**
	 * 首页
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String index(Model model) {
		UserVo userVo = ShiroKit.getUser();
		List<MenuNode> list = resourceService.selectTree(userVo);
		model.addAttribute("loginName", userVo.getLoginName());
		model.addAttribute("roleName", userVo.getRolesList().get(0).getDescription());
		model.addAttribute("resource", list);
		return "index";
	}

	/**
	 * GET 登录
	 * 
	 * @return {String}
	 */
	@ApiOperation(value = "GET 登录", notes = "GET 登录")

	@GetMapping("/login")
	@CsrfToken(create = true)
	public String login() {
		logger.info("GET请求登录");
		if (SecurityUtils.getSubject().isAuthenticated()) {
			return "redirect:/index";
		}
		return "login";
	}

	/**
	 * POST 登录 shiro 写法
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return {Object}
	 */
	@ApiOperation(value = "POST 登录 ", notes = "POST 登录 ")

	@PostMapping("/login")
	@CsrfToken(remove = true)
	@ResponseBody
	public Object loginPost(HttpServletRequest request, HttpServletResponse response, String username, String password,
			String captcha, @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe)
			throws RuntimeException {
		logger.info("POST请求登录");
		//登录失败从request中获取shiro处理的异常信息。shiroLoginFailure:就是shiro异常类的全类名.
        Object exception = request.getAttribute("shiroLoginFailure");
        String msg;
        if (exception != null) {
            if (UnknownAccountException.class.isInstance(exception)) {
                msg = "用户名不正确，请重新输入";
            } else if (IncorrectCredentialsException.class.isInstance(exception)) {
                msg = "密码错误，请重新输入";
            } else if (IncorrectCaptchaException.class.isInstance(exception)) {
                msg = "验证码错误";
            } else if (AuthenticationException.class.isInstance(exception)) {
                msg = "该用户已被禁用，如有疑问请联系系统管理员。";
            } else {
                msg = "发生未知错误，请联系管理员。";
            }
           
            return renderError(msg);
        }else {
        	return renderSuccess();
        }
     
	}

	/**
	 * 未授权
	 * 
	 * @return {String}
	 */
	@ApiOperation(value = "未授权 ", notes = "未授权 ")

	@GetMapping("/unauth")
	public String unauth() {
		if (SecurityUtils.getSubject().isAuthenticated() == false) {
			return "redirect:/login";
		}
		return "403";
	}

	/**
	 * 退出
	 * 
	 * @return {Result}
	 */
	@ApiOperation(value = "退出 ", notes = "退出")

	@GetMapping("/logout")
	public String logout() {
		logger.info("登出");
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/index";
	}

}
