package com.tsingma.system.wechat.action;

import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

import cn.binarywang.wx.miniapp.api.WxMaService;

@Scope("prototype")
@Component("WechatMiniProAction")
public class WechatMiniProAction extends BaseAction {

	private static final long serialVersionUID = 6549087661534194870L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
    private WxMaService wxService;
	
	private String signature;
	private String timestamp;
	private String nonce;
	private String echostr;
	
	/**
	 * 小程序接入门户方法
	 * @return
	 */
	public String programPortal() {
		PrintWriter pw = null;
		try {
			log.warn("\n接收到来自微信服务器的认证消息：signature = [{"+signature+"}], timestamp = [{"+timestamp+"}], nonce = [{"+nonce+"}], echostr = [{"+echostr+"}]");
			
			if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
	            throw new IllegalArgumentException("请求参数非法，请核实!");
	        }
			response.setContentType("text/html;charset=UTF-8");  
			pw = response.getWriter();
	        if (this.wxService.checkSignature(timestamp, nonce, signature)) {
		        pw.println(echostr);
	        }
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		} finally {
			pw.flush();  
	        pw.close(); 
		}
		return null;
	}

	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getEchostr() {
		return echostr;
	}
	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}
	
	

}
