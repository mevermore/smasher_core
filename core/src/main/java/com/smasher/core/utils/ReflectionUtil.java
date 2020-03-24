package com.smasher.core.utils;


import com.smasher.core.log.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by huangzhaoyi on 2017/8/18.
 */

public class ReflectionUtil {

    public static Field getField(Class clazz, String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }

    public static Object getValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }

    public static void setValue(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    public static Method getMethod(Class clazz, String methodName) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    public static void invokeMethod(Object object, Method method, Object... args) {
        try {
            if (method == null) {
                return;
            }
            method.invoke(object, args);
        } catch (Exception e) {
            Logger.exception(e);
        }
    }
}
