package com.hdw.solr.config;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description Solr对象转换工具类
 * @Date 2018/8/29 16:00
 * @Author TuMinglong
 */
public class SolrUtil {
    /**
     * 转Object
     *
     * @param record
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(SolrDocument record, Class<T> clazz) {
        T t = null;
        t = (T) clazz.getInterfaces();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            Object value = record.get(field.getName());
            try {
                BeanUtils.setProperty(t, field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 转List
     *
     * @param records
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toBeanList(SolrDocumentList records, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (SolrDocument record : records) {
            list.add(toBean(record, clazz));
        }
        return list;
    }
}
