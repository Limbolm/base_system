package com.smpl.base.Utils;

import java.security.MessageDigest;

/**
 * MD5工具类
 * MD5加密方式为 公钥+密码+私钥 的方式
 */
public final class MD5Utils {

    private static String PUBLIC_PASSWORD="smpl"; //公钥

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * MD5加密方法
     * @param pwd 需要加密的字符串
     * @param privatePwd 私钥
     * @return 返回MD5 加密后的String
     */
    public static String MD5Encrypt(String pwd,String privatePwd){
        return encrptByMD5(PUBLIC_PASSWORD+pwd+privatePwd);
    }

    /**
     * MD5算法
     * @param str
     * @return
     */
    private static String encrptByMD5(String str){
        if (!StringUtils.isEmpty(str)){
            try{
                MessageDigest messageDigest=MessageDigest.getInstance("MD5");
                byte[] bytes=messageDigest.digest(str.getBytes());
              return   byteTohexString(bytes).toUpperCase();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;

    }

    /**]
     * 将数组转换为十六进制 String
     * @param bytes
     * @return
     */
    private static String byteTohexString(byte[] bytes){
        StringBuffer stringBuffer=new StringBuffer();
        if (bytes.length>0){
            for (int i=0;i<bytes.length;i++){
                stringBuffer.append(byteToHex(bytes[i]));
            }
        }
        return stringBuffer.toString();
    }



    /**
     * 将一个字节 转换为 一个十六进制 字符串
     * @param be
     * @return
     */
    private static String byteToHex(byte be){
     int i=be;
        if(i<0)
           i=256+i;
        int be1=i / 16;
        int be2=i % 16;
        return hexDigits[be1]+hexDigits[be2];
    }


}
