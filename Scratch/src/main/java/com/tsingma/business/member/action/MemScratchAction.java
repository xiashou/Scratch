package com.tsingma.business.member.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.member.model.MemScratch;
import com.tsingma.business.member.service.MemScratchService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("MemScratchAction")
public class MemScratchAction extends BaseAction {

	private static final long serialVersionUID = -3882333044784424990L;
	private static Logger log = Logger.getLogger("SLog");

	@Autowired
	private MemScratchService memScratchService;
	
	private List<MemScratch> memScratchList;
	private MemScratch memScratch;
	
	private String openid;
	private Integer actId;
	private Integer times;
	
	private Double price;
	
	public String queryListByOpenid() {
		try {
			if (!Utils.isEmpty(openid) && !Utils.isEmpty(actId)) {
				memScratch = memScratchService.getLastByOpenid(actId, openid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String queryCountByOpenid() {
		try {
			if (!Utils.isEmpty(openid) && !Utils.isEmpty(actId)) {
				times = memScratchService.getCountByOpenid(actId, openid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String queryPriceByOpenid() {
		try {
			if (!Utils.isEmpty(openid)) {
				price = memScratchService.getPriceByOpenid(openid);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 设置为已刮奖
	 * 小程序端
	 * @return
	 */
	public String alreadyScratch() {
		try {
			if (!Utils.isEmpty(memScratch) && !Utils.isEmpty(memScratch.getId())) {
				memScratchService.updateAlreadyScratch(memScratch.getId());
				this.setResult(true, "");
	        } else
	        	this.setResult(false, "参数不能为空");
		} catch(Exception e) {
			this.setResult(false, "");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}

	public List<MemScratch> getMemScratchList() {
		return memScratchList;
	}
	public void setMemScratchList(List<MemScratch> memScratchList) {
		this.memScratchList = memScratchList;
	}
	public MemScratch getMemScratch() {
		return memScratch;
	}
	public void setMemScratch(MemScratch memScratch) {
		this.memScratch = memScratch;
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
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
