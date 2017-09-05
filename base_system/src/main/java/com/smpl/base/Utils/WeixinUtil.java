package com.smpl.base.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSONObject;
import com.smpl.base.entity.AccessToken;
import com.smpl.base.interceptor.WXSNSInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WeixinUtil {
	
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
	
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = ConfigUtil.getConfig().getAccessTokenUrl();
	
	// 菜单创建（POST） 限100（次/天）
	public static String menu_create_url = ConfigUtil.getConfig().getMenuCreateUrl();

	//上传多媒体文件
	public static String upload_media = ConfigUtil.getConfig().getUploadMedia();

	
	/**
	 * 获取access_token
	 * 
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		if(null!=jsonObject){
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
			} catch (Exception e) {
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
				return null;
			}
		}
		return accessToken;
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new X509TrustManagerImpl() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}
	
	/**
	 * 获取网页授权access_token的URL
	 * @param code
	 * @return
	 */
	public static String getSnsAccessTokenUrl(String code){
		String url = ConfigUtil.getConfig().getSnsAccessToken();
		url = url.replace("APPID", ConfigUtil.getConfig().getAppId())
				.replace("SECRET", ConfigUtil.getConfig().getAppSecret())
				.replace("CODE", code);
		
		return url;
	}
	
	/**
	 * 根据code获取openid
	 * @param code
	 * @return
	 */
	public static String getOpenid(String code){
		String url = getSnsAccessTokenUrl(code);
		JSONObject result = httpRequest(url, "GET", null);
		if(result!=null){
			return (String) result.get("openid");
		}else{
			return "";
		}
	}

	/**
	 * 获取微信用户授权路径
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getRedirectUrl(String url)
			throws UnsupportedEncodingException {
		String  snsRedirectUri = ConfigUtil.getConfig().getSnsRedirectUri();
		WXSNSInterceptor.logger.debug("url :" + url);
		String redirectUrlTemplate = ConfigUtil.getConfig().getSnsapiUrl();
		
		String redirectUrl = redirectUrlTemplate
				.replace("APPID", ConfigUtil.getConfig().getAppId())
				.replace("REDIRECT_URI", URLEncoder.encode(snsRedirectUri, "utf-8"))
				.replace("SCOPE", "snsapi_base")
				.replace("STATE", URLEncoder.encode(url, "utf-8"));
		WXSNSInterceptor.logger.debug("url:" + url +"没有openid，跳转到微信授权界面: "+redirectUrl);
		return redirectUrl;
	}

}
