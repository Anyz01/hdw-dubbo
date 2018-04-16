/*
 * 版权所有.(c)2008-2017. 卡尔科技工作室
 */

package com.hdw.dubbo.upms.shiro;

import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;


/**
 * 
 * @description cas 密码加密算法
 * @author TuMinglong
 * @date 2018年4月16日 下午4:02:53
 * @version 1.0.0
 */
public class PasswordSalt {
    private static String staticSalt = ".";
   
   
    /**
     * 加盐算法
     * @param algorithmName 算法
     * @param source 源对象
     * @param salt 加密盐
     * @param hashIterations hash次数(默认为2)
     * @param staticSalt
     * @return
     */
    public static String hashByShiro(String algorithmName, Object source, Object salt, int hashIterations) {
    	 ConfigurableHashService hashService = new DefaultHashService();
         hashService.setPrivateSalt(ByteSource.Util.bytes(staticSalt));
         hashService.setHashAlgorithmName(algorithmName);
         hashService.setHashIterations(hashIterations);
         HashRequest request = new HashRequest.Builder().setSalt(salt).setSource(source).build();
         String res =  hashService.computeHash(request).toHex();
         return res;
    }
    
    public static void main(String[] args) {
		System.out.println(hashByShiro("md5", "123456", "admin", 2));
	}
}
