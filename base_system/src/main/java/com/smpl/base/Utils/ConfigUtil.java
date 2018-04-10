package com.smpl.base.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import org.springside.modules.test.spring.Profiles;


/**
 * 微信配置参数
 * @author liuzhouwei
 *
 */
public class ConfigUtil {
	
	private static final String WX_CONFIG_DEFAULT_PATH = "wx/config.properties";
	private static final String WX_CONFIG_OTHER_PATH = "wx/config_PROFILE.properties";
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
			config.setAppId(properites.getProperty("appId"));
			config.setAppSecret(properites.getProperty("appSecret"));
			config.setAccessTokenUrl(properites.getProperty("access_token_url"));
			config.setMenuCreateUrl(properites.getProperty("menu_create_url"));
			config.setUploadMedia(properites.getProperty("upload_media"));
			config.setServerUrlRoot(properites.getProperty("server_url_root"));
			config.setServerDomain(properites.getProperty("server_domain"));
			config.setSnsapiUrl(properites.getProperty("snsapi_url"));
			config.setSnsAccessToken(properites.getProperty("sns_access_token"));
			config.setSnsRedirectUri(properites.getProperty("sns_redirect_uri"));
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
		private String appId;
		private String appSecret;
		private String accessTokenUrl;
		private String menuCreateUrl;
		private String uploadMedia;
		private String serverUrlRoot;
		private String serverDomain;
		private String snsapiUrl;
		private String snsAccessToken;
		private String snsRedirectUri;
				
		public String getSnsRedirectUri() {
			return snsRedirectUri;
		}
		public void setSnsRedirectUri(String snsRedirectUri) {
			this.snsRedirectUri = snsRedirectUri;
		}
		public String getSnsAccessToken() {
			return snsAccessToken;
		}
		public void setSnsAccessToken(String snsAccessToken) {
			this.snsAccessToken = snsAccessToken;
		}
		public String getSnsapiUrl() {
			return snsapiUrl;
		}
		public void setSnsapiUrl(String snsapiUrl) {
			this.snsapiUrl = snsapiUrl;
		}
		public String getServerDomain() {
			return serverDomain;
		}
		public void setServerDomain(String serverDomain) {
			this.serverDomain = serverDomain;
		}
		public String getServerUrlRoot() {
			return serverUrlRoot;
		}
		public void setServerUrlRoot(String serverUrlRoot) {
			this.serverUrlRoot = serverUrlRoot;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getAppSecret() {
			return appSecret;
		}
		public void setAppSecret(String appSecret) {
			this.appSecret = appSecret;
		}
		public String getAccessTokenUrl() {
			return accessTokenUrl;
		}
		public void setAccessTokenUrl(String accessTokenUrl) {
			this.accessTokenUrl = accessTokenUrl;
		}
		public String getMenuCreateUrl() {
			return menuCreateUrl;
		}
		public void setMenuCreateUrl(String menuCreateUrl) {
			this.menuCreateUrl = menuCreateUrl;
		}
		public String getUploadMedia() {
			return uploadMedia;
		}
		public void setUploadMedia(String uploadMedia) {
			this.uploadMedia = uploadMedia;
		}
		
		
	}

}
