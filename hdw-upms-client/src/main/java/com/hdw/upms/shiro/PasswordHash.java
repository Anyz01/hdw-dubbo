package com.hdw.upms.shiro;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;



/**
 * 
 * @description shiro密码加密配置
 * @author TuMinglong
 * @date 2018年4月16日 下午4:34:30
 * @version 1.0.0
 */
public class PasswordHash implements InitializingBean {
	private String algorithmName;
	private int hashIterations;

	public String getAlgorithmName() {
		return algorithmName;
	}
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	public int getHashIterations() {
		return hashIterations;
	}
	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(algorithmName, "algorithmName mast be MD5、SHA-1、SHA-256、SHA-384、SHA-512");
	}
	
	public String toHex(Object source, Object salt) {
		return DigestUtils.hashByShiro(algorithmName, source, salt, hashIterations);
	}
	
	public String toHexByCas(Object source, Object salt) {
		return PasswordSalt.hashByShiro(algorithmName, source, salt, hashIterations);
	}
}
