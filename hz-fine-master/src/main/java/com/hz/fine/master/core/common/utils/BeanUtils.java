package com.hz.fine.master.core.common.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * bean 工具类
 */
public class BeanUtils {

    private static ConcurrentMap<Class, ConcurrentMap<Class, BeanCopier>> beanCopierMap = new ConcurrentHashMap<Class, ConcurrentMap<Class, BeanCopier>>();

    /**
     * 复制属性, 会自动缓存以加快速度,建议使用传入Class的方法
     *
     * @param src
     * @param dest
     * @return 复制的目标对象,注入如果src为null,则这里会返回null
     */
    public static Object copy(Object src, Object dest) {
        if (src == null) {
            return null;
        }
        if (dest == null) {
            throw new NullPointerException("dest is null");
        }
        ConcurrentMap<Class, BeanCopier> innerMap = beanCopierMap.get(src.getClass());
        if (innerMap == null) {
            innerMap = new ConcurrentHashMap();
            ConcurrentMap<Class, BeanCopier> temp = beanCopierMap.putIfAbsent(src.getClass(), innerMap);
            if (temp != null) {
                innerMap = temp;
            }
        }
        BeanCopier beanCopier = innerMap.get(dest.getClass());
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(src.getClass(), dest.getClass(), false);
            BeanCopier temp = innerMap.putIfAbsent(dest.getClass(), beanCopier);
            if (temp != null) {
                beanCopier = temp;
            }
        }
        beanCopier.copy(src, dest, null);
        return dest;
    }

    /**
     * 复制属性, 会自动缓存以加快速度
     *
     * @param src
     * @param destClass 目标类,要求该类必须有无参构造函数
     * @param <T>
     * @return
     */
    public static <T> T copy(Object src, Class<T> destClass) {
        if (src == null) {
            return null;
        }
        try {
            T dest = destClass.newInstance();
            copy(src, dest);
            return dest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把list中的每个对象都转换为目标类的对象
     *
     * @param srcList
     * @param destClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> srcList, Class<T> destClass) {
        if (srcList == null) {
            return Collections.emptyList();
        }
        List<T> retList = new ArrayList<T>();
        for (Object src : srcList) {
            T destInstance = copy(src, destClass);
            retList.add(destInstance);
        }
        return retList;
    }

    public static Map<String, Object> transBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }


    /**
     * 方法说明：map转化为对象
     *
     * @param map
     * @param t
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> t) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T instance = t.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(instance, map);
        return instance;
    }
}
