package com.hdw.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @description 文件压缩工具类
 * @author TuMinglong
 * @date 2017年9月29日 下午4:03:42
 *
 */
public class ZipUtils {
   
	/**
	 * 功能：单文件或目录压缩
	 * @param srcFile 待压缩的文件或目录的路径
	 * @param zipFile 压缩后文件路径
	 * @return true false
	 */
    @SuppressWarnings("static-access")
	public static boolean zipFile(String srcFile, String zipFile){
    	try {
    		 File file = new File (srcFile) ;
	       	 ZipOutputStream zos = new ZipOutputStream (new FileOutputStream (zipFile)) ;
	       	 if (file.isDirectory ()) {
	       		File [] files = file.listFiles() ;
	       		for ( int i = 0 ; i < files.length ; i ++ ){
	       			BufferedInputStream bis = new BufferedInputStream (new FileInputStream (files[i])) ;
	       			zos.putNextEntry (new ZipEntry(file.getName() + file.separator+ files [i].getName())) ;
	       			byte[] buf = new byte[1024];
	           		int len;
	                   while ((len = bis.read(buf)) > 0) { 
	                       zos.write(buf, 0, len);  
	                   }  
	                   bis.close ( ) ;
	       		} 
	       	}else {    
	       		FileInputStream in = new FileInputStream(srcFile);  
	       		zos.putNextEntry(new ZipEntry(file.getName()));  
	       		byte[] buf = new byte[1024];
	       		int len;
	               while ((len = in.read(buf)) > 0) { 
	                   zos.write(buf, 0, len);  
	               }  
	               zos.closeEntry();  
	               in.close();  
	       	}
	       	zos.close ();
	        System.out.println("文件压缩完成");
            return true;
    	}catch (Exception e) {
			System.out.println("文件压缩出错");
			return false;
		} 
   }
    
    
    /** 
     * 功能:压缩多个文件成一个zip文件 
     * @param srcfile：源文件列表 
     * @param zipfile：压缩后的文件
     * @return true false 
     */  
    public static boolean zipFiles(File[] srcfile, File zipfile) {  
        byte[] buf = new byte[1024];  
        try {  
            //ZipOutputStream类：完成文件或文件夹的压缩  
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));  
            for (int i = 0; i < srcfile.length; i++) {  
                FileInputStream in = new FileInputStream(srcfile[i]);  
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));  
                int len;  
                while ((len = in.read(buf)) > 0) {  
                    out.write(buf, 0, len);  
                }  
                out.closeEntry();  
                in.close();  
            }  
            out.close();  
            System.out.println("文件压缩完成");
            return true;
        } catch (Exception e) { 
        	System.out.println("文件压缩出错");
			return false; 
        }  
    }  
  
    /** 
     * 功能:解压缩 
     * @param zipfile：需要解压缩的文件 
     * @param descDir：解压后的目标目录 
     */  
    @SuppressWarnings({ "rawtypes", "resource" })
	public static boolean unZipFiles(File zipfile, String descDir) {  
        try {  
            ZipFile zf = new ZipFile(zipfile);  
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {  
                ZipEntry entry = (ZipEntry) entries.nextElement();  
                String zipEntryName = entry.getName();  
                InputStream in = zf.getInputStream(entry);  
                OutputStream out = new FileOutputStream(descDir + zipEntryName);  
                byte[] buf= new byte[1024];  
                int len;  
                while ((len = in.read(buf)) > 0) {  
                    out.write(buf, 0, len);  
                }  
                in.close();  
                out.close();  
            }
            System.out.println("文件解压缩完成");  
            return true;
        } catch (Exception e) {  
        	System.out.println("文件解压缩出错");
			return false; 
        }
    }  

       
    /** 
    * 功能：复制单个文件
    * @param oldPath String 原文件路径 如：c:/fqf.txt 
    * @param newPath String 复制后路径 如：f:/fqf.txt 
    * @return boolean 
    */ 
    @SuppressWarnings("resource")
	public static void copyFile(String oldPath, String newPath) { 
	    try { 
		    int bytesum = 0; 
		    int byteread = 0; 
		    File oldfile = new File(oldPath); 
		    if (oldfile.exists()) { //文件存在时 
			    InputStream inStream = new FileInputStream(oldPath); //读入原文件 
			    FileOutputStream fs = new FileOutputStream(newPath); 
			    byte[] buffer = new byte[1444]; 
			    while ( (byteread = inStream.read(buffer)) != -1) { 
				    bytesum += byteread; //字节数 文件大小 
				    System.out.println(bytesum); 
				    fs.write(buffer, 0, byteread); 
			    } 
			    inStream.close(); 
		    } 
	    } catch (Exception e) { 
		    System.out.println("复制单个文件操作出错"); 
		    e.printStackTrace(); 
	    } 

    } 

    /** 
    * 功能：复制整个文件夹内容
    * @param oldPath String 原文件路径 如：c:/fqf 
    * @param newPath String 复制后路径 如：f:/fqf/ff 
    * @return boolean 
    */ 
    public static void copyFolder(String oldPath, String newPath) { 

	    try { 
		    (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
		    File a=new File(oldPath); 
		    String[] file=a.list(); 
		    File temp=null; 
		    for (int i = 0; i < file.length; i++) { 
			    if(oldPath.endsWith(File.separator)){ 
			    	temp=new File(oldPath+file[i]); 
			    }else{ 
			    	temp=new File(oldPath+File.separator+file[i]); 
			    } 
		
			    if(temp.isFile()){ 
				    FileInputStream input = new FileInputStream(temp); 
				    FileOutputStream output = new FileOutputStream(newPath + "/" + 
				    (temp.getName()).toString()); 
				    byte[] b = new byte[1024 * 5]; 
				    int len; 
				    while ( (len = input.read(b)) != -1) { 
				    	output.write(b, 0, len); 
				    } 
				    output.flush(); 
				    output.close(); 
				    input.close(); 
			    } 
			    if(temp.isDirectory()){//如果是子文件夹 
			    	copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
			    } 
		    } 
	    } catch (Exception e) { 
		    System.out.println("复制整个文件夹内容操作出错"); 
		    e.printStackTrace(); 
	
	    } 

    }
    
    
    /**
     * 功能：删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 功能：删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 功能：删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag =deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag =deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    
    
    
    public static void main(String[] args) throws IOException {
    	
    	zipFile("E:\\RiskImages\\emergencyChemistry\\20170928\\危险化学品安全生产“十三五”规划.docx", "F:/java.zip");
    }
    

}
