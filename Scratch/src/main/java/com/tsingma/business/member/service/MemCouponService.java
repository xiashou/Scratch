package com.tsingma.business.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.coupon.dao.ActCouponDao;
import com.tsingma.business.coupon.dao.CouponDao;
import com.tsingma.business.coupon.model.ActCoupon;
import com.tsingma.business.member.dao.MemCouponDao;
import com.tsingma.business.member.dao.MemberDao;
import com.tsingma.business.member.model.MemCoupon;
import com.tsingma.business.member.model.Member;

@Service("memCouponService")
@Transactional
public class MemCouponService {

	@Autowired
	private MemCouponDao memCouponDao;
	@Autowired
	private ActCouponDao actCouponDao;
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private MemberDao memberDao;
	
	
	/**
	 * 根据openid查询会员所有优惠券
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<MemCoupon> getListByOpenid(Integer actId, String openid) throws Exception {
		List<MemCoupon> list = memCouponDao.loadListByOpenid(actId, openid);
		for(MemCoupon memCoupon : list){
			ActCoupon actCoupon = actCouponDao.get(memCoupon.getActcouponId());
			memCoupon.setCoupon(couponDao.get(actCoupon.getCouponId()));
		}
		return list;
	}
	
	/**
	 * 根据openid查询会员最后三张优惠券
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<MemCoupon> getList3ByOpenid(Integer actId, String openid) throws Exception {
		List<MemCoupon> list = memCouponDao.loadList3ByOpenid(actId, openid, 3);
		for(MemCoupon memCoupon : list){
			ActCoupon actCoupon = actCouponDao.get(memCoupon.getActcouponId());
			memCoupon.setCoupon(couponDao.get(actCoupon.getCouponId()));
		}
		return list;
	}
	
	public List<MemCoupon> getListPage(MemCoupon mCoupon, int start, int limit) throws Exception {
		List<MemCoupon> list = memCouponDao.loadListPage(mCoupon, start, limit);
		for(MemCoupon memCoupon : list){
			ActCoupon actCoupon = actCouponDao.get(memCoupon.getActcouponId());
			memCoupon.setCoupon(couponDao.get(actCoupon.getCouponId()));
			Member member = memberDao.loadByOpenId(memCoupon.getOpenid());
			memCoupon.setMember(member);
		}
		return list;
	}
	
	public Long getListCount(MemCoupon memCoupon) throws Exception {
		return memCouponDao.loadListCount(memCoupon);
	}
}
