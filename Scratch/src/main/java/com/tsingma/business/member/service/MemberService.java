package com.tsingma.business.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.member.dao.MemberDao;
import com.tsingma.business.member.dao.RankingDao;
import com.tsingma.business.member.dao.RecordDao;
import com.tsingma.business.member.model.Member;
import com.tsingma.business.member.model.Ranking;
import com.tsingma.business.member.model.Record;
import com.tsingma.core.util.Utils;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

@Service("memberService")
@Transactional
public class MemberService {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private RecordDao recordDao;
	@Autowired
	private RankingDao rankingDao;
	
	/**
	 * 根据openId检查会员是否存在
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	public Boolean checkMemberExist(String openId) throws Exception {
		Member exist = memberDao.loadByOpenId(openId);
		if(Utils.isEmpty(exist))
			return false;
		else
			return true;
	}
	
	public List<Record> getRecordByOpenid(String openid) throws Exception {
		return recordDao.loadListByOpenid(openid);
	}
	
	public List<Ranking> getRankingByAppid(String appid) throws Exception {
		return rankingDao.loadRankingByAppid(appid);
	}
	
	public List<Member> getListPage(Member member, int start, int limit) throws Exception {
		return memberDao.loadListPage(member, start, limit);
	}
	
	public void insert(Member member) throws Exception {
		memberDao.save(member);
	}
	
	public void insert(WxMaUserInfo userInfo) throws Exception {
		memberDao.save(transfer(userInfo));
	}
	
	public Member transfer(WxMaUserInfo userInfo) {
		return new Member(userInfo.getUnionId(), userInfo.getWatermark().getAppid(), userInfo.getOpenId(), 
				userInfo.getNickName().replaceAll("'", "''"), userInfo.getGender(), userInfo.getLanguage(), userInfo.getCity(), 
				userInfo.getProvince(), userInfo.getCountry(), userInfo.getAvatarUrl(), userInfo.getWatermark().getTimestamp());
	}
}
