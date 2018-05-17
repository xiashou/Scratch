package com.tsingma.business.member.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.MemCoupon;
import com.tsingma.common.dao.BaseDao;

@Component("memCouponDao")
public class MemCouponDao extends BaseDao<MemCoupon, Serializable> {

	/**
	 * 根据openid查询会员所有优惠券
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<MemCoupon> loadListByOpenid(Integer actId, String openid) throws Exception {
		return super.listByHql("from MemCoupon m where m.openid = ?1 and m.actcouponId in (select a.id from ActCoupon a where a.actId = ?0) and m.status = 0 order by m.createdTime desc", actId, openid);
	}
	
	/**
	 * 根据openid查询会员最后三张优惠券
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<MemCoupon> loadList3ByOpenid(Integer actId, String openid, Integer limit) throws Exception {
		return super.listLimitByHql("from MemCoupon m where m.openid = ?1 and m.actcouponId in (select a.id from ActCoupon a where a.actId = ?0) and m.status = 0 order by m.createdTime desc", limit, actId, openid);
	}
	
	
}
