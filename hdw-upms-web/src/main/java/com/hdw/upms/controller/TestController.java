package com.hdw.upms.controller;

import com.hdw.common.base.BaseController;
import com.hdw.common.result.Result;
import com.hdw.job.task.config.AsyncTask;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.exception.FdfsUnsupportStorePathException;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.concurrent.Future;

/**
 * @author TuMinglong
 * @Descriptin 测试控制器
 * @Date 2018年4月27日 下午2:59:17
 */

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private AsyncTask task;

    /**
     * 文件上传服务器名称
     */
    @Value("${file-upload.server}")
    private String fileUploadServer;

    @GetMapping(value = "/task")
    @ResponseBody
    public Object task() {
        try {
            long start = System.currentTimeMillis();
            Future<String> task1 = task.doTaskOne();
            Future<String> task2 = task.doTaskTwo();
            Future<String> task3 = task.doTaskThree();
            while (true) {
                if (task1.isDone() && task2.isDone() && task3.isDone()) {
                    // 三个任务都调用完成，退出循环等待
                    break;
                }
                Thread.sleep(1000);
            }
            long end = System.currentTimeMillis();
            logger.info("任务全部完成，总耗时：" + (end - start) + "毫秒");
            return renderSuccess(Long.toString(end - start));
        } catch (Exception e) {
            return renderError("运行错误，请联系管理员");
        }
    }

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @RequestMapping("/upload")
    @ResponseBody
    public Object upload() throws IOException {
        // 定义操作的文件
        File file = new File("F://法卡拉瓦环礁.jpg");
        // 上传文件
        StorePath storePath = fastFileStorageClient.uploadFile(IOUtils.toByteArray(new FileInputStream(file)),
                FilenameUtils.getExtension(file.getName()));
        System.out.println("上传文件路径：" + storePath.getFullPath());
        logger.info("图片分组：" + storePath.getGroup() + "图片路径：" + storePath.getFullPath());
        String fileName = storePath.getFullPath().substring(storePath.getFullPath().lastIndexOf("/") + 1);
        return fileUploadServer + "/" + storePath.getFullPath() + "?attname=" + fileName;
    }

    /**
     * 从FastDFS上删除文件
     *
     * @param fileUrl 源文件路径
     */
    @RequestMapping("/deleteFile")
    @ResponseBody
    public Result deleteFileFromFastDFS(String fileUrl) throws RuntimeException {
        if (StringUtils.isEmpty(fileUrl)) {
            Result r = new Result();
            r.setMsg("文件删除失败");
            r.setSuccess(false);
            r.setCode(-1);
            return r;
        }
        try {
            String temp = fileUrl.substring(fileUrl.indexOf("group"), fileUrl.indexOf("?"));
            String group = temp.substring(0, temp.indexOf("/"));
            String path = temp.substring(temp.indexOf("/") + 1);
            fastFileStorageClient.deleteFile(group, path);
            Result r = new Result();
            r.setMsg("文件删除成功");
            r.setSuccess(true);
            r.setCode(1);
            return r;
        } catch (FdfsUnsupportStorePathException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException("删除文件异常，请联系管理员");
        }
    }
}
