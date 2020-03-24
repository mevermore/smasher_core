package com.smasher.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author moyu
 * @date 2017/3/17
 */
public class UrlUtil {

    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @return url请求参数部分
     */
    public static Map<String, String> getQueryString(String query) {
        Map<String, String> mapRequest = new HashMap<>();
        String[] arrSplit = null;
        if (query == null) {
            return mapRequest;
        }
        // 每个键值为一组
        arrSplit = query.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (!"".equals(arrSplitEqual[0])) {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }
}
