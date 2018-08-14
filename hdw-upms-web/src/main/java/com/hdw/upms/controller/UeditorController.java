package com.hdw.upms.controller;


import com.hdw.upms.ueditor.ActionEnter;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author TuMinglong
 * @Description com.zhuoxun.upms.controller
 * @date 2018/8/5 18:59
 */
@Controller
@RequestMapping("/ueditor")
public class UeditorController extends UpLoadController {

    /**
     * 文件上传路径前缀
     */
    @Value("${file-upload.prefix}")
    private String fileUploadPrefix;

    /**
     * 文件上传服务器名称
     */
    @Value("${file-upload.server}")
    private String fileUploadServer;

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/imgUpload")
    @ResponseBody
    public String imgUpdate(@RequestParam("upfile") MultipartFile upfile) {
        if (upfile.isEmpty()) {
            return "error" ;
        }
        // 获取文件名
        String fileName = upfile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = System.currentTimeMillis() + suffixName;
        File dest = new File(fileUploadPrefix + File.separator + "upload" + File.separator + "ueditor" + File.separator + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            upfile.transferTo(dest);
            String url = fileUploadServer + "/upload/ueditor/" ;
            String config = "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + url + fileName + "\"," +
                    "\"title\": \"" + fileName + "\"," +
                    "\"original\": \"" + fileName + "\"}" ;
            return config;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error" ;
    }
}
