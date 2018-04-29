package com.tsingma.system.wechat.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.tsingma.business.scratch.service.PaymentService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;
import com.tsingma.system.wechat.service.WxPayCustomService;

@Scope("prototype")
@Component("WechatPayAction")
public class WechatPayAction extends BaseAction {

	private static final long serialVersionUID = -6519830695740900751L;
	private static Logger log = Logger.getLogger("PLog");
	
	@Autowired
	private WxPayCustomService wxPayCustomService;
	@Autowired
	private PaymentService paymentService;
	
	private String wxPayResponse;
	
	/**
	 * 支付结果通知.
	 * @return
	 */
	public String wechatPayNotify() {
		InputStream inputStream = null;
		this.getResponse().setCharacterEncoding("utf-8");
        PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
		    StringBuffer sb = new StringBuffer();
		    inputStream = request.getInputStream();
		    String s;
		    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		    while ((s = in.readLine()) != null){ 
		      sb.append(s);
		    }
		    in.close();
		    inputStream.close();
		    WxPayOrderNotifyResult result = wxPayCustomService.parseOrderNotifyResult(sb.toString());
		    paymentService.updateByWxPayOrderNotifyResult(result);
		    wxPayResponse = WxPayNotifyResponse.success("OK");
		    out.print(wxPayResponse);
		    
		} catch(WxPayException we) {
			log.error(Utils.getErrorMessage(we));
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		} finally {
			out.flush();
			out.close();
		}
        return null;
	}

	public String getWxPayResponse() {
		return wxPayResponse;
	}

	public void setWxPayResponse(String wxPayResponse) {
		this.wxPayResponse = wxPayResponse;
	}

	

}
