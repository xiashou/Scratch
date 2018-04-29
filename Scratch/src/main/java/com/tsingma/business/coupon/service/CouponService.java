package com.tsingma.business.coupon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.coupon.dao.CouponDao;
import com.tsingma.business.coupon.model.Coupon;

@Service("couponService")
@Transactional
public class CouponService {

	@Autowired
	private CouponDao couponDao;
	
	/**
	 * 分页查询优惠券
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public List<Coupon> getListPage(Coupon coupon, int start, int limit) throws Exception {
		return couponDao.loadListPage(coupon, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long getListCount(Coupon coupon) throws Exception {
		return couponDao.loadListCount(coupon);
	}
	
	public Coupon getById(Integer id) throws Exception {
		return couponDao.get(id);
	}

	public void insert(Coupon coupon) throws Exception {
		couponDao.save(coupon);
	}
	
	public void update(Coupon coupon) throws Exception {
		couponDao.update(coupon);
	}
	
	public void merge(Coupon coupon) throws Exception {
		couponDao.merge(coupon);
	}
	
	public void delete(Coupon coupon) throws Exception {
		couponDao.deleteObject(coupon);
	}
}
