package com.tsingma.business.member.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.Ranking;
import com.tsingma.common.dao.BaseDao;

@Component("rankingDao")
public class RankingDao extends BaseDao<Ranking, Serializable> {

	public List<Ranking> loadRankingByAppid(String appid) throws Exception {
		return super.listBySql2("select m.openId,m.avatarUrl,m.nickName,p.total FROM b_member m,"
				+ "(SELECT s.openid, SUM(s.price) AS total FROM b_memscratch s WHERE s.isscratch = 1 GROUP BY s.openid ORDER BY total DESC) p "
				+ "WHERE m.appid = ?0 AND m.openId = p.openid", appid);
	}
}
