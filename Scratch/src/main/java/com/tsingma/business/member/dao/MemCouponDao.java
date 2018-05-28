package com.tsingma.business.member.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.MemCoupon;
import com.tsingma.common.dao.BaseDao;
import com.tsingma.core.util.Utils;

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
	
	/**
	 * 分页查询用户优惠券
	 * @param memCoupon
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<MemCoupon> loadListPage(MemCoupon memCoupon, int start, int limit) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<MemCoupon> query = builder.createQuery(MemCoupon.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<MemCoupon> root = query.from(MemCoupon.class); 
        query.select(root);
        if(!Utils.isEmpty(memCoupon.getCode()))
        	predicatesList.add(builder.equal(root.get("code"), memCoupon.getCode()));
        if(!Utils.isEmpty(memCoupon.getEndDate()))
        	predicatesList.add(builder.equal(root.get("endDate"), memCoupon.getEndDate()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listPage(query, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(MemCoupon memCoupon) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<MemCoupon> root = query.from(MemCoupon.class);  
		query.select(builder.count(root));
		if(!Utils.isEmpty(memCoupon.getCode()))
        	predicatesList.add(builder.equal(root.get("code"), memCoupon.getCode()));
        if(!Utils.isEmpty(memCoupon.getEndDate()))
        	predicatesList.add(builder.equal(root.get("endDate"), memCoupon.getEndDate()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listCount(query);
	}
	
	
}
