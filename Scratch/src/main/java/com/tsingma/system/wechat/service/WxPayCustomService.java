package com.tsingma.system.wechat.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.binarywang.wxpay.bean.WxPayApiData;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponInfoQueryRequest;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponInfoQueryResult;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponSendRequest;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponSendResult;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponStockQueryRequest;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponStockQueryResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxScanPayNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayAuthcode2OpenidRequest;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderReverseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import com.github.binarywang.wxpay.bean.request.WxPaySendRedpackRequest;
import com.github.binarywang.wxpay.bean.request.WxPayShorturlRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayBillResult;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderReverseResult;
import com.github.binarywang.wxpay.bean.result.WxPayRedpackQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPaySendRedpackResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants.SignType;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.EntPayServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceApacheHttpImpl;
import com.github.binarywang.wxpay.util.SignUtils;

@Service("wxPayCustomService")
public class WxPayCustomService extends WxPayServiceApacheHttpImpl implements WxPayService {

	private static Logger log = Logger.getLogger("PLog");

	private EntPayService entPayService = new EntPayServiceImpl(this);

	@Autowired
	private WxPayService wxService;

	private WxPayConfig config;

	@PostConstruct
	public void init() {

		Properties props = new Properties();
		InputStream inputStream = null;
		try {
			System.out.println("Loading miniPay properties...");
			inputStream = getClass().getResourceAsStream("/wechat.properties");
			props.load(inputStream);

			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId((String) props.get("miniapp.appid"));
			payConfig.setMchId((String) props.get("miniapp.mchId"));
			payConfig.setMchKey((String) props.get("miniapp.mchKey"));
			payConfig.setTradeType((String) props.get("miniapp.tradeType"));
			payConfig.setNotifyUrl((String) props.get("miniapp.notifyUrl"));
			payConfig.setKeyPath((String) props.get("miniapp.keyPath"));

			this.setConfig(payConfig);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public WxPayOrderQueryResult queryOrder(String transactionId, String outTradeNo) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayOrderCloseResult closeOrder(String outTradeNo) {
		try {
			WxPayOrderCloseResult orderCloseResult = this.wxService.closeOrder(outTradeNo);
			return orderCloseResult;
		} catch (WxPayException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T createOrder(WxPayUnifiedOrderRequest request) throws WxPayException {
		WxPayUnifiedOrderResult unifiedOrderResult = this.unifiedOrder(request);
		String prepayId = unifiedOrderResult.getPrepayId();
		if (StringUtils.isBlank(prepayId)) {
			throw new RuntimeException(String.format("无法获取prepay id，错误代码： '%s'，信息：%s。", unifiedOrderResult.getErrCode(),
					unifiedOrderResult.getErrCodeDes()));
		}

		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonceStr = String.valueOf(System.currentTimeMillis());
		switch (request.getTradeType()) {
		case TradeType.NATIVE: {
			return (T) WxPayNativeOrderResult.builder().codeUrl(unifiedOrderResult.getCodeURL()).build();
		}

		case TradeType.APP: {
			// APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
			String appId = this.getConfig().getAppId();
			Map<String, String> configMap = new HashMap<>();
			// 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
			String partnerId = getConfig().getMchId();
			configMap.put("prepayid", prepayId);
			configMap.put("partnerid", partnerId);
			String packageValue = "Sign=WXPay";
			configMap.put("package", packageValue);
			configMap.put("timestamp", timestamp);
			configMap.put("noncestr", nonceStr);
			configMap.put("appid", appId);

			return (T) WxPayAppOrderResult.builder()
					.sign(SignUtils.createSign(configMap, null, this.getConfig().getMchKey(), false)).prepayId(prepayId)
					.partnerId(partnerId).appId(appId).packageValue(packageValue).timeStamp(timestamp)
					.nonceStr(nonceStr).build();
		}

		case TradeType.JSAPI: {
			String signType = SignType.MD5;
			WxPayMpOrderResult payResult = WxPayMpOrderResult.builder().appId(unifiedOrderResult.getAppid())
					.timeStamp(timestamp).nonceStr(nonceStr).packageValue("prepay_id=" + prepayId).signType(signType)
					.build();

			payResult.setPaySign(SignUtils.createSign(payResult, signType, this.getConfig().getMchKey(), false));
			return (T) payResult;
		}

		default: {
			throw new WxPayException("该交易类型暂不支持");
		}
		}
	}

	@Override
	public WxPayUnifiedOrderResult unifiedOrder(WxPayUnifiedOrderRequest request) throws WxPayException {
		// return this.wxService.unifiedOrder(request);
		request.checkAndSign(this.getConfig());

		String url = this.wxService.getPayBaseUrl() + "/pay/unifiedorder";
		String responseContent = this.post(url, request.toXML(), false);
		WxPayUnifiedOrderResult result = BaseWxPayResult.fromXML(responseContent, WxPayUnifiedOrderResult.class);
		result.checkResult(this, request.getSignType(), true);
		return result;
	}
	
	public EntPayResult entPay(EntPayRequest request) throws WxPayException {
	    return this.wxService.getEntPayService().entPay(request);
	}

	@Override
	public Map<String, String> getPayInfo(WxPayUnifiedOrderRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayRefundResult refund(WxPayRefundRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayRefundQueryResult refundQuery(String transactionId, String outTradeNo, String outRefundNo,
			String refundId) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayOrderNotifyResult getOrderNotifyResult(String xmlData) throws WxPayException {
		return this.parseOrderNotifyResult(xmlData);
	}

	@Override
	public WxPayOrderNotifyResult parseOrderNotifyResult(String xmlData) throws WxPayException {
		log.warn("微信支付异步通知请求参数：{" + xmlData + "}");
		WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlData);
		log.warn("微信支付异步通知请求解析后的对象：{" + result + "}");
		result.checkResult(this, this.getConfig().getSignType(), false);
		return result;
	}

	@Override
	public WxPayRefundNotifyResult parseRefundNotifyResult(String xmlData) throws WxPayException {
		log.warn("微信支付退款异步通知参数：{" + xmlData + "}");
		WxPayRefundNotifyResult result = WxPayRefundNotifyResult.fromXML(xmlData, this.getConfig().getMchKey());
		log.warn("微信支付退款异步通知解析后的对象：{" + result + "}");
		return result;
	}

	@Override
	public WxScanPayNotifyResult parseScanPayNotifyResult(String xmlData) throws WxPayException {
		log.warn("扫码支付回调通知请求参数：{" + xmlData + "}");
		WxScanPayNotifyResult result = BaseWxPayResult.fromXML(xmlData, WxScanPayNotifyResult.class);
		log.warn("扫码支付回调通知解析后的对象：{" + result + "}");
		result.checkResult(this, this.getConfig().getSignType(), false);
		return result;
	}

	@Override
	public WxPaySendRedpackResult sendRedpack(WxPaySendRedpackRequest request) throws WxPayException {
//		return this.wxService.sendRedpack(request);
		request.checkAndSign(this.getConfig());

	    String url = this.wxService.getPayBaseUrl() + "/mmpaymkttransfers/sendredpack";
	    if (request.getAmtType() != null) {
	      //裂变红包
	      url = this.wxService.getPayBaseUrl() + "/mmpaymkttransfers/sendgroupredpack";
	    }

	    String responseContent = this.post(url, request.toXML(), true);
	    //无需校验，因为没有返回签名信息
	    return BaseWxPayResult.fromXML(responseContent, WxPaySendRedpackResult.class);
	}

	@Override
	public WxPayRedpackQueryResult queryRedpack(String mchBillNo) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] createScanPayQrcodeMode1(String productId, File logoFile, Integer sideLength) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createScanPayQrcodeMode1(String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] createScanPayQrcodeMode2(String codeUrl, File logoFile, Integer sideLength) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void report(WxPayReportRequest request) throws WxPayException {
		// TODO Auto-generated method stub

	}

	@Override
	public WxPayBillResult downloadBill(String billDate, String billType, String tarType, String deviceInfo)
			throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayMicropayResult micropay(WxPayMicropayRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayOrderReverseResult reverseOrder(WxPayOrderReverseRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String shorturl(WxPayShorturlRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String shorturl(String longUrl) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String authcode2Openid(WxPayAuthcode2OpenidRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String authcode2Openid(String authCode) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSandboxSignKey() throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayCouponSendResult sendCoupon(WxPayCouponSendRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayCouponStockQueryResult queryCouponStock(WxPayCouponStockQueryRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayCouponInfoQueryResult queryCouponInfo(WxPayCouponInfoQueryRequest request) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayApiData getWxApiData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryComment(Date beginDate, Date endDate, Integer offset, Integer limit) throws WxPayException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WxPayConfig getConfig() {
		return this.config;
	}

	@Override
	public void setConfig(WxPayConfig config) {
		this.config = config;
	}
	
	@Override
	public EntPayService getEntPayService() {
		return entPayService;
	}

	@Override
	public void setEntPayService(EntPayService entPayService) {
		this.entPayService = entPayService;
	}

}
