package com.tsingma.business.member.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.MemScratch;
import com.tsingma.common.dao.BaseDao;
import com.tsingma.core.util.Utils;

@Component("memScratchDao")
public class MemScratchDao extends BaseDao<MemScratch, Serializable> {

	/**
	 * 根据订单号查询唯一记录
	 * @param tradeNo
	 * @return
	 * @throws Exception
	 */
	public MemScratch loadByTradeNo(String tradeNo) throws Exception {
		return super.uniqueResult("tradeNo", tradeNo);
	}
	
	/**
	 * 根据openid查询会员最后刮奖结果
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public List<MemScratch> loadLastByOpenid(Integer actId, String openid) throws Exception {
		return super.listLimitByHql("from MemScratch m where m.actId = ?0 and m.openid = ?1 and m.isscratch = 0 order by m.createdTime desc", 1, actId, openid);
	}
	
	/**
	 * 根据openid查询会员刮奖次数
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public Integer loadCountByOpenid(Integer actId, String openid) throws Exception {
		return super.loadCountHql("select count(*) from MemScratch m where m.actId = ?0 and m.openid = ?1 and m.isscratch = 0", actId, openid);
	}
	
	/**
	 * 设置为已刮奖
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public void editAlreadyScratch(Integer id, String time) throws Exception {
		super.excuteHql("update MemScratch m set m.isscratch = 1,m.scratchTime = '" + time + "' where m.id = " + id);
	}
	
	/**
	 * 查询会员累计刮奖金额
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public Double loadPriceByOpenid(String openid) throws Exception {
		return super.loadPriceHql("select sum(m.price) from MemScratch m where m.openid = ?0 and m.isscratch = 1", openid);
	}
	
	/**
	 * 分页查询用户优惠券
	 * @param memCoupon
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<MemScratch> loadListPage(MemScratch memScratch, int start, int limit) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<MemScratch> query = builder.createQuery(MemScratch.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<MemScratch> root = query.from(MemScratch.class); 
        query.select(root);
        if(!Utils.isEmpty(memScratch.getActId()))
        	predicatesList.add(builder.equal(root.get("actId"), memScratch.getActId()));
        if(!Utils.isEmpty(memScratch.getIsscratch()))
        	predicatesList.add(builder.equal(root.get("isscratch"), memScratch.getIsscratch()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listPage(query, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(MemScratch memScratch) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<MemScratch> root = query.from(MemScratch.class);  
		query.select(builder.count(root));
		if(!Utils.isEmpty(memScratch.getActId()))
        	predicatesList.add(builder.equal(root.get("actId"), memScratch.getActId()));
        if(!Utils.isEmpty(memScratch.getIsscratch()))
        	predicatesList.add(builder.equal(root.get("isscratch"), memScratch.getIsscratch()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listCount(query);
	}
}
