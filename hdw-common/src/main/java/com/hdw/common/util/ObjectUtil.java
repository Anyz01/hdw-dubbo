package com.hdw.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 对象工具类
 * @Author TuMinglong
 * @Date 2018/5/30 11:27
 */
public class ObjectUtil {

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toLowerCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取属性名数组
     *
     * @param o
     * @return
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型（type）,属性名（name）,属性值（value）的map组成的list
     *
     * @param o
     * @return
     */
    public static List<Map<String, Object>> getFieldsInfo(Object o) {
        Field[] fields = o.getClass().getFields();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> par = null;
        for (int i = 0; i < fields.length; i++) {
            par = new HashMap<>();
            par.put("type", fields[i].getType().toString());
            par.put("name", fields[i].getName());
            par.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(par);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     *
     * @param o
     * @return
     */
    public static Object[] getFiledValues(Object o) {
        String[] fieldNames = getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

    /**
     * Map转Object
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass){
        try {
            if (map == null)
                return null;
            Object obj = beanClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    setter.invoke(obj, map.get(property.getName()));
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * Object转Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj){
        try {
            if (obj == null)
                return null;

            Map<String, Object> map = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * Map转Object 通过反射
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObjectByReflect(Map<String, Object> map, Class<?> beanClass) {
        try {
            if (map == null)
                return null;
            Object obj = beanClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Object转Map 通过反射
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMapByReflect(Object obj){
        try {
            if (obj == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
