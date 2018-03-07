package com.hdw.dubbo.common.scan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.hdw.dubbo.common.result.Result;
import com.hdw.dubbo.common.util.BeanUtils;
import com.hdw.dubbo.common.util.WebUtils;

/**
 * 
 * @description 异常处理，对ajax类型的异常返回ajax错误，避免页面问题
 * @author TuMinglong
 * @date 2018年3月7日 上午9:42:48
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
	
	private static final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);

	@Autowired
	private JacksonObjectMapper jacksonObjectMapper;

	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		// log记录异常
		log.error(e.getMessage(), e);
		// 非控制器请求照成的异常
		if (!(handler instanceof HandlerMethod)) {
			return new ModelAndView("error/500");
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		if (WebUtils.isAjax(handlerMethod)) {
			Result result = new Result();
			result.setMsg(e.getMessage());
			MappingJackson2JsonView view = new MappingJackson2JsonView();
			view.setObjectMapper(jacksonObjectMapper);
			view.setContentType("text/html;charset=UTF-8");
			return new ModelAndView(view, BeanUtils.toMap(result));
		}

		// 页面指定状态为500，便于上层的resion或者nginx的500页面跳转，由于error/500不适合对用户展示
		// response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return new ModelAndView("error/500").addObject("error", e.getMessage());
	}

}