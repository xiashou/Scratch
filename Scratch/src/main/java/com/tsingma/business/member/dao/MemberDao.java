package com.tsingma.business.member.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.Member;
import com.tsingma.common.dao.BaseDao;
import com.tsingma.core.util.Utils;

@Component("memberDao")
public class MemberDao extends BaseDao<Member, Serializable> {

	/**
	 * 根据openId查找唯一会员
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	public Member loadByOpenId(String openId) throws Exception {
		return super.uniqueResult("openId", openId);
	}
	
	/**
	 * 分页查询会员
	 * @param member
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Member> loadListPage(Member member, int start, int limit) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Member> query = builder.createQuery(Member.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<Member> root = query.from(Member.class); 
        query.select(root);
        if(!Utils.isEmpty(member.getNickName()))
        	predicatesList.add(builder.equal(root.get("nickName"), member.getNickName()));
        if(!Utils.isEmpty(member.getGender()))
        	predicatesList.add(builder.equal(root.get("gender"), member.getGender()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listPage(query, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(Member member) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<Member> root = query.from(Member.class);  
		query.select(builder.count(root));
		if(!Utils.isEmpty(member.getNickName()))
        	predicatesList.add(builder.equal(root.get("nickName"), member.getNickName()));
        if(!Utils.isEmpty(member.getGender()))
        	predicatesList.add(builder.equal(root.get("gender"), member.getGender()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listCount(query);
	}
	
}
