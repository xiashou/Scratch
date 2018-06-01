package com.tsingma.business.member.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.Member;
import com.tsingma.business.member.model.Ranking;
import com.tsingma.business.member.model.Record;
import com.tsingma.business.member.service.MemberService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("MemberAction")
public class MemberAction extends BaseAction {
	
	private static final long serialVersionUID = 4588740724635168769L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private MemberService memberService;
	
	private Member member;
	private List<Member> memberList;
	private List<Record> recordList;
	private List<Ranking> rankingList;
	
	private String openid;
	private String appid;
	
	
	public String queryMemberList() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				if(Utils.isEmpty(member))
					member = new Member();
				member.setAppid(super.getWxMaConfig().getAppid());
				this.setTotalCount(memberService.getListCount(member));
				memberList = memberService.getListPage(member, this.getStart(), this.getLimit());
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String queryRecordListByOpenid() {
		try {
			if (!Utils.isEmpty(openid)) {
				recordList = memberService.getRecordByOpenid(openid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String queryRankingByAppid() {
		try {
			if (!Utils.isEmpty(appid)) {
				rankingList = memberService.getRankingByAppid(appid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public List<Member> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}
	public List<Record> getRecordList() {
		return recordList;
	}
	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public List<Ranking> getRankingList() {
		return rankingList;
	}
	public void setRankingList(List<Ranking> rankingList) {
		this.rankingList = rankingList;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}

}
