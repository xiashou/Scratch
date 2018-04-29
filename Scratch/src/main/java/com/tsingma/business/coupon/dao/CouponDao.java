package com.tsingma.business.coupon.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tsingma.business.coupon.model.Coupon;
import com.tsingma.common.dao.BaseDao;

@Component("couponDao")
public class CouponDao extends BaseDao<Coupon, Serializable> {

	/**
	 * 更新优惠券数量
	 * @param id
	 * @param number
	 * @throws Exception
	 */
	public void editNumber(Integer id, Integer number) throws Exception {
		super.excuteHql("update Coupon c set c.number = c.number + " + number + " where c.id = " + id);
	}
	
	/**
	 * 分页查询优惠券
	 * @param coupon
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Coupon> loadListPage(Coupon coupon, int start, int limit) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Coupon> query = builder.createQuery(Coupon.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<Coupon> root = query.from(Coupon.class); 
        query.select(root);
        predicatesList.add(builder.equal(root.get("storeId"), coupon.getStoreId()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listPage(query, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(Coupon coupon) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<Coupon> root = query.from(Coupon.class);  
		query.select(builder.count(root));
		predicatesList.add(builder.equal(root.get("storeId"), coupon.getStoreId()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listCount(query);
	}
}
