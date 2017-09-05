package com.smpl.base.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.smpl.base.Utils.QYH.ConfigUtil;
import com.smpl.base.entity.AccessToken;
import com.smpl.base.entity.DataMap;
import com.smpl.base.interceptor.WXSNSInterceptor;
import com.smpl.base.spring.SpringBeanManager;
import com.smpl.main.service.QyhAccessService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WXQYHUtil {

	private static Logger log = LoggerFactory.getLogger(WXQYHUtil.class);

	//设置缓存时间
	static final long cacheTime = 1000 * 60 * 30;
	static QyhAccessService _qyhAccessService;

	protected static QyhAccessService getQyhAccessService(){
		if(_qyhAccessService == null){
			_qyhAccessService = SpringBeanManager.getBean(QyhAccessService.class);
		}
		return _qyhAccessService;
	}
	// 获取access_token的接口地址（GET）
	private final static String access_token_url = ConfigUtil.getConfig().getAccessTokenUrl();

	private final static String sendmessage_url = ConfigUtil.getConfig().getSendMessageUrl();

	private final static String corpid = ConfigUtil.getConfig().getCorpid();

	private final static String corpsecret = ConfigUtil.getConfig().getCorpsecret();

	private final static String corpsecretDirectories = ConfigUtil.getConfig().getCorpsecretDirectories();

	private final static String departments_url = ConfigUtil.getConfig().getGetDepartmentUrl();

	private final static String create_department_url = ConfigUtil.getConfig().getCreateDepartmentUrl();

	private final static String update_department_url = ConfigUtil.getConfig().getUpdateDepartmentUrl();

	private final static String delete_department_url = ConfigUtil.getConfig().getDeleteDepartmentUrl();

	private final static String user_simplelist_url = ConfigUtil.getConfig().getUserSimpleListUrl();

	private final static String create_user_url = ConfigUtil.getConfig().getCreateWxuserUrl();

	private final static String update_user_url = ConfigUtil.getConfig().getUpdateWxuserUrl();

	private final static String user_id_url = ConfigUtil.getConfig().getUserIdUrl();

	private final static String user_info_url = ConfigUtil.getConfig().getUserInfoUrl();

	private final static String delete_user_url = ConfigUtil.getConfig().getDeleteWxuserUrl();

	private final static String batchdelete_user_url = ConfigUtil.getConfig().getBatchDeleteWxuserUrl();

	private final static String get_department_wxuser_url = ConfigUtil.getConfig().getGetDepartmentWxuserUrl();

	private final static String tag_url = ConfigUtil.getConfig().getTagUrl();

	private final static String tag_list_url = ConfigUtil.getConfig().getTagListUrl();

	private final static String jsapi_ticket_url = ConfigUtil.getConfig().getJsapiTicketUrl();

	private final static String group_ticket_url = ConfigUtil.getConfig().getGroupTicketUrl();
	/**
	 * 获取access_token
	 *
	 * @return
	 */
	//https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=CORPSECRET
//	@Deprecated
	public static AccessToken getAccessToken(String corpid, String _corpsecret,String appName) throws Exception {
		QyhAccessService accessService = getQyhAccessService();
		DataMap atv = accessService.selectQyhAccess(corpid, appName+"Token");
		long curTime = System.currentTimeMillis();
		//获取accesstoken
		AccessToken accessToken = null;
		int expires_in = 0;
		if (atv == null || (curTime - Long.parseLong(atv.get("BEGIN_TIME").toString())) >= cacheTime) {
			try {
				String requestUrl = access_token_url.replace("CORPID", corpid).replace("CORPSECRET", _corpsecret);
				log.info("requestUrl: " + requestUrl);
				JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
				if(null!=jsonObject){
					expires_in = jsonObject.getInteger("expires_in");
					try {
						if(atv == null){
							atv = new DataMap();
							atv.put("CORP_ID",corpid);
							atv.put("TYPE",appName+"Token");
							atv.put("VALUE",jsonObject.getString("access_token"));
							atv.put("BEGIN_TIME",curTime);
							log.info("插入的token: " + jsonObject.getString("access_token") + " 插入时间：" + curTime);
							accessService.insertQyhAccess(atv);
						}else{
							atv.put("VALUE",jsonObject.getString("access_token"));
							atv.put("BEGIN_TIME",curTime);
							log.info("更新的token: " + jsonObject.getString("access_token") + " 更新时间：" + curTime);
							accessService.updateQyhAccess(atv);
						}
					} catch (Exception e) {
						log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
						return null;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		accessToken = new AccessToken();
		accessToken.setToken(atv.get("VALUE").toString());
		accessToken.setExpiresIn(expires_in);
		return accessToken;
	}

	public static AccessToken getAccessToken(String _corpsecret,String appName) throws Exception {
		if(null!=_corpsecret && !"".equals(_corpsecret)){
			return getAccessToken(corpid, _corpsecret,appName);
		}
		return getAccessToken(corpid, corpsecret);
	}

	public static String getSendMessageUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return sendmessage_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getUpdateUserUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return update_user_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getCreateUserUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return create_user_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getUserSimplelistUrl(String departmentId,String fetchChild,String status,String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return user_simplelist_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken()).replace("DEPARTMENT_ID", departmentId).replace("FETCH_CHILD", fetchChild).replace("STATUS", status);
	}

	public static String getDepartmentsUrl(String departmentId,String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return departments_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken()).replace("DEPARTMENT_ID", departmentId);
	}

	public static String getDeleteUserUrl(String userId,String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return delete_user_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken()).replace("USERID", userId);
	}

	public static String getDeleteDepartmentUrl(String departmentId,String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return delete_department_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken()).replace("DEPARTMENT_ID", departmentId);
	}

	public static String getCreateDepartmentUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return create_department_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}


	public static String getUpdateDepartmentUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return update_department_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getUserIdUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return user_id_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}


	public static String getUserInfoUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return user_info_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getTagUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return tag_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getTagListUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return tag_list_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getBatchdeleteUserUrl(String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return batchdelete_user_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken());
	}

	public static String getGetDepartmentWxuserUrl(String departmentId,String appName) throws Exception {
		String secret = "";
		if(ReadPropertiesUtils.iscontain(appName)){
			secret = ReadPropertiesUtils.getSecret(appName);
		}
		return get_department_wxuser_url.replace("ACCESS_TOKEN",getAccessToken(corpid,secret,appName).getToken()).replace("DEPARTMENT_ID", departmentId);
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
		log.debug("********发起https请求*********");
		log.debug("requestUrl: " + requestUrl + ", requestMethod:" + requestMethod + ", outputStr:" + outputStr);
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
		log.debug("jsonObject:"+jsonObject);
		log.debug("********返回https请求的结果*********");
		return jsonObject;
	}

	public static String getRedirectUrl(String url)
		throws UnsupportedEncodingException {
			String  authRedirectUrl = ConfigUtil.getConfig().getAuthRedirectUrl();
			WXSNSInterceptor.logger.debug("url :" + url);
			String redirectUrlTemplate = ConfigUtil.getConfig().getAuthUrl();

			String redirectUrl = redirectUrlTemplate
					.replace("CORPID", ConfigUtil.getConfig().getCorpid())
					.replace("REDIRECT_URI", URLEncoder.encode(authRedirectUrl+"?url="+ URLEncoder.encode(url, "utf-8"), "utf-8"))
					.replace("SCOPE", "snsapi_base")
					.replace("STATE", "");
			WXSNSInterceptor.logger.debug("url:" + url +"没有userid，跳转到微信授权界面: "+redirectUrl);
			return redirectUrl;
	}

	public static String getUserId(String code,String appName) throws Exception {
		String url = getUserIdUrl(appName).replace("CODE", code);

		JSONObject result = httpRequest(url, "GET", null);
		if(result!=null){
			return (String) result.get("UserId");
		}else{
			return "";
		}
	}

	public static String getUserInfo(String userId,String appName) throws Exception {
		String url = getUserInfoUrl(appName).replace("USERID", userId);
		JSONObject result = httpRequest(url, "GET", null);
		if(result!=null){
			System.out.println(JSONObject.toJSON(result).toString());
			return url;
		}else{
			return "";
		}

	}

	/**
	 * 获取group_ticket
	 * 注意：group_ticket 的有效期为 7200 秒，开发者必须全局缓存 group_ticket ，防止超过调用频率。
	 */
	public static Map<String,Object> getGroupTicketAndGroupId(String accessToken) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		QyhAccessService accessService = getQyhAccessService();
		DataMap accessTokenValue = accessService.selectQyhAccess(corpid, "groupTicket");
		//获取系统当前时间
		long curTime = System.currentTimeMillis();
		String groupId = "";
		if (accessTokenValue == null || (curTime - Long.parseLong(accessTokenValue.get("BEGIN_TIME").toString()) >= cacheTime) ){
			try{
				//获取groupticket
				String url = group_ticket_url.replace("ACCESS_TOKEN", accessToken);
				JSONObject result = httpRequest(url, "GET", null);
				String groupticket = (String) result.get("ticket");
				groupId = (String) result.get("group_id");
				if(accessTokenValue == null){
					accessTokenValue = new DataMap();
					accessTokenValue.put("CORP_ID",corpid);
					accessTokenValue.put("TYPE","groupTicket");
					accessTokenValue.put("GROUP_ID",groupId);
					accessTokenValue.put("VALUE",groupticket);
					accessTokenValue.put("BEGIN_TIME",curTime);
					log.info("插入的ticket: " + groupticket + " 插入时间：" + curTime);
					accessService.insertQyhAccess(accessTokenValue);
				}else{
					accessTokenValue.put("GROUP_ID",groupId);
					accessTokenValue.put("VALUE",groupticket);
					accessTokenValue.put("BEGIN_TIME",curTime);
					log.info("更新的ticket: " + groupticket + " 更新时间：" + curTime);
					accessService.updateQyhAccess(accessTokenValue);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		map.put("groupId", accessTokenValue.get("GROUP_ID"));
		map.put("groupticket", accessTokenValue.get("VALUE"));
		return map;
	}

	/**
	 * 获取jsapi_ticket
	 * 注意：jsapi_ticket 的有效期为 7200 秒，开发者必须全局缓存 jsapi_ticket ，防止超过调用频率。
	 */
	public static String getJsapiTicket(String accessToken,String appName) throws Exception {
		QyhAccessService accessService = getQyhAccessService();
		DataMap accessTokenValue = accessService.selectQyhAccess(corpid, appName+"Ticket");
		//获取系统当前时间
		long curTime = System.currentTimeMillis();
		if (accessTokenValue == null || (curTime - Long.parseLong(accessTokenValue.get("BEGIN_TIME").toString()) >= cacheTime)){
			try{
				//获取jsticket
				String url = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
				JSONObject result = httpRequest(url, "GET", null);
				String jsTicket = (String) result.get("ticket");
				if(accessTokenValue == null){
					accessTokenValue = new DataMap();
					accessTokenValue.put("CORP_ID",corpid);
					accessTokenValue.put("TYPE",appName+"Ticket");
					accessTokenValue.put("VALUE",jsTicket);
					accessTokenValue.put("BEGIN_TIME",curTime);
					log.info("插入的ticket: " + jsTicket + " 插入时间：" + curTime);
					accessService.insertQyhAccess(accessTokenValue);
				}else{
					accessTokenValue.put("VALUE",jsTicket);
					accessTokenValue.put("BEGIN_TIME",curTime);
					log.info("更新的ticket: " + jsTicket + " 更新时间：" + curTime);
					accessService.updateQyhAccess(accessTokenValue);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return accessTokenValue.get("VALUE").toString();
	}
	
	public static String group_sign(String ticket, String nonceStr, long timeStamp, String url){
		String plain = "group_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
		+ "&url=" + url;
		String result = "";
		try{
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			result = bytesToHex(sha1.digest());
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static String sign(String ticket, String nonceStr, long timeStamp, String url){
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
		+ "&url=" + url;
		String result = "";
		try{
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			result = bytesToHex(sha1.digest());
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	
	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	@SuppressWarnings("deprecation")
	public static String getGroupConfig(HttpServletRequest request){
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		String queryStringEncode = null;
		String url;
		if (queryString != null) {
			queryStringEncode = URLDecoder.decode(queryString);
			url = urlString + "?" + queryStringEncode;
		} else {
			url = urlString;
		}
		if ("//".endsWith(url.substring(url.length() - 2, url.length()))) {
			url = url.substring(0, url.length() - 1);
		}

		String nonceStr = "abcdef";
		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl = url;
		
		String accessToken = null;
		String groupticket = null;
		String signature = null;
		String groupId = null;
		
		try{
			accessToken = WXQYHUtil.getAccessToken(corpid,corpsecret).getToken();
			Map<String,Object> ticketmap = WXQYHUtil.getGroupTicketAndGroupId(accessToken);
			groupticket = (String) ticketmap.get("groupticket");
			groupId = (String) ticketmap.get("groupId");
			signature = WXQYHUtil.group_sign(groupticket,nonceStr,timeStamp,signedUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String configValue = "{groupTicket:'" + groupticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr
				+ "',timeStamp:'" + timeStamp + "',groupId:'" + groupId + "'}";
		System.out.println(configValue);
		return configValue;
	}
	
	@SuppressWarnings("deprecation")
	public static String getConfig(HttpServletRequest request,String appName){
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		String queryStringEncode = null;
		String url;
		if (queryString != null) {
			queryStringEncode = URLDecoder.decode(queryString);
			url = urlString + "?" + queryStringEncode;
		} else {
			url = urlString;
		}
		if ("//".endsWith(url.substring(url.length() - 2, url.length()))) {
			url = url.substring(0, url.length() - 1);
		}

		String nonceStr = "abcdef";
		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl = url;
		
		String accessToken = null;
		String ticket = null;
		String signature = null;
		
		try{
			String secret = "";		
			if(ReadPropertiesUtils.iscontain(appName)){			
				secret = ReadPropertiesUtils.getSecret(appName);
			}
			accessToken = WXQYHUtil.getAccessToken(corpid,secret,appName).getToken();
			ticket = WXQYHUtil.getJsapiTicket(accessToken,appName);
			signature = WXQYHUtil.sign(ticket,nonceStr,timeStamp,signedUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr
				+ "',timeStamp:'" + timeStamp + "',appId:'" + corpid + "'}";
		System.out.println(configValue);
		return configValue;
	}
	
	
	
	public static String getaccessToken(HttpServletRequest request){
		String accessToken = null;
		try{
			accessToken = WXQYHUtil.getAccessToken(corpid,corpsecret).getToken();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return accessToken;
	}
}
