package com.tsingma.system.wechat.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;
import com.tsingma.system.wechat.service.WxOpenCustomService;

import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;

@Scope("prototype")
@Component("WechatOpenApiAction")
public class WechatOpenApiAction extends BaseAction {

	private static final long serialVersionUID = -4739462875058257459L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private WxOpenCustomService wxOpenCustomService;
	
	private String authUrl;
	private String appId;
	private String auth_code;
	private Integer expires_in;
	
	public String gotoPreAuthUrl() {
		try {
			String host = request.getHeader("host");
	        String url = "http://"+host+"/platformApi/authJump";
	        authUrl = wxOpenCustomService.getWxOpenComponentService().getPreAuthUrl(url);
	        System.out.println(authUrl);
//            response.sendRedirect(url);
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String authJump() {
		try {
			WxOpenQueryAuthResult queryAuthResult = wxOpenCustomService.getWxOpenComponentService().getQueryAuth(auth_code);
			System.out.println("jump");
			System.out.println(queryAuthResult.getAuthorizationInfo().getAuthorizerAccessToken());
			System.out.println(queryAuthResult.getAuthorizationInfo().getAuthorizerAppid());
			System.out.println(queryAuthResult.getAuthorizationInfo().getAuthorizerRefreshToken());
			System.out.println(queryAuthResult.getAuthorizationInfo().getExpiresIn());
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String getAuthorizerInfo() {
		try {
			System.out.println("auth");
			WxOpenAuthorizerInfoResult openAuthorizerInfo = wxOpenCustomService.getWxOpenComponentService().getAuthorizerInfo(appId);
			System.out.println(openAuthorizerInfo.getAuthorizerInfo().getNickName());
			System.out.println(openAuthorizerInfo.getAuthorizerInfo().getUserName());
			System.out.println(openAuthorizerInfo.getAuthorizationInfo().getAuthorizerAppid());
			System.out.println(openAuthorizerInfo.getAuthorizationInfo().getAuthorizerAccessToken());
			System.out.println(openAuthorizerInfo.getAuthorizationInfo().getAuthorizerRefreshToken());
			System.out.println(openAuthorizerInfo.getAuthorizationInfo().getExpiresIn());
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	

}
