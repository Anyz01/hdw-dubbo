package com.hdw.upms.controller;

import com.hdw.common.base.BaseController;
import com.hdw.common.result.ResultMap;
import com.hdw.common.util.DateUtil;
import com.hdw.common.util.QrcodeUtil;
import com.hdw.common.util.URLUtils;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.exception.FdfsUnsupportStorePathException;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.apache.commons.codec.Charsets;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author TuMinglong
 * @Descriptin 文件上传下载
 * @Date 2018年5月8日 上午10:12:45
 */
public abstract class UpLoadController extends BaseController {

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
     * 二维码文件本地路径
     */
    @Value("${file-upload.prefix}")
    private String qrCodeDir;


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
        String header = request.getHeader("SysUser-Agent");
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
     * 单个文件上传
     *
     * @param file 前台传过来文件路径
     * @param dir  保存文件的相对路径,相对路径必须upload开头，例如upload/test
     * @return
     * @throws Exception
     */
    public Map<String, String> upload(MultipartFile file, String dir) {
        Map<String, String> params = new HashedMap();
        String resultPath = "";
        try {
            String savePath = "";
            if (StringUtils.isNotBlank(dir)) {
                savePath = fileUploadPrefix + File.separator + "upload" + File.separator
                        + dir + File.separator + DateUtil.format(new Date(), "yyyyMMdd") + File.separator;
            } else {
                savePath = fileUploadPrefix + File.separator + "upload" + File.separator
                        + DateUtil.format(new Date(), "yyyyMMdd") + File.separator;
            }
            // 保存文件
            String realFileName = file.getOriginalFilename();
            Long fileName = System.currentTimeMillis();
            File targetFile = new File(new File(savePath).getAbsolutePath() + File.separator + fileName + realFileName.substring(realFileName.indexOf(".")));
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);// 复制临时文件到指定目录下
            if (StringUtils.isNotBlank(fileUploadServer)) {
                resultPath = fileUploadServer + "/static/upload/" + dir + "/"
                        + DateUtil.format(new Date(), "yyyyMMdd") + "/"
                        + fileName + realFileName.substring(realFileName.indexOf("."));
            } else {
                resultPath = "/static/upload/" + dir + "/"
                        + DateUtil.format(new Date(), "yyyyMMdd") + "/"
                        + fileName + realFileName.substring(realFileName.indexOf("."));
            }

            params.put("fileName", realFileName);
            params.put("filePath", resultPath);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return params;
    }

