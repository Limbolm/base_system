package com.smpl.base.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smpl.base.Utils.WXQYHUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29 0029.
 */
public class SessionComponent implements Serializable {

    private static final long serialVersionUID = -6103144231774343610L;
    private static final String secretaryTagId = "6";
    private String appName;
    private String openid;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public static SessionComponent getInstance(ApplicationContext context) {
        return context.getBean(SessionComponent.class);
    }

    public boolean isSecretary() throws Exception {
        if(userId==null) return false;
        JSONObject result = WXQYHUtil.httpRequest(WXQYHUtil.getTagUrl(appName).replace("TAGID", secretaryTagId), "GET", "");
        String objectstr = result.get("userlist").toString();
        List<Object> array = JSONArray.parseArray(objectstr);
        for(Object object:array){
            String jsonstr = JSON.toJSONString(object);
            JSONObject o = JSONObject.parseObject(jsonstr);
            if(userId.equals(o.get("userid"))){
                return true;
            }
        }
        return false;
    }

//	public List<T_LZCITY_APPROVE_UNIT_INFO> getUnit() {
//		return unit;
//	}
//
//	public void setUnit(List<T_LZCITY_APPROVE_UNIT_INFO> unit) {
//		this.unit = unit;
//	}
}
