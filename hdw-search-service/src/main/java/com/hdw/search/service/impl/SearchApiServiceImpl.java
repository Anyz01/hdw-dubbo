package com.hdw.search.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


import com.hdw.search.entity.PushDataIntoSolrRequest;
import com.alibaba.dubbo.config.annotation.Service;
import com.hdw.search.api.ISearchApiService;

/**
 * 
 * @description SearchApi实现层
 * @author TuMinglong
 * @date 2018年3月8日 上午11:01:57
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class SearchApiServiceImpl implements ISearchApiService {

	private static final Logger logger = LoggerFactory.getLogger(SearchApiServiceImpl.class);

	@Value("${spring.data.solr.host}")
	private String solr_host;

	private  SolrClient solr;

	public SolrClient getSolrClient(String coreName) {
		HttpSolrClient solrClient = new HttpSolrClient.Builder(solr_host + coreName).build();
		solrClient.setSoTimeout(5000);
		solrClient.setConnectionTimeout(1000);
		solrClient.setDefaultMaxConnectionsPerHost(1000);
		solrClient.setMaxTotalConnections(5000);
		return solrClient;
	}
	
	@Override
	public <T> boolean insertOfSolr(String coreName, T t) {
		boolean flag = false;
		try {
			solr = getSolrClient(coreName);
			solr.add(Obj2Doc(t));
			solr.commit();
			flag = true;
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return flag;
	}
	
	@Override
	public boolean insertOfSolr(PushDataIntoSolrRequest request) {
		
        boolean flag = false;
		try {
			SolrInputDocument doc = new SolrInputDocument();  
	        for (Map.Entry<String, Object> entry : request.getDoc().entrySet()) {  
	            doc.addField(entry.getKey(), entry.getValue());  
	        } 
	        
			solr = getSolrClient(request.getCoreName());
			solr.add(doc);
			solr.commit();
			flag = true;
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return flag;
	}

	@Override
	public <T> boolean insertBatchOfSolr(String coreName, List<T> t) {
		boolean flag = false;
		try {
			solr = getSolrClient(coreName);
			solr.add(turnDoc(t));
			solr.commit();
			flag = true;
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return flag;
	}
	
	@Override
	public boolean insertBatchOfSolr(PushDataIntoSolrRequest request) {
		 boolean flag = false;
			try {
				List<SolrInputDocument> docs=new ArrayList<>();
				for(Map<String,Object> par:request.getList()) {
					SolrInputDocument doc = new SolrInputDocument();  
			        for (Map.Entry<String, Object> entry : par.entrySet()) {  
			            doc.addField(entry.getKey(), entry.getValue());  
			        } 
			        docs.add(doc);
				}
				solr = getSolrClient(request.getCoreName());
				solr.add(docs);
				solr.commit();
				flag = true;
			} catch (SolrServerException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} finally {
				try {
					solr.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
			return flag;
	}

	@Override
	public <T> boolean updateOfSolr(String coreName, T t) {
		boolean flag = false;
		try {
			solr = getSolrClient(coreName);
			solr.add(Obj2Doc(t));
			solr.commit();
			flag = true;
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return flag;
	}

	@Override
	public boolean deleteByIdOfSolr(String coreName, String id) {
		boolean flag = false;
		try {
			solr = getSolrClient(coreName);
			solr.deleteById(id);
			solr.commit();
			flag = true;
		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return flag;

	}

	@Override
	public QueryResponse queryOfSolr(String coreName, SolrQuery solrQuery) {
		QueryResponse rsp = null;
		try {
			solr = getSolrClient(coreName);
			rsp = solr.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return rsp;
	}

	@Override
	public boolean DeleteAllOfSolr(String coreName) {
		// 清空所有数据
		boolean flag = false;
		try {
			solr = getSolrClient(coreName);
			solr.deleteByQuery("*:*");
			solr.commit();
			flag = true;
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		return flag;
	}
	
	
	public  SolrInputDocument Obj2Doc(Object obj) {
    	//创建 SolrInputDocument对象  
        SolrInputDocument document = new SolrInputDocument();
         Field[] fields = obj.getClass().getDeclaredFields();
    	 for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			String name = field.getName();
			Object val = new Object();		
			try {
				val = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(!name.equals("serialVersionUID")) {
				document.addField(name, val);
			}		
    	 }
		return document;
    	 
     }
     
     public  <T> List<SolrInputDocument> turnDoc(List<T> objs){
    	 List<SolrInputDocument> documents =new ArrayList<>();
    	 int size = objs.size();
  		 for (int i = 0; i < size; i++) {
  			documents.add(Obj2Doc(objs.get(i)));
  		 }
  		 return documents;
     }

	
}
