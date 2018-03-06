package com.hdw.dubbo.common.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.common.result.Result;
import com.hdw.dubbo.common.util.Charsets;
import com.hdw.dubbo.common.util.DateUtil;
import com.hdw.dubbo.common.util.StringEscapeEditor;
import com.hdw.dubbo.common.util.URLUtils;

/**
 * 
 * @description BaseController 公用Controller
 * @author TuMinglong
 * @date 2017年10月2日 下午3:36:02
 *
 */
public abstract class BaseController {
	protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 文件上传根目录
	 */
	@Value("${fileUploadRootURL}")
	private String fileUploadRootURL;

	/**
	 * 文件上传服务器名称
	 */
	@Value("${fileUploadServer}")
	private String fileUploadServer;

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

	/**
	 * 下载文件
	 * 
	 * @param file
	 *            文件
	 */
	protected ResponseEntity<Resource> download(File file) {
		String fileName = file.getName();
		return download(file, fileName);
	}

	/**
	 * 下载
	 * 
	 * @param file
	 *            文件
	 * @param fileName
	 *            生成的文件名
	 * @return {ResponseEntity}
	 */
	protected ResponseEntity<Resource> download(File file, String fileName) {
		Resource resource = new FileSystemResource(file);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String header = request.getHeader("User-Agent");
		// 避免空指针
		header = header == null ? "" : header.toUpperCase();
		HttpStatus status;
		if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
			fileName = URLUtils.encodeURL(fileName, Charsets.UTF_8);
			status = HttpStatus.OK;
		} else {
			fileName = new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
			status = HttpStatus.CREATED;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileName);
		return new ResponseEntity<Resource>(resource, headers, status);
	}

	/**
	 * 多文件上传
	 * 
	 * @param files
	 *            文件
	 * @param dir
	 *            保存文件的文件夹名称
	 * @return
	 * @throws Exception
	 */
	public List<String> uploads(MultipartFile[] files, String dir) throws Exception {

		List<String> fileNames = new ArrayList<String>();
		try {
			File dirFile = null;
			if (StringUtils.isNotBlank(dir)) {
				dirFile = new File(fileUploadRootURL + File.separator + dir + File.separator
						+ DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
			} else {
				new RuntimeException("保存文件的文件夹名称为空");
			}

			// 判断file数组不能为空并且长度大于0
			if (files != null && files.length > 0) {
				// 循环获取file数组中得文件
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					// 保存文件
					String fileName = file.getOriginalFilename();
					File tagetFile = new File(dirFile.getAbsoluteFile() + File.separator + fileName);
					if (!tagetFile.exists()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
						tagetFile.createNewFile();
					}
					file.transferTo(tagetFile);
					fileNames.add("/" + fileUploadServer + "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd")
							+ "/" + fileName);
				}
			}
			return fileNames;

		} catch (Exception e) {
			new RuntimeException("程序运行异常！");
		}
		return null;

	}

	/**
	 * 单个文件上传
	 * 
	 * @param file
	 *            前台传过来文件路径
	 * @param dir
	 *            保存文件的相对路径
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String upload(MultipartFile file, String dir) throws Exception {
		try {
			File dirFile = null;
			if (StringUtils.isNotBlank(dir)) {
				dirFile = new File(fileUploadRootURL + File.separator + dir + File.separator
						+ DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
			} else {
				new RuntimeException("保存文件的文件夹名称为空");
			}

			String fileName = file.getOriginalFilename();
			File tagetFile = new File(dirFile.getAbsoluteFile() + File.separator + fileName);
			if (!tagetFile.exists()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
				tagetFile.createNewFile();
			}
			file.transferTo(tagetFile);
			return "/" + fileUploadServer + "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + fileName;
		} catch (Exception e) {
			new RuntimeException("程序运行异常！");
		}
		return null;
	}

}
