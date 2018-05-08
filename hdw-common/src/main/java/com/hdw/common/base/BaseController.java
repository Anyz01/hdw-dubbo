package com.hdw.common.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.baomidou.mybatisplus.plugins.Page;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.Result;
import com.hdw.common.util.StringEscapeEditor;

/**
 * 
 * @description 公用Controller
 * @author TuMinglong
 * @date 2017年10月2日 下午3:36:02
 *
 */
public abstract class BaseController {
	protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class,
				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH"), true));
		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor());
	}

	/**
	 * ajax失败
	 * 
	 * @param msg
	 *            失败的消息
	 * @return {Object}
	 */
	public Object renderError(String msg) {
		Result result = new Result();
		result.setMsg(msg);
		result.setCode(-1);
		return result;
	}

	/**
	 * 
	 * @param code
	 *            状态码
	 * @param msg
	 *            失败信息
	 * @return
	 */
	public Object renderError(Integer code, String msg) {
		Result result = new Result();
		result.setMsg(msg);
		result.setCode(code);
		return result;
	}

	/**
	 * ajax成功
	 * 
	 * @return {Object}
	 */
	public Object renderSuccess() {
		Result result = new Result();
		result.setSuccess(true);
		result.setCode(1);
		return result;
	}

	/**
	 * ajax成功
	 * 
	 * @param msg
	 *            消息
	 * @return {Object}
	 */
	public Object renderSuccess(String msg) {
		Result result = new Result();
		result.setSuccess(true);
		result.setMsg(msg);
		result.setCode(1);
		return result;
	}

	/**
	 * ajax成功
	 * 
	 * @param obj
	 *            成功时的对象
	 * @return {Object}
	 */
	public Object renderSuccess(Object obj) {
		Result result = new Result();
		result.setSuccess(true);
		result.setObj(obj);
		result.setCode(1);
		return result;
	}

	public <T> Page<T> getPage(int current, int size, String sort, String order) {
		Page<T> page = new Page<T>(current, size, sort);
		if ("desc".equals(order)) {
			page.setAsc(false);
		} else {
			page.setAsc(true);
		}
		return page;
	}

	public <T> PageInfo pageToPageInfo(Page<T> page) {
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(page.getRecords());
		pageInfo.setTotal(page.getTotal());
		return pageInfo;
	}

	/**
	 * redirect跳转
	 * 
	 * @param url
	 *            目标url
	 */
	protected String redirect(String url) {
		return new StringBuilder("redirect:").append(url).toString();
	}

}
