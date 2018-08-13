package com.hdw.upms.controller;

import com.google.common.base.Charsets;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.Result;
import com.hdw.common.util.DateUtil;
import com.hdw.common.util.URLUtils;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.exception.FdfsUnsupportStorePathException;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.*;
import java.util.*;

/**
 * @author TuMinglong
 * @Descriptin 文件上传下载
 * @Date 2018年5月8日 上午10:12:45
 */
public abstract class UpLoadController extends BaseController {

    /**
     * 文件上传路径前缀
     */
    @Value("${hdw.file-upload.prefix}")
    private String fileUploadPrefix;

    /**
     * 文件上传服务器名称
     */
    @Value("${hdw.file-upload.server}")
    private String fileUploadServer;

    /**
     * FastDFS文件上传服务器名称
     */
    @Value("${fdfs.file-upload.server}")
    private String fdfsfileUploadServer;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 下载文件
     *
     * @param file 文件
     */
    protected ResponseEntity<Resource> download(File file) {
        String fileName = file.getName();
        return download(file, fileName);
    }

    /**
     * 下载
     *
     * @param file     文件
     * @param fileName 生成的文件名
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
     * @param files 文件
     * @param dir   保存文件的文件夹名称
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
            } else {
                dirFile = new File(fileUploadPrefix + File.separator +"upload"+ File.separator
                        + DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
            }
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            // 判断file数组不能为空并且长度大于0
            if (files != null && files.length > 0) {
                // 循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    // 保存文件
                    String realFileName = file.getOriginalFilename();
                    Long fileName = System.currentTimeMillis();
                    File targetFile = new File(dirFile.getAbsoluteFile() + File.separator + fileName + realFileName.substring(realFileName.indexOf(".")));
                    if (!targetFile.exists()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
                        targetFile.createNewFile();
                    }
                    file.transferTo(targetFile);

                    if (StringUtils.isNotBlank(fileUploadServer)) {
                        fileNames.add(fileUploadServer + "/" + "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd")
                                + "/" + fileName + realFileName.substring(realFileName.indexOf(".")) + "?attname=" + realFileName);
                    } else {
                        fileNames.add("/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + fileName + realFileName.substring(realFileName.indexOf(".")));
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
     * @param file 前台传过来文件路径
     * @param dir  保存文件的相对路径
     * @return
     * @throws Exception
     */
    public String upload(MultipartFile file, String dir) throws RuntimeException {
        try {
            File dirFile = null;
            if (StringUtils.isNotBlank(dir)) {
                dirFile = new File(fileUploadPrefix + File.separator + dir + File.separator
                        + DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
            } else {
                dirFile = new File(fileUploadPrefix + File.separator +"upload"+ File.separator
                        + DateUtil.format(new Date(), "yyyyMMdd") + File.separator);
            }
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            String realFileName = file.getOriginalFilename();
            long fileName = System.currentTimeMillis();
            File targetFile = new File(dirFile.getAbsoluteFile() + File.separator + fileName + realFileName.substring(realFileName.indexOf(".")));
            if (!targetFile.exists()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
                targetFile.createNewFile();
            }
            file.transferTo(targetFile);
            if (StringUtils.isNotBlank(fileUploadServer)) {
                return fileUploadServer + "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/"
                        + fileName + realFileName.substring(realFileName.indexOf(".")) + "?attname=" + realFileName;
            } else {
                return "/" + dir + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + fileName + realFileName.substring(realFileName.indexOf("."));
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("保存文件异常，请联系管理员");
        }
    }

    /**
     * 上传文件到FastDFS
     *
     * @param file
     * @return
     * @throws RuntimeException
     */
    public Map<String, Object> uploadToFastDFS(MultipartFile file) throws RuntimeException {
        try {
            String fileName = file.getOriginalFilename();
            StorePath storePath = fastFileStorageClient.uploadFile(IOUtils.toByteArray(file.getInputStream()),
                    FilenameUtils.getExtension(file.getOriginalFilename()));
            // StorePath storePath =
            // fastFileStorageClient.uploadFile(IOUtils.toByteArray(new
            // FileInputStream(file)),FilenameUtils.getExtension(file.getName()));
            System.out.println("上传文件路径：" + storePath.getFullPath());
            logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
            String path = fdfsfileUploadServer + "/" + storePath.getFullPath() + "?attname=" + fileName;
            Map<String, Object> par = new HashMap<>();
            par.put(fileName, path);
            return par;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException("保存文件异常，请联系管理员");
        }
    }

    /**
     * 上传文件到FastDFS
     *
     * @param localFilePath
     * @return
     * @throws RuntimeException
     */
    public String uploadToFastDFS(String localFilePath) throws RuntimeException {
        try {
            byte[] bytes = getBytes(localFilePath);
            String fileName = "";
            if ((localFilePath != null) && (localFilePath.length() > 0)) {
                int dot = localFilePath.lastIndexOf(File.separator);
                if ((dot > -1) && (dot < (localFilePath.length() - 1))) {
                    fileName = localFilePath.substring(dot + 1, localFilePath.length());
                }
            }
            StorePath storePath = fastFileStorageClient.uploadFile(bytes, FilenameUtils.getExtension(fileName));
            // StorePath storePath =
            // fastFileStorageClient.uploadFile(IOUtils.toByteArray(new
            // FileInputStream(file)),FilenameUtils.getExtension(file.getName()));
            System.out.println("上传文件路径：" + storePath.getFullPath());
            logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
            String path = fdfsfileUploadServer + "/" + storePath.getFullPath() + "?attname=" + fileName;
            //Map<String, Object> par = new HashMap<>();
            //par.put(fileName, path);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException("保存文件异常，请联系管理员");
        }
    }

    /**
     * 多文件上传到FastDFS
     *
     * @param files
     * @param dir
     * @return
     * @throws RuntimeException
     */
    public Map<String, Object> uploadsToFastDFS(MultipartFile[] files, String dir) throws RuntimeException {
        Map<String, Object> par = new HashMap<>();
        try {
            // 判断file数组不能为空并且长度大于0
            if (files != null && files.length > 0) {
                // 循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    String fileName = file.getOriginalFilename();
                    StorePath storePath = fastFileStorageClient.uploadFile(IOUtils.toByteArray(file.getInputStream()),
                            FilenameUtils.getExtension(file.getOriginalFilename()));
                    // StorePath storePath =
                    // fastFileStorageClient.uploadFile(IOUtils.toByteArray(new
                    // FileInputStream(file)),FilenameUtils.getExtension(file.getName()));
                    System.out.println("上传文件路径：" + storePath.getFullPath());
                    logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
                    String path = fdfsfileUploadServer + "/" + storePath.getFullPath() + "?attname=" + fileName;
                    par.put(fileName, path);
                }
            }
            return par;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException("保存文件异常，请联系管理员");
        }
    }

    /**
     * 从FastDFS上下载文件
     *
     * @param fileUrl  源文件路径
     * @param filePath 保存路径（不需要带文件名）
     * @return
     */
    public Result downloadFileFromFastDFS(String fileUrl, String filePath) throws RuntimeException {
        try {
            String temp = fileUrl.substring(fileUrl.indexOf("group"), fileUrl.indexOf("?"));
            String group = temp.substring(0, temp.indexOf("/"));
            String path = temp.substring(temp.indexOf("/") + 1);
            String fileName = fileUrl.substring(fileUrl.indexOf("=") + 1);
            byte[] bfile = fastFileStorageClient.downloadFile(group, path);
            getFile(bfile, filePath, fileName);
            Result r = new Result();
            r.setMsg("文件下载成功");
            r.setSuccess(true);
            r.setCode(1);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException("保存文件异常，请联系管理员");
        }

    }

    /**
     * 从FastDFS上删除文件
     *
     * @param fileUrl 源文件路径
     */
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

    /**
     * 获得指定文件的byte数组
     *
     * @param filePath
     * @return
     */
    @SuppressWarnings("unused")
    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     *
     * @param bfile
     * @param filePath
     * @param fileName
     */
    private void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
