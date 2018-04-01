package com.tsingma.system.wechat.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;
import com.tsingma.system.wechat.service.WxOpenCustomService;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;

@Scope("prototype")
@Component("WechatOpenPlatformAction")
public class WechatOpenPlatformAction extends BaseAction {

	private static final long serialVersionUID = 6851150280040411122L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private WxOpenCustomService wxOpenCustomService;
	
	private String signature;
	private String encrypt_type;
	private String msg_signature;
	private String timestamp;
	private String nonce;
	
	private String appId;
	private String openid;

	
	/**
	 * 授权事件接收
	 * @return
	 */
	public String wechatAuthorization() {
		PrintWriter pw = null;
		try {
			log.warn("接收微信请求：[signature=[{"+signature+"}], encrypt_type=[{"+encrypt_type+"}], msg_signature=[{"+msg_signature+"}],"
	                        + " timestamp=[{"+timestamp+"}], nonce=[{"+nonce+"}] ");
	        if (!StringUtils.equalsIgnoreCase("aes", encrypt_type) || !wxOpenCustomService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
	            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
	        }
	        
	        response.setContentType("text/html;charset=UTF-8");  
	        InputStreamReader inputReader = new InputStreamReader(request.getInputStream(), "UTF-8"); 
    		BufferedReader bufferReader = new BufferedReader(inputReader); 
    		StringBuilder requestBody = new StringBuilder(); 
    		String line = null; 
    		while ((line = bufferReader.readLine()) != null) { 
    			requestBody.append(line); 
    		}
	        pw = response.getWriter();
//	        System.out.println(requestBody.toString());
	        
	        // aes加密的消息
	        WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody.toString(), wxOpenCustomService.getWxOpenConfigStorage(), timestamp, nonce, msg_signature);
	        log.warn("消息解密后内容为： " + inMessage.toString());
	        String out = wxOpenCustomService.getWxOpenComponentService().route(inMessage);
	        log.warn("组装回复信息：" + out);
	        pw.println(out);
		} catch(WxErrorException e1) {
			log.error(Utils.getErrorMessage(e1));
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		} finally {
			pw.flush();  
	        pw.close(); 
		}
		return null;
	}
	
	/**
	 * 消息与事件接收
	 * @return
	 */
	public String wechatMessageEvent() {
		PrintWriter pw = null;
		try {
			log.warn("接收微信请求：[openid=[{"+openid+"}], [signature=[{"+signature+"}], encrypt_type=[{"+encrypt_type+"}], msg_signature=[{"+msg_signature+"}],"
	                        + " timestamp=[{"+timestamp+"}], nonce=[{"+nonce+"}] ");
			if (!StringUtils.equalsIgnoreCase("aes", encrypt_type) || !wxOpenCustomService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
	            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
	        }
			
			String requestUri = request.getRequestURI();
			String reqStr[] = requestUri.split("/");
			this.setAppId(reqStr[2]);
			
			response.setContentType("text/html;charset=UTF-8");
	        InputStreamReader inputReader = new InputStreamReader(request.getInputStream(), "UTF-8"); 
    		BufferedReader bufferReader = new BufferedReader(inputReader); 
    		StringBuilder requestBody = new StringBuilder(); 
    		String line = null;
    		while ((line = bufferReader.readLine()) != null) {
    			requestBody.append(line); 
    		}
	        pw = response.getWriter();
	        System.out.println("MessageEvent appId:" + appId);
	        System.out.println(requestBody.toString());
	        
	        String out = "";
	        // aes加密的消息
	        WxMpXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedMpXml(requestBody.toString(), wxOpenCustomService.getWxOpenConfigStorage(), timestamp, nonce, msg_signature);
	        log.warn("消息解密后内容为：" + inMessage.toString());
            WxMpXmlOutMessage outMessage = wxOpenCustomService.getWxOpenMessageRouter().route(inMessage, appId);
            if(outMessage != null){
                out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(outMessage, wxOpenCustomService.getWxOpenConfigStorage());
            }
	        pw.println(out);
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
	public String getEncrypt_type() {
		return encrypt_type;
	}
	public void setEncrypt_type(String encrypt_type) {
		this.encrypt_type = encrypt_type;
	}
	public String getMsg_signature() {
		return msg_signature;
	}
	public void setMsg_signature(String msg_signature) {
		this.msg_signature = msg_signature;
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
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
