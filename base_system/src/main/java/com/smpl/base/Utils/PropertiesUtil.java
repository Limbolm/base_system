package com.smpl.base.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件 解析工具类
 */
public final class PropertiesUtil {

    private Properties properties;
 /*   private URI uri;*/

    /**
     * 构造方法 再新建 类时 同时读取文件
     * @param filePath
     */
    public PropertiesUtil(String filePath) {
        readProperties(filePath);
    }

    /**
     * 无参构造器
     */
    public PropertiesUtil() {
       super();
    }

    /**
     * 解析文件
     * @param filePath
     */
    private void readProperties(String filePath){
        properties=new Properties();
        InputStream ips=getClass().getClassLoader().getResourceAsStream(filePath);
        System.out.printf(this.getClass().getResource("/").toString());
        try {
            properties.load(ips);
          /*  uri=this.getClass().getResource("/"+fileName).toURI();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取相关属性值
     * @param key
     * @return
     */
    public  String getValue (String key){
        return  properties.getProperty(key);
    }

    /**
     * 解析文件 将属性 值 封装 成map
     * @return
     */
    public Map getPropertisOfMap(){
        Map map=new HashMap();

        Enumeration enumeration=properties.propertyNames();
        while (enumeration.hasMoreElements()){
            String key= (String) enumeration.nextElement();
            String value=properties.getProperty(key);
            map.put(key,value);
        }
        return map;
    }


    public String getBaseDataName(){
        this.readProperties("jdbc.properties");
        return getValue("system_database");

    }


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        PropertiesUtil propertiesUtil=new PropertiesUtil("jdbc.properties");
        Map map=propertiesUtil.getPropertisOfMap();
        System.out.println(propertiesUtil.getValue("atm.url"));
    }

}
