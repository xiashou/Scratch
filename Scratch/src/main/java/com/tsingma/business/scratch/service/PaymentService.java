package com.tsingma.business.scratch.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.tsingma.business.scratch.dao.PaymentDao;
import com.tsingma.business.scratch.model.Payment;
import com.tsingma.core.util.Utils;

@Service("paymentService")
@Transactional
public class PaymentService {

	@Autowired
	private PaymentDao paymentDao;
	
	/**
	 * 根据订单号查询唯一付款信息
	 * @param outTradeNo
	 * @return
	 * @throws Exception
	 */
	public Payment getByOurTradeNo(String outTradeNo) throws Exception {
		return paymentDao.loadByOutTradeNo(outTradeNo);
	}
	
	/**
	 * 根据请求参数创建付款信息
	 * @param request
	 * @throws Exception
	 */
	public void insertByOrderRequest(WxPayUnifiedOrderRequest request) throws Exception {
		if(!Utils.isEmpty(request)){
			Payment payment = new Payment();
			payment.setOutTradeNo(request.getOutTradeNo());
			payment.setAttach(Integer.parseInt(request.getAttach()));
			payment.setProductName(request.getBody());
			payment.setClientIp(request.getSpbillCreateIp());
			payment.setPrice(request.getTotalFee());
			payment.setOpenid(request.getOpenid());
			payment.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			this.insert(payment);
		}
	}
	
	/**
	 * 根据返回结果更新付款信息
	 * @param result
	 * @throws Exception
	 */
	public void updateByWxPayOrderNotifyResult(WxPayOrderNotifyResult result) throws Exception {
		if(!Utils.isEmpty(result) && "SUCCESS".equals(result.getReturnCode())){
			Payment payment = paymentDao.loadByOutTradeNo(result.getOutTradeNo());
			if(!Utils.isEmpty(payment)){
				payment.setAppid(result.getAppid());
				payment.setAttach(Integer.parseInt(result.getAttach()));
				payment.setBankType(result.getBankType());
				payment.setErrorCode(result.getErrCode());
				payment.setErrorMsg(result.getErrCodeDes());
				payment.setFeeType(result.getFeeType());
				payment.setIsSubscribe(result.getIsSubscribe());
				payment.setMchId(result.getMchId());
				payment.setNotifyTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				payment.setResultCode(result.getResultCode());
				payment.setTimeEnd(result.getTimeEnd());
				payment.setTotalFee(result.getTotalFee());
				payment.setTradeType(result.getTradeType());
				payment.setTransactionId(result.getTransactionId());
				this.update(payment);
			} else {
				payment = new Payment();
				payment.setOutTradeNo(result.getOutTradeNo());
				payment.setPrice(result.getTotalFee());
				payment.setOpenid(result.getOpenid());
				payment.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				
				payment.setAppid(result.getAppid());
				payment.setAttach(Integer.parseInt(result.getAttach()));
				payment.setBankType(result.getBankType());
				payment.setErrorCode(result.getErrCode());
				payment.setErrorMsg(result.getErrCodeDes());
				payment.setFeeType(result.getFeeType());
				payment.setIsSubscribe(result.getIsSubscribe());
				payment.setMchId(result.getMchId());
				payment.setNotifyTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				payment.setResultCode(result.getResultCode());
				payment.setTimeEnd(result.getTimeEnd());
				payment.setTotalFee(result.getTotalFee());
				payment.setTradeType(result.getTradeType());
				payment.setTransactionId(result.getTransactionId());
				this.insert(payment);
			}
		}
	}
	
	
	public void insert(Payment payament) throws Exception {
		paymentDao.save(payament);
	}
	
	public void update(Payment payament) throws Exception {
		paymentDao.update(payament);
	}
	
	public void merge(Payment payament) throws Exception {
		paymentDao.merge(payament);
	}
	
	public void delete(Payment payament) throws Exception {
		paymentDao.deleteObject(payament);
	}
}
