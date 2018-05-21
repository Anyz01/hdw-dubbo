package com.hdw.upms.controller;

import com.google.common.base.Charsets;
import com.hdw.common.base.BaseController;
import com.hdw.common.util.DateUtil;
import com.hdw.common.util.URLUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @Descriptin 文件上传下载
 * @author TuMinglong
 * @Date 2018年5月8日 上午10:12:45
 */
public abstract class UpLoadController extends BaseController {

	/**
	 * 文件上传路径前缀
	 */
	@Value("${hdw.file-upload.prefix}")
	private String fileUploadPrefix;

	public String getFileUploadPrefix() {
		return fileUploadPrefix;
	}

	public String getFileUploadServer() {
		return fileUploadServer;
	}

	/**
	 * 文件上传服务器名称
	 */
	@Value("${hdw.file-upload.server}")
	private String fileUploadServer;

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
	public List<String> uploads(MultipartFile[] files, String dir) throws RuntimeException {
		List<String> fileNames = new ArrayList<String>();
		try {
			File dirFile = null;
			if (StringUtils.isNotBlank(dir)) {
				dirFile = new File(fileUploadPrefix + File.separator + dir + File.separator
						+ DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
			} else {
				throw new RuntimeException("保存文件的文件夹名称为空");
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

					if (StringUtils.isNotBlank(fileUploadServer)) {
						fileNames.add("/" + fileUploadServer + "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd")
								+ "/" + fileName);
					} else {
						fileNames.add("/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + fileName);
					}

				}
			}
			return fileNames;

		} catch (Exception e) {
			throw new RuntimeException("保存文件异常，请联系管理员");
		}

	}

	/**
	 * 单个文件上传
	 * 
	 * @param file
	 *            前台传过来文件路径
	 * @param dir
	 *            保存文件的相对路径
	 * @return
	 * @throws Exception
	 */
	public String upload(MultipartFile file, String dir) throws RuntimeException {
		try {
			File dirFile = null;
			if (StringUtils.isNotBlank(dir)) {
				dirFile = new File(fileUploadPrefix + File.separator + dir + File.separator
						+ DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
			} else {
				throw new RuntimeException("保存文件的文件夹名称为空");
			}

			String fileName = file.getOriginalFilename();
			File tagetFile = new File(dirFile.getAbsoluteFile() + File.separator + fileName);
			if (!tagetFile.exists()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
				tagetFile.createNewFile();
			}
			file.transferTo(tagetFile);
			if (StringUtils.isNotBlank(fileUploadServer)) {
				return "/" + fileUploadServer + "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/"
						+ fileName;
			} else {
				return "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + fileName;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("保存文件异常，请联系管理员");
		}
	}

}
