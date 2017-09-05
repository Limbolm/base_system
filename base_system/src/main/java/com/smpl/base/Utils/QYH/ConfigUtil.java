package com.smpl.base.Utils.QYH;

import org.springside.modules.test.spring.Profiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/8/24 0024.
 */
public class ConfigUtil {
    private static final String WX_CONFIG_DEFAULT_PATH = "properties/config.properties";
    private static final String WX_CONFIG_OTHER_PATH = "properties/config_PROFILE.properties";
    private static final String PATH = getConfigPath();

    private static Properties properites;

    private static Config config = new Config();

    static {
        InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(PATH);
        properites = new Properties();
        try {
            properites.load(in);
            in.close();
            config.setToken(properites.getProperty("token"));
            config.setCorpid(properites.getProperty("corpid"));
            config.setCorpsecret(properites.getProperty("corpsecret"));
            config.setCorpsecretDirectories(properites.getProperty("corpsecret_directories"));
            config.setAccessTokenUrl(properites.getProperty("access_token_url"));
            config.setSendMessageUrl(properites.getProperty("send_message_url"));
            config.setGetDepartmentUrl(properites.getProperty("get_departments_url"));
            config.setCreateDepartmentUrl(properites.getProperty("create_departments_url"));
            config.setUpdateDepartmentUrl(properites.getProperty("update_departments_url"));
            config.setDeleteDepartmentUrl(properites.getProperty("delete_department_url"));
            config.setUserSimpleListUrl(properites.getProperty("get_user_simplelist_url"));
            config.setCreateWxuserUrl(properites.getProperty("create_wxuser_url"));
            config.setUpdateWxuserUrl(properites.getProperty("update_wxuser_url"));
            config.setAuthUrl(properites.getProperty("auth_url"));
            config.setUserIdUrl(properites.getProperty("user_id_url"));
            config.setAuthRedirectUrl(properites.getProperty("auth_redirect_url"));
            config.setScheduleUrl(properites.getProperty("schedule_url"));
            config.setUserInfoUrl(properites.getProperty("user_url"));
            config.setTagUrl(properites.getProperty("tag_url"));
            config.setTagListUrl(properites.getProperty("tag_list_url"));
            config.setScheduleOperateUrl(properites.getProperty("schedule_operate_url"));
            config.setJsapiTicketUrl(properites.getProperty("jsapi_ticket_url"));
            config.setGroupTicketUrl(properites.getProperty("group_ticket_url"));
            config.setDeleteWxuserUrl(properites.getProperty("delete_wxuser_url"));
            config.setBatchDeleteWxuserUrl(properites.getProperty("batchdelete_wxuser_url"));
            config.setGetDepartmentWxuserUrl(properites.getProperty("get_department_wxuser_url"));
        } catch (IOException e) {
            //System.out.println("No errorMessage.properties defined error");
        }
    }

    private static String getConfigPath() {
        String profiles = System.getProperty("spring.profiles.active");
        String path = WX_CONFIG_DEFAULT_PATH;
        if(profiles!=null && !Profiles.PRODUCTION.equals(profiles) ){
            path = WX_CONFIG_OTHER_PATH.replace("PROFILE", profiles);
        }
        return path;
    }

    public static Config getConfig(){
        return config;
    }

    public static class Config {
        private String token;
        private String corpid;
        private String corpsecret;
        private String corpsecretDirectories;
        private String accessTokenUrl;
        private String sendMessageUrl;
        private String getDepartmentUrl;
        private String createDepartmentUrl;
        private String updateDepartmentUrl;
        private String deleteDepartmentUrl;
        private String userSimpleListUrl;
        private String createWxuserUrl;
        private String updateWxuserUrl;
        private String deleteWxuserUrl;
        private String batchDeleteWxuserUrl;
        private String getDepartmentWxuserUrl;
        private String authUrl;
        private String userIdUrl;
        private String authRedirectUrl;
        private String userInfoUrl;
        private String scheduleUrl;
        private String groupTicketUrl;
        private String tagUrl;
        //[{"tagid":1,"tagname":"处长"},{"tagid":2,"tagname":"副处长"},{"tagid":3,"tagname":"委领导"},{"tagid":4,"tagname":"党组成员"},{"tagid":5,"tagname":"委属单位处长"},{"tagid":6,"tagname":"文秘组"}]
        private String tagListUrl;
        private String scheduleOperateUrl;
        private String jsapiTicketUrl;

