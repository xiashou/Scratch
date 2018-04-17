package com.tsingma.business.member.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.Member;
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
	
	
	public String queryMemberList() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				if(Utils.isEmpty(member))
					member = new Member();
				member.setAppid(super.getWxMaConfig().getAppid());
				memberList = memberService.getListPage(member, this.getStart(), this.getLimit());
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

}
