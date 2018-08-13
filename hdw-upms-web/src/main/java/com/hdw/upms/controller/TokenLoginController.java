package com.hdw.upms.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdw.common.base.BaseController;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author TuMinglong
 * @version 1.0.0
 * @description Token鉴权登录
 * @date 2018年4月19日 上午11:39:52
 */
@Controller
public class TokenLoginController extends BaseController {

//	@SuppressWarnings("rawtypes")
//	@Autowired
//	private JwtGenerator generator;
//
//	@Autowired
//	private CasRestFormClient casRestFormClient;
//
//	@Value("${sso.cas.serviceUrl}")
//	private String serviceUrl;
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@RequestMapping("/user/login")
//	@ResponseBody
//	public Object login(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, Object> model = new HashMap<>();
//		J2EContext context = new J2EContext(request, response);
//		final ProfileManager<CasRestProfile> manager = new ProfileManager(context);
//		final Optional<CasRestProfile> profile = manager.get(true);
//		// 获取ticket
//		TokenCredentials tokenCredentials = casRestFormClient.requestServiceTicket(serviceUrl, profile.get(), context);
//		// 根据ticket获取用户信息
//		final CasProfile casProfile = casRestFormClient.validateServiceTicket(serviceUrl, tokenCredentials, context);
//		// 生成jwt token
//		String token = generator.generate(casProfile);
//		model.put("token", token);
//		return model;
//	}
//
//	@GetMapping("/user/detail")
//	@ResponseBody
//	public Object detail(HttpServletRequest request) {
//		return "users:" + request.getUserPrincipal().getName();
//	}

}
