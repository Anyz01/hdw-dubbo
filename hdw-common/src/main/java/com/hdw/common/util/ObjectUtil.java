package com.hdw.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
     * Map 转换 java bin
     *
     * @param type
     * @param map
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public static Object convertMap(Class type, Map map) throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }
}
