package com.tsingma.business.setting.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("SettingAction")
public class SettingAction extends BaseAction {

	private static final long serialVersionUID = 745570317899076173L;
	private static Logger log = Logger.getLogger("SLog");
	
	private String tcode;
	private String appName;
	private List<String> bannerList;
	
	public String queryAppName() {
		try {
			if (!StringUtils.isBlank(tcode)) {
	            
	            appName = "欢乐刮刮奖";
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String queryBannerList() {
		try {
			bannerList = new ArrayList<String>();
			bannerList.add("http://img02.tooopen.com/images/20150928/tooopen_sy_143912755726.jpg");
			bannerList.add("http://img06.tooopen.com/images/20160818/tooopen_sy_175866434296.jpg");
			bannerList.add("http://img06.tooopen.com/images/20160818/tooopen_sy_175833047715.jpg");
			
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}

	public String getTcode() {
		return tcode;
	}
	public void setTcode(String tcode) {
		this.tcode = tcode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public List<String> getBannerList() {
		return bannerList;
	}
	public void setBannerList(List<String> bannerList) {
		this.bannerList = bannerList;
	}

}
