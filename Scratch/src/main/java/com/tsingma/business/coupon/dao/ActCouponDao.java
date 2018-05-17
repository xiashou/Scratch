package com.tsingma.business.coupon.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tsingma.business.coupon.model.ActCoupon;
import com.tsingma.common.dao.BaseDao;

@Component("actCouponDao")
public class ActCouponDao extends BaseDao<ActCoupon, Serializable> {
	
	/**
	 * 根据条件查询唯一值
	 * @param actId
	 * @param couponId
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public ActCoupon loadByCondition(Integer actId, Integer couponId, String endDate) throws Exception {
		return super.uniqueResultByHql("from ActCoupon a where a.actId = " + actId + " and a.couponId = " + couponId + " and a.endDate = '" + endDate + "'");
	}
	
	/**
	 * 查询活动下所有优惠券
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public List<ActCoupon> loadListByActId(Integer actId) throws Exception {
		return super.listByHql("from ActCoupon a where a.actId = " + actId);
	}
	
	/**
	 * 获取活动的优惠券总数
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public Integer loadSumByActId(Integer actId) throws Exception {
		return super.loadCountHql("select sum(a.number) from ActCoupon a where a.actId = " + actId);
	}
	
	/**
	 * 更新活动优惠券的抽中概率
	 * @param sumNumber
	 * @param actId
	 * @throws Exception
	 */
	public void editProbability(Integer sumNumber, Integer actId) throws Exception {
		super.excuteHql("update ActCoupon a set a.probability = ROUND(a.number*100/" + sumNumber + ") where a.actId = " + actId);
	}
	
	/**
	 * 更新活动优惠券数量
	 * @param actId
	 * @param couponId
	 * @param endDate
	 * @param number
	 * @throws Exception
	 */
	public void editNumber(Integer actId, Integer couponId, String endDate, Integer number) throws Exception {
		super.excuteHql("update ActCoupon a set a.number = a.number + " + number + " where a.actId = " + actId + " and a.couponId = " + couponId + " and a.endDate = '" + endDate + "'");
	}

	/**
	 * 分页查询优惠券
	 * @param coupon
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<ActCoupon> loadListPage(ActCoupon coupon, int start, int limit) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<ActCoupon> query = builder.createQuery(ActCoupon.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<ActCoupon> root = query.from(ActCoupon.class); 
        query.select(root);
        predicatesList.add(builder.equal(root.get("actId"), coupon.getActId()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listPage(query, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(ActCoupon coupon) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<ActCoupon> root = query.from(ActCoupon.class);  
		query.select(builder.count(root));
		predicatesList.add(builder.equal(root.get("actId"), coupon.getActId()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listCount(query);
	}
}
