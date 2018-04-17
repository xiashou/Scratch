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
	
	public List<Member> loadListPage(Member member, int start, int limit) throws Exception {
		return super.listPage(connectionCriteria(member), start, limit);
	}
	
	private CriteriaQuery<Member> connectionCriteria(Member member){
		CriteriaBuilder crb = super.getSession().getCriteriaBuilder();  
        CriteriaQuery<Member> crq = crb.createQuery(Member.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<Member> root=crq.from(Member.class);  
        crq.select(root);
        predicatesList.add(crb.equal(root.get("appid"), member.getAppid()));
        if(!Utils.isEmpty(member.getNickName()))
        	predicatesList.add(crb.equal(root.get("nickName"), member.getNickName()));
        crq.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
        return crq;
	}
}