        public String getDeleteWxuserUrl() {
            return deleteWxuserUrl;
        }
        public void setDeleteWxuserUrl(String deleteWxuserUrl) {
            this.deleteWxuserUrl = deleteWxuserUrl;
        }
        public String getBatchDeleteWxuserUrl() {
            return batchDeleteWxuserUrl;
        }
        public void setBatchDeleteWxuserUrl(String batchDeleteWxuserUrl) {
            this.batchDeleteWxuserUrl = batchDeleteWxuserUrl;
        }
        public String getGetDepartmentWxuserUrl() {
            return getDepartmentWxuserUrl;
        }
        public void setGetDepartmentWxuserUrl(String getDepartmentWxuserUrl) {
            this.getDepartmentWxuserUrl = getDepartmentWxuserUrl;
        }
        public String getDeleteDepartmentUrl() {
            return deleteDepartmentUrl;
        }
        public void setDeleteDepartmentUrl(String deleteDepartmentUrl) {
            this.deleteDepartmentUrl = deleteDepartmentUrl;
        }
        public String getCorpsecretDirectories() {
            return corpsecretDirectories;
        }
        public void setCorpsecretDirectories(String corpsecretDirectories) {
            this.corpsecretDirectories = corpsecretDirectories;
        }
        public String getGroupTicketUrl() {
            return groupTicketUrl;
        }
        public void setGroupTicketUrl(String groupTicketUrl) {
            this.groupTicketUrl = groupTicketUrl;
        }
        public String getUpdateWxuserUrl() {
            return updateWxuserUrl;
        }
        public void setUpdateWxuserUrl(String updateWxuserUrl) {
            this.updateWxuserUrl = updateWxuserUrl;
        }
        public String getCreateWxuserUrl() {
            return createWxuserUrl;
        }
        public void setCreateWxuserUrl(String createWxuserUrl) {
            this.createWxuserUrl = createWxuserUrl;
        }
        public String getUserSimpleListUrl() {
            return userSimpleListUrl;
        }
        public void setUserSimpleListUrl(String userSimpleListUrl) {
            this.userSimpleListUrl = userSimpleListUrl;
        }
        public String getUpdateDepartmentUrl() {
            return updateDepartmentUrl;
        }
        public void setUpdateDepartmentUrl(String updateDepartmentUrl) {
            this.updateDepartmentUrl = updateDepartmentUrl;
        }
        public String getCreateDepartmentUrl() {
            return createDepartmentUrl;
        }
        public void setCreateDepartmentUrl(String createDepartmentUrl) {
            this.createDepartmentUrl = createDepartmentUrl;
        }
        public String getGetDepartmentUrl() {
            return getDepartmentUrl;
        }
        public void setGetDepartmentUrl(String getDepartmentUrl) {
            this.getDepartmentUrl = getDepartmentUrl;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public String getCorpid() {
            return corpid;
        }
        public void setCorpid(String corpid) {
            this.corpid = corpid;
        }
        public String getCorpsecret() {
            return corpsecret;
        }
        public void setCorpsecret(String corpsecret) {
            this.corpsecret = corpsecret;
        }
        public String getAccessTokenUrl() {
            return accessTokenUrl;
        }
        public void setAccessTokenUrl(String accessTokenUrl) {
            this.accessTokenUrl = accessTokenUrl;
        }
        public String getSendMessageUrl() {
            return sendMessageUrl;
        }
        public void setSendMessageUrl(String sendMessageUrl) {
            this.sendMessageUrl = sendMessageUrl;
        }
        public String getAuthUrl() {
            return authUrl;
        }
        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }
        public String getUserIdUrl() {
            return userIdUrl;
        }
        public void setUserIdUrl(String userIdUrl) {
            this.userIdUrl = userIdUrl;
        }
        public String getAuthRedirectUrl() {
            return authRedirectUrl;
        }
        public void setAuthRedirectUrl(String authRedirectUrl) {
            this.authRedirectUrl = authRedirectUrl;
        }
        public String getUserInfoUrl() {
            return userInfoUrl;
        }
        public void setUserInfoUrl(String userInfoUrl) {
            this.userInfoUrl = userInfoUrl;
        }
        public String getScheduleUrl() {
            return scheduleUrl;
        }
        public void setScheduleUrl(String scheduleUrl) {
            this.scheduleUrl = scheduleUrl;
        }
        public String getTagUrl() {
            return tagUrl;
        }
        public void setTagUrl(String tagUrl) {
            this.tagUrl = tagUrl;
        }
        public String getTagListUrl() {
            return tagListUrl;
        }
        public void setTagListUrl(String tagListUrl) {
            this.tagListUrl = tagListUrl;
        }
        public String getScheduleOperateUrl() {
            return scheduleOperateUrl;
        }
        public void setScheduleOperateUrl(String scheduleOperateUrl) {
            this.scheduleOperateUrl = scheduleOperateUrl;
        }
        public String getJsapiTicketUrl() {
            return jsapiTicketUrl;
        }
        public void setJsapiTicketUrl(String jsapiTicketUrl) {
            this.jsapiTicketUrl = jsapiTicketUrl;
        }

    }

}