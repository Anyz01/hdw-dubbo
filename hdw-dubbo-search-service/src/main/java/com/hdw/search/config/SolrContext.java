package com.hdw.search.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages={"com.hdw.dubbo.search"}, multicoreSupport=true)
public class SolrContext {
	
	@Value("${spring.data.solr.host}")
	private String SOLR_HOST;
	@Bean
	 public SolrClient solrClient() {
	    return new HttpSolrClient.Builder(SOLR_HOST).build();
	  }
	
}
