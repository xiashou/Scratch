package com.tsingma.business.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsingma.business.member.dao.MemberDao;
import com.tsingma.business.member.model.Member;
import com.tsingma.core.util.Utils;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

@Service("memberService")
public class MemberService {

	@Autowired
	private MemberDao memberDao;
	
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
	
	public void insert(Member member) throws Exception {
		memberDao.save(member);
	}
	
	public void insert(WxMaUserInfo userInfo) throws Exception {
		memberDao.save(trsfer(userInfo));
	}
	
	public Member trsfer(WxMaUserInfo userInfo) {
		return new Member(userInfo.getUnionId(), userInfo.getWatermark().getAppid(), userInfo.getOpenId(), 
				userInfo.getNickName(), userInfo.getGender(), userInfo.getLanguage(), userInfo.getCity(), 
				userInfo.getProvince(), userInfo.getCountry(), userInfo.getAvatarUrl(), userInfo.getWatermark().getTimestamp());
	}
}
