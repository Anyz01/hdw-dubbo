package com.hdw.dubbo.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @description jackson工具类
 * @author TuMinglong
 * @date 2016-11-29
 * @version v1.0
 *
 */

public class JacksonUtils {
	private static final ObjectMapper objectMapper;
	static{
		objectMapper=new ObjectMapper();
		//去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		//设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		//空值不序列化
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		//反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//序列化时，日期统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}
	
	/**
	 * 将对象序列化json字符串
	 * @param t 对象可以是 String Map、List
	 * @return json字符串
	 */
	public static <T> String toJson(T t){
		try {
			return objectMapper.writeValueAsString(t);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 字符串转Java对象
	 * JsonNode可适用本方法
	 * @param json
	 * @param clazz
	 * @return t
	 */
	public static <T> T toObject(String json,Class<T> clazz){
		try {
			return objectMapper.readValue(json, clazz);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
	/**
	 * json序列化
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T toCollection(String json,TypeReference<T> typeReference){
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	
	}
	
	/**
	 * 获取jsonNode对象
	 * @param json
	 * @return
	 */
	public static JsonNode getJsonNode(String json){
		try {
			return objectMapper.readValue(json, JsonNode.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 
    
   
}
