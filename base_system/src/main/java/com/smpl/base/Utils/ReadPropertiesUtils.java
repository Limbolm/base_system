package com.smpl.base.Utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public  class ReadPropertiesUtils {
	/**
	 * 遍历返回properties里的全部秘钥
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getProperties(){
	Properties props = new Properties();
	Map<String,String> map = new HashMap<String,String>();
	try{
		InputStream in= ReadPropertiesUtils.class.getResourceAsStream("/properties/corpsecret.properties");
        props.load(in);
        Enumeration en=props.propertyNames();
        while (en.hasMoreElements()) {
            String key=(String) en.nextElement();
            String property=props.getProperty(key);
            map.put(key, property);
        }
	}catch(Exception e){
		e.printStackTrace();
	}
	return map;
}


	/**
	 * 通过appName判断是否有该应用秘钥
	 * @param appName
	 * @return
	 */
	public static boolean iscontain(String appName){
		boolean flag = false;		
		Map<String,String> map = getProperties();
		if(map.containsKey(appName+"_corpsecret")){
			return true;
		}
		return flag;
	}

	/**
	 * 通过appName获取秘钥
	 * @param appName
	 * @return
	 */
	public static String getSecret(String appName){
		String secret = "";
		Map<String,String> map = getProperties();
		Object obj = new Object();
		obj = map.get(appName+"_corpsecret");
		secret = obj.toString();
		return secret;
	}
	
	public static void main(String[] args){
		//ReadPropertiesUtils r = new ReadPropertiesUtils();
		
		Map<String,String> map = getProperties();
		
		Iterator<String> iterator=map.keySet().iterator();  
        while(iterator.hasNext()){  
            Object key=iterator.next();  
            System.out.println(key+":"+map.get(key));  
        } 
	}
}
