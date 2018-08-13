package com.hdw.common.interceptor;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author TuMinglong
 * @Descriptin 上传文件类型拦截器
 * @Date 2018年5月8日 上午10:31:09
 */
@Component
public class FileUploadTypeInterceptor extends HandlerInterceptorAdapter {

    protected static final Logger logger = LoggerFactory.getLogger(FileUploadTypeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean flag = true;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> files = multipartRequest.getFileMap();
            Iterator<String> iterator = files.keySet().iterator();
            // 对多部件请求资源进行遍历
            while (iterator.hasNext()) {
                String formKey = (String) iterator.next();
                MultipartFile multipartFile = multipartRequest.getFile(formKey);
                String filename = multipartFile.getOriginalFilename();
                // 判断是否为限制文件类型
                if (!checkFile(filename)) {
                    new RuntimeException("只允许上传gif,jpg,jpeg,bmp,png,jar,doc,docx,xls,xlsx,pdf,txt,rar格式文件");
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 判断是否为允许的上传文件类型,true表示允许
     */
    private boolean checkFile(String fileName) {
        // 设置允许上传文件类型
        String suffixList = "gif,jpg,jpeg,bmp,png,jar,doc,docx,xls,xlsx,pdf,txt,rar";
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        if (suffixList.contains(suffix.trim().toLowerCase())) {
            return true;
        }
        return false;
    }

}
