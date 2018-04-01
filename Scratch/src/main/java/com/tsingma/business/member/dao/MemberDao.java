package com.tsingma.business.member.dao;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.Member;
import com.tsingma.common.dao.BaseDao;

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
}