    /**
     * 多文件上传
     *
     * @param multipartFiles 文件
     * @param dir            保存文件的文件夹名称,,相对路径必须upload开头，例如upload/test
     * @return
     */
    public List<Map<String, String>> uploads(MultipartFile[] multipartFiles, String dir) {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            // 判断file数组不能为空并且长度大于0
            if (multipartFiles != null && multipartFiles.length > 0) {
                // 循环获取file数组中得文件
                for (MultipartFile file : multipartFiles) {
                    String savePath = "";
                    if (StringUtils.isNotBlank(dir)) {
                        savePath = fileUploadPrefix + File.separator + "upload" + File.separator
                                + dir + File.separator + DateUtil.format(new Date(), "yyyyMMdd") + File.separator;
                    } else {
                        savePath = fileUploadPrefix + File.separator + "upload" + File.separator
                                + DateUtil.format(new Date(), "yyyyMMdd") + File.separator;
                    }
                    // 保存文件
                    String realFileName = file.getOriginalFilename();
                    Long fileName = System.currentTimeMillis();
                    File targetFile = new File(new File(savePath).getAbsolutePath() + File.separator + fileName + realFileName.substring(realFileName.indexOf(".")));
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);// 复制临时文件到指定目录下
                    if (StringUtils.isNotBlank(fileUploadServer)) {
                        String resultPath = fileUploadServer + "/static/upload/" + dir + "/"
                                + DateUtil.format(new Date(), "yyyyMMdd") + "/"
                                + fileName + realFileName.substring(realFileName.indexOf("."));
                        Map<String, String> params = new HashedMap();
                        params.put("fileName", realFileName);
                        params.put("filePath", resultPath);
                        list.add(params);
                    } else {
                        String resultPath = "/static/upload/" + dir + "/"
                                + DateUtil.format(new Date(), "yyyyMMdd") + "/"
                                + fileName + realFileName.substring(realFileName.indexOf("."));
                        Map<String, String> params = new HashedMap();
                        params.put("fileName", realFileName);
                        params.put("filePath", resultPath);
                        list.add(params);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    /**
     * 从本地删除文件
     *
     * @param fileUrl http路径
     * @return
     */
    public Object deleteFileFromLocal(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return ResultMap.error(1, "文件删除失败");
        }
        String temp = fileUrl.substring(fileUrl.indexOf("/static") + 7);
        System.out.println(fileUploadPrefix + File.separator + temp);
        File file = new File(fileUploadPrefix + File.separator + temp);
        if (file.exists() && file.isFile()) {
            file.delete();
            return ResultMap.ok("文件删除成功");
        } else {
            return ResultMap.error(1, "文件删除失败");
        }
    }

    /**
     * 上传文件到FastDFS
     *
     * @param localFilePath
     * @return
     * @throws RuntimeException
     */
    public Map<String, String> uploadToFastDFS(String localFilePath) {
        Map<String, String> params = new HashedMap();
        String path = "";
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
            System.out.println("上传文件路径：" + storePath.getFullPath());
            logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
            path = fdfsfileUploadServer + "/" + storePath.getFullPath();
            params.put("fileName", fileName);
            params.put("filePath", path);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return params;
    }


    /**
     * 上传文件到FastDFS
     *
     * @param file
     * @return
     */
    public Map<String, String> uploadToFastDFS(File file) {
        Map<String, String> params = new HashedMap();
        String path = "";
        try {
            String fileName = file.getName();
            StorePath storePath =
                    fastFileStorageClient.uploadFile(IOUtils.toByteArray(new
                            FileInputStream(file)), FilenameUtils.getExtension(file.getName()));
            System.out.println("上传文件路径：" + storePath.getFullPath());
            logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
            path = fdfsfileUploadServer + "/" + storePath.getFullPath();
            params.put("fileName", fileName);
            params.put("filePath", path);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return params;
    }


    /**
     * 上传文件到FastDFS
     *
     * @param file
     * @return
     */
    public Map<String, String> uploadToFastDFS(MultipartFile file) {
        Map<String, String> params = new HashedMap();
        String path = "";
        try {
            String fileName = file.getOriginalFilename();
            StorePath storePath = fastFileStorageClient.uploadFile(IOUtils.toByteArray(file.getInputStream()),
                    FilenameUtils.getExtension(file.getOriginalFilename()));
            System.out.println("上传文件路径：" + storePath.getFullPath());
            logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
            path = fdfsfileUploadServer + "/" + storePath.getFullPath();
            params.put("fileName", fileName);
            params.put("filePath", path);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return params;
    }

    /**
     * 多文件上传到FastDFS
     *
     * @param fileList
     * @return
     */
    public List<Map<String, String>> uploadsToFastDFS(List<File> fileList) {
        List<Map<String, String>> list = new ArrayList<>();
        if (null != fileList && !fileList.isEmpty()) {
            for (File file : fileList) {
                try {
                    String fileName = file.getName();
                    StorePath storePath = fastFileStorageClient.uploadFile(IOUtils.toByteArray(new FileInputStream(file)), FilenameUtils.getExtension(file.getName()));
                    System.out.println("上传文件路径：" + storePath.getFullPath());
                    logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
                    String path = fdfsfileUploadServer + "/" + storePath.getFullPath();
                    Map<String, String> params = new HashedMap();
                    params.put("fileName", fileName);
                    params.put("filePath", path);
                    list.add(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 多文件上传到FastDFS
     *
     * @param multipartFiles
     * @return
     */
    public List<Map<String, String>> uploadsToFastDFS(MultipartFile[] multipartFiles) {
        List<Map<String, String>> list = new ArrayList<>();
        if (multipartFiles != null && multipartFiles.length > 0) {
            for (MultipartFile file : multipartFiles) {
                try {
                    String fileName = file.getOriginalFilename();
                    StorePath storePath = fastFileStorageClient.uploadFile(IOUtils.toByteArray(file.getInputStream()), FilenameUtils.getExtension(file.getOriginalFilename()));
                    System.out.println("上传文件路径：" + storePath.getFullPath());
                    logger.info("文件分组：" + storePath.getGroup() + "上传文件路径：" + storePath.getFullPath());
                    String path = fdfsfileUploadServer + "/" + storePath.getFullPath();
                    Map<String, String> params = new HashedMap();
                    params.put("fileName", fileName);
                    params.put("filePath", path);
                    list.add(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 从FastDFS上下载文件
     *
     * @param fileUrl  源文件路径
     * @param filePath 保存路径（不需要带文件名）
     * @return
     */
    public Object downloadFileFromFastDFS(String fileUrl, String filePath) {
        try {
            String temp = "";
            if (fileUrl.indexOf("?") > -1) {
                temp = fileUrl.substring(fileUrl.indexOf("group"), fileUrl.indexOf("?"));
            } else {
                temp = fileUrl.substring(fileUrl.indexOf("group"));
            }
            String group = temp.substring(0, temp.indexOf("/"));
            String path = temp.substring(temp.indexOf("/") + 1);
            String fileName = fileUrl.substring(fileUrl.indexOf("=") + 1);
            byte[] bfile = fastFileStorageClient.downloadFile(group, path);
            getFile(bfile, filePath, fileName);
            return ResultMap.ok("文件下载成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error(1, e.getMessage());
        }
    }

    /**
     * 从FastDFS上下载文件
     *
     * @param fileUrl 源文件路径
     * @return
     */
    public byte[] downloadFileFromFastDFS(String fileUrl) {
        byte[] bfile = null;
        try {
            String temp = "";
            if (fileUrl.indexOf("?") > -1) {
                temp = fileUrl.substring(fileUrl.indexOf("group"), fileUrl.indexOf("?"));
            } else {
                temp = fileUrl.substring(fileUrl.indexOf("group"));
            }
            String group = temp.substring(0, temp.indexOf("/"));
            String path = temp.substring(temp.indexOf("/") + 1);
            bfile = fastFileStorageClient.downloadFile(group, path);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return bfile;
    }

    /**
     * 从FastDFS上删除文件
     *
     * @param fileUrl 源文件路径
     */
    public Object deleteFileFromFastDFS(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return ResultMap.error(1, "文件删除失败");
        }
        try {
            String temp = "";
            if (fileUrl.indexOf("?") > -1) {
                temp = fileUrl.substring(fileUrl.indexOf("group"), fileUrl.indexOf("?"));
            } else {
                temp = fileUrl.substring(fileUrl.indexOf("group"));
            }
            String group = temp.substring(0, temp.indexOf("/"));
            String path = temp.substring(temp.indexOf("/") + 1);
            fastFileStorageClient.deleteFile(group, path);
            return ResultMap.ok("文件删除成功");
        } catch (FdfsUnsupportStorePathException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error(1, e.getMessage());
        }
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param filePath
     * @return
     */
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

    /**
     * 创建二维码
     *
     * @param qrResource 内容
     * @return
     */
    public String createQrcode(String qrResource) {
        String pngDir = QrcodeUtil.createQrcode(qrCodeDir + File.separator + "upload" + File.separator + "qr" + File.separator, qrResource);
        String qrDir = "";
        Map<String, String> params = uploadToFastDFS(pngDir);
        if (null != params) {
            qrDir = params.get("filePath");
        }
        return qrDir;
    }

    public String getFileUploadPrefix() {
        return fileUploadPrefix;
    }

    public String getFileUploadServer() {
        return fileUploadServer;
    }

}
