package com.tsingma.business.scratch.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.tsingma.business.scratch.model.Activity;
import com.tsingma.business.scratch.model.Scratch;
import com.tsingma.business.scratch.model.ScratchText;
import com.tsingma.business.scratch.service.ActivityService;
import com.tsingma.business.scratch.service.EntPaymentService;
import com.tsingma.business.scratch.service.ScratchService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;
import com.tsingma.system.wechat.service.WxPayCustomService;

@Scope("prototype")
@Component("ScratchAction")
public class ScratchAction extends BaseAction {

	private static final long serialVersionUID = -8719205598234547814L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private ScratchService scratchService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private EntPaymentService entPaymentService;
	@Autowired
	private WxPayCustomService wxPayCustomService;
	
	private List<Scratch> scratchList;
	private Scratch scratch;
	private ScratchText scratchText;
	
	private EntPayRequest entPay;
	
	/**
	 * 查询刮奖设置列表
	 * @return
	 */
	public String queryScratchList() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				scratchList = scratchService.getListByAppid(super.getWxMaConfig().getAppid());
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String queryScratchText() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				Activity act = activityService.getEnableActivity(super.getWxMaConfig().getAppid());
				scratchText = scratchService.getTextByActId(act.getId());
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 现金转账
	 * @return
	 */
	public String grantScratch() {
		try {
			if (!Utils.isEmpty(entPay)) {
				entPay.setDescription("奖金");
//				entPay.setMchAppid(appid);
//				entPay.setAmount(amount);
//				entPay.setOpenid(openid);
//				entPay.setPartnerTradeNo(tradeNo);
				entPay.setSpbillCreateIp(this.getRequest().getRemoteAddr());
				entPay.setCheckName("NO_CHECK");
//				EntPayResult result = wxPayCustomService.entPay(entPay);
//				if(!Utils.isEmpty(result)){
//					if("SUCCESS".equals(result.getReturnCode())){
//						if("SUCCESS".equals(result.getResultCode())){
//							this.setResult(true, "支付成功");
//						} else
//							this.setResult(false, result.getErrCodeDes());
//					} else
//						this.setResult(false, result.getReturnMsg());
//					entPaymentService.insertByEntPayResult(result);
//				} else
					this.setResult(false, "未开通");
	        } 
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertScratch() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				Activity act = activityService.getEnableActivity(super.getWxMaConfig().getAppid());
				if(!Utils.isEmpty(scratch)){
					scratch.setActId(act.getId());
					scratch.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
					scratchService.insert(scratch);
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
	
	public String updateScratch() {
		try {
			if(!Utils.isEmpty(scratch) && !Utils.isEmpty(scratch.getId())){
				Scratch exist = scratchService.getById(scratch.getId());
				exist.setName(scratch.getName());
				exist.setPrice(scratch.getPrice());
				exist.setProbability(scratch.getProbability());
				scratchService.merge(exist);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteScratch() {
		try {
			if(!Utils.isEmpty(scratch) && !Utils.isEmpty(scratch.getId())){
				scratchService.delete(scratch);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateTextScratch() {
		try {
			if(!Utils.isEmpty(scratchText)){
				if(!Utils.isEmpty(scratchText.getId())){
					ScratchText exist = scratchService.getTextById(scratchText.getId());
					exist.setName(scratchText.getName());
					exist.setDescrib(scratchText.getDescrib());
					scratchService.mergeText(exist);
				} else {
					Activity act = activityService.getEnableActivity(super.getWxMaConfig().getAppid());
					scratchText.setActId(act.getId());
					scratchText.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
					scratchService.insertText(scratchText);
				}
				this.setResult(true, "操作成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	
	public List<Scratch> getScratchList() {
		return scratchList;
	}
	public void setScratchList(List<Scratch> scratchList) {
		this.scratchList = scratchList;
	}
	public Scratch getScratch() {
		return scratch;
	}
	public void setScratch(Scratch scratch) {
		this.scratch = scratch;
	}
	public ScratchText getScratchText() {
		return scratchText;
	}
	public void setScratchText(ScratchText scratchText) {
		this.scratchText = scratchText;
	}
	public EntPayRequest getEntPay() {
		return entPay;
	}
	public void setEntPay(EntPayRequest entPay) {
		this.entPay = entPay;
	}
	
	
}
