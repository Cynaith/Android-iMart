package com.ly.imart.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GsonUtil {

    /**
     *解析字符串字典
     *
     * @author 人民重重
     * @time 2019/1/31 上午9:04
     */
    public static <T> T parseJsonWithGson(String json, Class<T> type) {

        T result  = new Gson().fromJson(json, type);
        return result;
    }

    /**
     * 解析数组
     * @author 人民重重
     * @time 2019/1/31 上午10:12
     */
    public static <T> List<T> parseArrayJsonWithGson(String json, Class<T> type) {
        List<T> list = new Gson().fromJson(json, new TypeToken<List<T>>(){}.getType());
        return list;
    }
}