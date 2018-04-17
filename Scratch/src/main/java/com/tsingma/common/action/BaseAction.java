package com.tsingma.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.tsingma.system.wechat.service.WxMiniCustomService;
import com.tsingma.system.wechat.service.WxOpenCustomService;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;

public class BaseAction extends ActionSupport {

	private static final long serialVersionUID = -7840704472234586868L;
	
	protected HttpServletResponse response = ServletActionContext.getResponse();
	protected HttpServletRequest request = ServletActionContext.getRequest();
	
	@Autowired
	private WxOpenCustomService wxOpenCustomService;
	@Autowired
	private WxMiniCustomService wxMiniCustomService;
	
	private Boolean success;
	private String msg;

	private Integer start;
	private Integer limit;
	private Integer totalCount;
	
	public void setResult(Boolean success, String msg) {
		this.setSuccess(success);
		this.setMsg(msg);
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public WxOpenConfigStorage getWxConfig() {
		return wxOpenCustomService.getWxOpenConfigStorage();
	}

	public WxMaConfig getWxMaConfig() {
		return wxMiniCustomService.getWxMaConfig();
	}
}
