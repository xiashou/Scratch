package com.tsingma.business.scratch.dao;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.tsingma.business.scratch.model.Payment;
import com.tsingma.common.dao.BaseDao;

@Component("paymentDao")
public class PaymentDao extends BaseDao<Payment, Serializable> {

	/**
	 * 根据订单号查询唯一付款信息
	 * @param outTradeNo
	 * @return
	 * @throws Exception
	 */
	public Payment loadByOutTradeNo(String outTradeNo) throws Exception {
		return super.uniqueResultByHql("from Payment p where p.outTradeNo = ?0", outTradeNo);
	}
}
