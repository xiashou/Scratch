package com.tsingma.business.coupon.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.coupon.model.ActCoupon;
import com.tsingma.business.coupon.service.ActCouponService;
import com.tsingma.business.scratch.model.Activity;
import com.tsingma.business.scratch.service.ActivityService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("ActCouponAction")
public class ActCouponAction extends BaseAction {

	private static final long serialVersionUID = 7847593935988661013L;
	private static Logger log = Logger.getLogger("SLog");

	@Autowired
	private ActCouponService actCouponService;
	@Autowired
	private ActivityService activityService;
	
	private ActCoupon actCoupon;
	private List<ActCoupon> actCouponList;
	
	
	/**
	 * 分页查询优惠券
	 * @return
	 * @throws Exception
	 */
	public String queryListPage() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				if(Utils.isEmpty(actCoupon))
					actCoupon = new ActCoupon();
				Activity act = activityService.getEnableActivity(super.getWxMaConfig().getAppid());
				if(!Utils.isEmpty(act)){
					actCoupon.setActId(act.getId());
					this.setTotalCount(actCouponService.getListCount(actCoupon));
					actCouponList = actCouponService.getListPage(actCoupon, this.getStart(), this.getLimit());
				}
			}
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertActCoupon() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				Activity act = activityService.getEnableActivity(super.getWxMaConfig().getAppid());
				if(!Utils.isEmpty(actCoupon)){
					actCoupon.setActId(act.getId());
					actCoupon.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
					actCouponService.insert(actCoupon);
					this.setResult(true, "添加成功！");
				} else
					this.setResult(false, "参数不能为空！");
			}
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateActCoupon() {
		try {
			if(!Utils.isEmpty(actCoupon) && !Utils.isEmpty(actCoupon.getId())){
				ActCoupon exist = actCouponService.getById(actCoupon.getId());
				exist.setEndDate(actCoupon.getEndDate());
				exist.setNumber(actCoupon.getNumber());
				actCouponService.merge(exist);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteActCoupon() {
		try {
			if(!Utils.isEmpty(actCoupon)){
				actCouponService.delete(actCoupon);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}

	public ActCoupon getActCoupon() {
		return actCoupon;
	}
	public void setActCoupon(ActCoupon actCoupon) {
		this.actCoupon = actCoupon;
	}
	public List<ActCoupon> getActCouponList() {
		return actCouponList;
	}
	public void setActCouponList(List<ActCoupon> actCouponList) {
		this.actCouponList = actCouponList;
	}
	
}
