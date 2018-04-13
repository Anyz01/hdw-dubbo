
package com.hdw.dubbo.upms.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


import java.security.Principal;

/**
 * 
 * @description IndexController
 * @author TuMinglong
 * @date 2018年4月12日 下午9:12:22
 */
@RestController
public class IndexController {

	@GetMapping("/test/index")
    public String index(HttpServletRequest request, Model model) {
        //用户详细信息
        Principal principal = request.getUserPrincipal();
        model.addAttribute("user", principal);
        //打开index.html页面
        return "index";
    }
}
