package com.tsingma.business.member.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.MemCoupon;
import com.tsingma.business.member.service.MemCouponService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("MemCouponAction")
public class MemCouponAction extends BaseAction {

	private static final long serialVersionUID = 4588740724635168769L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private MemCouponService memCouponService;
	
	private List<MemCoupon> memCouponList;
	private MemCoupon memCoupon;
	
	private String openid;
	private Integer actId;
	
	/**
	 * 查询会员所有优惠券记录
	 * @return
	 */
	public String queryListByOpenid() {
		try {
			if (!Utils.isEmpty(openid) && !Utils.isEmpty(actId)) {
				memCouponList = memCouponService.getListByOpenid(actId, openid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询会员最后三条优惠券记录
	 * @return
	 */
	public String queryListThreeByOpenid() {
		try {
			if (!Utils.isEmpty(openid) && !Utils.isEmpty(actId)) {
				memCouponList = memCouponService.getList3ByOpenid(actId, openid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	
	public List<MemCoupon> getMemCouponList() {
		return memCouponList;
	}
	public void setMemCouponList(List<MemCoupon> memCouponList) {
		this.memCouponList = memCouponList;
	}
	public MemCoupon getMemCoupon() {
		return memCoupon;
	}
	public void setMemCoupon(MemCoupon memCoupon) {
		this.memCoupon = memCoupon;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getActId() {
		return actId;
	}
	public void setActId(Integer actId) {
		this.actId = actId;
	}
	
}
