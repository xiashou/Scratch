package com.tsingma.system.wechat.storage;

import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;

public class WxOpenCustomConfigStorage extends WxOpenInMemoryConfigStorage {

	private final static String COMPONENT_VERIFY_TICKET_KEY = "wechat_component_verify_ticket:";
	private final static String COMPONENT_ACCESS_TOKEN_KEY = "wechat_component_access_token:";

	private final static String AUTHORIZER_REFRESH_TOKEN_KEY = "wechat_authorizer_refresh_token:";
	private final static String AUTHORIZER_ACCESS_TOKEN_KEY = "wechat_authorizer_access_token:";
	private final static String JSAPI_TICKET_KEY = "wechat_jsapi_ticket:";
	private final static String CARD_API_TICKET_KEY = "wechat_card_api_ticket:";

	private String componentVerifyTicketKey;
	private String componentAccessTokenKey;
	private String authorizerRefreshTokenKey;
	private String authorizerAccessTokenKey;
	private String jsapiTicketKey;
	private String cardApiTicket;

	@Override
	public void setComponentAppId(String componentAppId) {
		super.setComponentAppId(componentAppId);
		this.componentVerifyTicketKey = COMPONENT_VERIFY_TICKET_KEY.concat(componentAppId);
		this.componentAccessTokenKey = COMPONENT_ACCESS_TOKEN_KEY.concat(componentAppId);
		this.authorizerRefreshTokenKey = AUTHORIZER_REFRESH_TOKEN_KEY.concat(componentAppId);
		this.authorizerAccessTokenKey = AUTHORIZER_ACCESS_TOKEN_KEY.concat(componentAppId);
		this.jsapiTicketKey = JSAPI_TICKET_KEY.concat(componentAppId);
		this.cardApiTicket = CARD_API_TICKET_KEY.concat(componentAppId);
	}

	public String getComponentVerifyTicketKey() {
		return componentVerifyTicketKey;
	}

	public void setComponentVerifyTicketKey(String componentVerifyTicketKey) {
		this.componentVerifyTicketKey = componentVerifyTicketKey;
	}

	public String getComponentAccessTokenKey() {
		return componentAccessTokenKey;
	}

	public void setComponentAccessTokenKey(String componentAccessTokenKey) {
		this.componentAccessTokenKey = componentAccessTokenKey;
	}

	public String getAuthorizerRefreshTokenKey() {
		return authorizerRefreshTokenKey;
	}

	public void setAuthorizerRefreshTokenKey(String authorizerRefreshTokenKey) {
		this.authorizerRefreshTokenKey = authorizerRefreshTokenKey;
	}

	public String getAuthorizerAccessTokenKey() {
		return authorizerAccessTokenKey;
	}

	public void setAuthorizerAccessTokenKey(String authorizerAccessTokenKey) {
		this.authorizerAccessTokenKey = authorizerAccessTokenKey;
	}

	public String getJsapiTicketKey() {
		return jsapiTicketKey;
	}

	public void setJsapiTicketKey(String jsapiTicketKey) {
		this.jsapiTicketKey = jsapiTicketKey;
	}

	public String getCardApiTicket() {
		return cardApiTicket;
	}

	public void setCardApiTicket(String cardApiTicket) {
		this.cardApiTicket = cardApiTicket;
	}

}
