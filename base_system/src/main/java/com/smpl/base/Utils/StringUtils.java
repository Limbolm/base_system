package com.smpl.base.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * String 工具类
 */
public final class StringUtils {


    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (str==null){
            return true;
        }else{
           return  "".equals(str)?true:false;
        }
    }


    /**
     * 根据特定 标识 截取字符串
     * @param sourceStr 原有字符串
     * @param expr 特定标识
     * @return 返回String 数组
     */

    public static String[] stringSplit(String sourceStr,String expr){
        if (!sourceStr.isEmpty()){
            return sourceStr.split(expr);
        }
        return null;
    }

    /**
     * 根据特定 标识 截取字符串
     * @param sourceStr 原有字符串
     * @param expr 特定标识
     * @return 返回List
     */
    public static List<String> stringSplitL(String sourceStr,String expr){
        if (!sourceStr.isEmpty()){
            String[] strings= sourceStr.split(expr);
            return Arrays.asList(strings);
        }
        return null;
    }



}
