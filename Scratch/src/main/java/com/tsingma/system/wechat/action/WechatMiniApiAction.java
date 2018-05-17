package com.tsingma.system.wechat.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.member.service.MemberService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

@Scope("prototype")
@Component("WechatMiniApiAction")
public class WechatMiniApiAction extends BaseAction {

	private static final long serialVersionUID = -4621042841504918499L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
    private WxMaService wxService;
	@Autowired
	private MemberService memberService;
	
	private String code;
	private String encryptedData;
	private String iv;
	private String rawData;
	private String signature;
	private String sessionKey;
	
	private WxMaJscode2SessionResult wxSession;
	
	/**
	 * 获取小程序用户登录所需参数
	 * @return
	 */
	public String userLogin() {
		try {
			if (!StringUtils.isBlank(code)) {
				wxSession = this.wxService.getUserService().getSessionInfo(code);
//				System.out.println("userLogin:" + wxSession);
	        } else
	        	log.error("code is null.");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 用户注册成功后存入数据库
	 * @return
	 */
	public String userRegister() {
		try {
			// 用户信息校验
	        if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
	        	log.error("用户校验失败！");
	        	log.error("sessionKey:" + sessionKey + " rawData:" + rawData + " signature:" + signature);
	            return null;
	        }
//	        System.out.println("sessionKey:" + sessionKey + " encryptedData:" + encryptedData + " iv:" + iv);
	        // 解密用户信息
	        WxMaUserInfo userInfo = this.wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
	        log.warn(userInfo.getOpenId() + "|" + userInfo.getAvatarUrl() + "|" + userInfo.getCity() + "|" + userInfo.getCountry() + "|" + 
	        userInfo.getGender() + "|" + userInfo.getLanguage() + "|" + userInfo.getNickName() + "|" + userInfo.getProvince() + "|" + userInfo.getUnionId());
	        //保存用户信息
	        if(!memberService.checkMemberExist(userInfo.getOpenId())){
	        	memberService.insert(userInfo);
	        	log.warn(userInfo.getOpenId() + " 插入成功！");
	        	System.out.println("openId:" + userInfo.getOpenId());
	        }
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return null;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public WxMaJscode2SessionResult getWxSession() {
		return wxSession;
	}
	public void setWxSession(WxMaJscode2SessionResult wxSession) {
		this.wxSession = wxSession;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	public String getRawData() {
		return rawData;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	
	
}
