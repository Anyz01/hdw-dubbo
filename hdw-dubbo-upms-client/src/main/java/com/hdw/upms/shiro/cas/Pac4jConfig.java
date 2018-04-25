package com.hdw.upms.shiro.cas;


import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @description cas客户端配置
 * @author TuMinglong
 * @date 2018年4月25日上午11:37:29
 * @version v1.0.0
 */
@Configuration
public class Pac4jConfig {
	
	@Value("${sso.cas.server.loginUrl}")
	private String loginUrl;

	@Value("${sso.cas.server.prefixUrl}")
	private String prefixUrl;

	@Value("${sso.cas.client.callbackUrl}")
	private String callbackUrl;

	@Value("${sso.cas.serviceUrl}")
	private String serviceUrl;
	
	@Autowired
	private JwtAuthenticator jwtAuthenticator;
	
	/**
	 * cas服务端配置
	 * 
	 * @return
	 */
	@Bean
	public CasConfiguration casConfiguration() {
		CasConfiguration casConfiguration = new CasConfiguration(loginUrl);
		casConfiguration.setProtocol(CasProtocol.CAS30);
		casConfiguration.setPrefixUrl(prefixUrl);
		return casConfiguration;
	}

	/**
	 * cas客户端配置
	 * 
	 * @return
	 */
	@Bean
	public CasClient casClient() {
		CasClient casClient = new CasClient();
		casClient.setConfiguration(casConfiguration());
		casClient.setCallbackUrl(callbackUrl);
		casClient.setName("cas");
		return casClient;
	}

	/**
	 * 通过rest接口可以获取tgt,获取service ticket,甚至可以获取casProfile
	 * 
	 * @return
	 */
	@Bean
	public CasRestFormClient casRestFormClient() {
		CasRestFormClient casRestFormClient = new CasRestFormClient();
		casRestFormClient.setConfiguration(casConfiguration());
		casRestFormClient.setName("rest");
		return casRestFormClient;

	}

	@Bean
	public Clients clients() {
		// 设置默认client
		Clients clients = new Clients();
		// token校验器，可以用HeaderClient更安全
		ParameterClient parameterClient = new ParameterClient("token", jwtAuthenticator);
		parameterClient.setSupportGetRequest(true);
		parameterClient.setName("jwt");
		// 支持的client全部设置进去
		clients.setClients(casClient(), casRestFormClient(), parameterClient);
		return clients;
	}

	@Bean(name="casConfig")
	public Config casConfig() {
		Config config = new Config();
		config.setClients(clients());
		return config;
	}
	
}
