package com.tsingma.business.scratch.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.tsingma.business.member.dao.MemScratchDao;
import com.tsingma.business.member.model.MemScratch;
import com.tsingma.business.scratch.dao.EntPaymentDao;
import com.tsingma.business.scratch.model.EntPayment;
import com.tsingma.common.exception.TsingmaException;
import com.tsingma.core.util.Utils;

@Service("entPaymentService")
@Transactional
public class EntPaymentService {

	@Autowired
	private EntPaymentDao entPaymentDao;
	@Autowired
	private MemScratchDao memScratchDao;
	
	/**
	 * 根据订单号查询唯一付款信息
	 * @param tradeNo
	 * @return
	 * @throws Exception
	 */
	public EntPayment getByTradeNo(String tradeNo) throws Exception {
		return entPaymentDao.loadByTradeNo(tradeNo);
	}
	
	
	/**
	 * 根据付款结果创建付款信息
	 * @param request
	 * @throws Exception
	 */
	public void insertByEntPayResult(EntPayResult result) throws Exception {
		if(!Utils.isEmpty(result)){
			
			if("SUCCESS".equals(result.getReturnCode())){
				EntPayment payment = new EntPayment();
				payment.setAppid(result.getMchAppid());
				payment.setMchId(result.getMchId());
				payment.setResultCode(result.getResultCode());
				payment.setErrorCode(result.getErrCode());
				payment.setErrorMsg(result.getErrCodeDes());
				payment.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				if("SUCCESS".equals(result.getResultCode())){
					payment.setTradeNo(result.getPartnerTradeNo());
					payment.setPaymentNo(result.getPaymentNo());
					payment.setPaymentTime(result.getPaymentTime());
					
					MemScratch memScratch = memScratchDao.loadByTradeNo(result.getPartnerTradeNo());
					if(!Utils.isEmpty(memScratch)){
						memScratch.setStatus(1);
						memScratchDao.merge(memScratch);
					}
				}
				this.insert(payment);
			} else
				throw new TsingmaException(result.getReturnMsg());
		}
	}
	
	
	public void insert(EntPayment payament) throws Exception {
		entPaymentDao.save(payament);
	}
	
	public void update(EntPayment payament) throws Exception {
		entPaymentDao.update(payament);
	}
	
	public void merge(EntPayment payament) throws Exception {
		entPaymentDao.merge(payament);
	}
	
	public void delete(EntPayment payament) throws Exception {
		entPaymentDao.deleteObject(payament);
	}
}
