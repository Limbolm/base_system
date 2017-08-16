package com.smpl.base.entity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by chenxiaopei on 2017/8/16.
 */
public class DataMap<k,v> extends HashMap implements Serializable{

    private String mapperId;

    /**
     *  获取 key 的value 转换为String
     */

    public final String getStr(String key){
        return this.get(key).toString();
    }

    public void setMapperId(String mapperId) {
        this.mapperId = mapperId;
    }

    public String getMapperId() {
        return mapperId;
    }
}
