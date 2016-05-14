package com.dreamliner.simplifyokhttp.utils;

import com.google.gson.Gson;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * @author chenzj
 * @Title: GsonUtil
 * @Description: 类的描述 - Gson工具类
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class GsonUtil {

    static Gson gson = new Gson();

    public static Object fromJsonToObj(String jsonStr, Type type) {
        Object object = null;
        try {
            return gson.fromJson(jsonStr, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object fromJsonToObj(Reader reader, Type type) {
        Object object = null;
        try {
            return gson.fromJson(reader, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(Reader json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String toJson(Class<T> clazz) {
        try {
            return gson.toJson(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
