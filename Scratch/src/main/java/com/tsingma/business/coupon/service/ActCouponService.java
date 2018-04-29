package com.tsingma.business.coupon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.coupon.dao.ActCouponDao;
import com.tsingma.business.coupon.dao.CouponDao;
import com.tsingma.business.coupon.model.ActCoupon;
import com.tsingma.core.util.Utils;

@Service("actCouponService")
@Transactional
public class ActCouponService {

	@Autowired
	private ActCouponDao actCouponDao;
	@Autowired
	private CouponDao couponDao;
	
	/**
	 * 分页查询优惠券
	 * @param coupon
	 * @return
	 * @throws Exception
	 */
	public List<ActCoupon> getListPage(ActCoupon coupon, int start, int limit) throws Exception {
		List<ActCoupon> list = actCouponDao.loadListPage(coupon, start, limit);
		for(ActCoupon actCoupon : list){
			actCoupon.setCouponName(couponDao.get(actCoupon.getCouponId()).getName());
		}
		return list;
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long getListCount(ActCoupon coupon) throws Exception {
		return actCouponDao.loadListCount(coupon);
	}
	
	public ActCoupon getById(Integer id) throws Exception {
		return actCouponDao.get(id);
	}

	public void insert(ActCoupon coupon) throws Exception {
		ActCoupon exist = actCouponDao.loadByCondition(coupon.getActId(), coupon.getCouponId(), coupon.getEndDate());
		if(!Utils.isEmpty(exist)){
			actCouponDao.editNumber(coupon.getActId(), coupon.getCouponId(), coupon.getEndDate(), coupon.getNumber());
		} else {
			actCouponDao.save(coupon);
		}
		couponDao.editNumber(coupon.getCouponId(), coupon.getNumber() * -1);
	}
	
	public void update(ActCoupon coupon) throws Exception {
		actCouponDao.update(coupon);
	}
	
	public void merge(ActCoupon coupon) throws Exception {
		actCouponDao.merge(coupon);
	}
	
	public void delete(ActCoupon coupon) throws Exception {
		actCouponDao.deleteObject(coupon);
	}
}
