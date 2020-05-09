package com.hz.fine.master.core.common.utils;

import java.lang.reflect.Field;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/12/5 14:11
 * @Version 1.0
 */
public class ClassUtil {
    public String key;
    public String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void  main(String  []  args  ){
        Class clz = ClassUtil.class;
        Field[] fields = clz.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
    }

}
