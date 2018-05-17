package com.tsingma.business.scratch.dao;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.tsingma.business.scratch.model.EntPayment;
import com.tsingma.common.dao.BaseDao;

@Component("entPaymentDao")
public class EntPaymentDao extends BaseDao<EntPayment, Serializable> {

	/**
	 * 根据订单号查询唯一付款信息
	 * @param tradeNo
	 * @return
	 * @throws Exception
	 */
	public EntPayment loadByTradeNo(String tradeNo) throws Exception {
		return super.uniqueResultByHql("from EntPayment p where p.tradeNo = ?0", tradeNo);
	}
}
