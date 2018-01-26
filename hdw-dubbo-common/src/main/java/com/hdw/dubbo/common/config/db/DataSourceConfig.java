package com.hdw.dubbo.common.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.hdw.dubbo.common.util.security.SecurityUtil;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 多数据源配置
 * @author TuMinglong
 * @date 2018年1月24日 下午4:07:51
 */
@Configuration
@MapperScan("com.hdw.*.mapper")
public class DataSourceConfig {
	protected final static Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Value("${spring.datasource.master.url}")
	private String masterUrl;

	@Value("${spring.datasource.master.username}")
	private String masterUsername;

	@Value("${spring.datasource.master.password}")
	private String masterPassword;

	@Value("${spring.datasource.master.driver-class-name}")
	private String masterDriverClassName;

	@Value("${spring.datasource.master.validationQuery}")
	private String masterValidationQuery;

	@Value("${spring.datasource.slave.url}")
	private String slaveUrl;

	@Value("${spring.datasource.slave.username}")
	private String slaveUsername;

	@Value("${spring.datasource.slave.password}")
	private String slavePassword;

	@Value("${spring.datasource.slave.driver-class-name}")
	private String slaveDriverClassName;

	@Value("${spring.datasource.slave.validationQuery}")
	private String slaveValidationQuery;

	@Value("${spring.datasource.filters}")
	private String filters;

	@Value("${spring.datasource.initialSize}")
	private int initialSize;

	@Value("${spring.datasource.minIdle}")
	private int minIdle;

	@Value("${spring.datasource.maxActive}")
	private int maxActive;

	@Value("${spring.datasource.maxWait}")
	private int maxWait;

	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;

	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${spring.datasource.maxOpenPreparedStatements}")
	private int maxOpenPreparedStatements;

	@Value("{spring.datasource.connectionProperties}")
	private String connectionProperties;

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("loginUsername", "druid");
		reg.addInitParameter("loginPassword", "druid");
		return reg;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
		filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
		return filterRegistrationBean;
	}

	/**
	 * 主数据源
	 */
	@Qualifier("masterDataSource")
	@Bean(name = "masterDataSource")
	public DruidDataSource masterDataSource() throws SQLException {

		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(masterUrl);
		datasource.setUsername(masterUsername);
		datasource.setPassword(SecurityUtil.decryptAes(masterPassword));
		datasource.setDriverClassName(masterDriverClassName);

		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(masterValidationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxOpenPreparedStatements);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}

		return datasource;
	}

	/**
	 * 从数据源
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Qualifier("slaveDataSource")
	@Bean(name = "slaveDataSource")
	public DruidDataSource slaveDataSource() throws SQLException {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(slaveUrl);
		datasource.setUsername(slaveUsername);
		datasource.setPassword(SecurityUtil.decryptAes(slavePassword));
		datasource.setDriverClassName(slaveDriverClassName);

		datasource.setInitialSize(initialSize);
		datasource.setMinIdle(minIdle);
		datasource.setMaxActive(maxActive);
		datasource.setMaxWait(maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(slaveValidationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(maxOpenPreparedStatements);
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}

		return datasource;
	}

	@DependsOn(value = { "masterDataSource", "slaveDataSource" })
	@Bean(name = "datasource")
	@Qualifier("datasource")
	@Primary
	public DynamicDataSource dynamicDataSource(@Qualifier(value = "masterDataSource") DataSource masterDataSource,
			@Qualifier(value = "slaveDataSource") DataSource slaveDataSource) {
		DynamicDataSource bean = new DynamicDataSource();
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		targetDataSources.put("masterDataSource", masterDataSource);
		targetDataSources.put("slaveDataSource", slaveDataSource);
		bean.setTargetDataSources(targetDataSources);
		bean.setDefaultTargetDataSource(masterDataSource);
		return bean;
	}

	@Bean(name = "sessionFactory")
	@ConfigurationProperties(prefix = "mybatis-plus")
	@Primary
	public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier(value = "datasource") DataSource dataSource) {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean;
	}

}
