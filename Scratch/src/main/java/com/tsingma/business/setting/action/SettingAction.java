package com.tsingma.business.setting.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.business.setting.model.Banner;
import com.tsingma.business.setting.service.BannerService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("SettingAction")
public class SettingAction extends BaseAction {

	private static final long serialVersionUID = 745570317899076173L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private BannerService bannerService;
	
	private String appid;
	private Banner banner;
	private File upload;
	private String uploadFileName;
	
	private String tcode;
	private String appName;
	private List<Banner> bannerList;
	
	/**
	 * 获取系统名称
	 * @return
	 */
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
	
	/**
	 * 获取banner列表
	 * @return
	 */
	public String queryBannerList() {
		try {
			bannerList = bannerService.getListByAppid(super.getWxMaConfig().getAppid());
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertBanner() {
		try {
			if(!Utils.isEmpty(banner)){
				
				String appId = super.getWxMaConfig().getAppid();
				
				if(!Utils.isEmpty(appId)){
					if (!Utils.isEmpty(upload)) {
			        	String realpath = ServletActionContext.getServletContext().getRealPath("/upload/banner/" + appId);
			        	String suffix;
			        	if(uploadFileName != null && !"".equals(uploadFileName) && uploadFileName.indexOf(".") > 0)
							suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toLowerCase();
						else
							suffix = "";
			        	if("jpeg".equals(suffix) || "jpg".equals(suffix) || "png".equals(suffix) || "gif".equals(suffix) || "bmp".equals(suffix)){
			        		uploadFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "." + suffix;
			        		File savefile = new File(new File(realpath), uploadFileName);
			        		if (!savefile.getParentFile().exists())
			        			savefile.getParentFile().mkdirs();
			        		FileUtils.copyFile(upload, savefile);
			        		this.setResult(true, "上传成功！");
			        	} else
			        		this.setResult(false, "请上传正确的图片格式！");
			        	banner.setAppid(appId);
			        	banner.setBannerUrl(uploadFileName);
			        }
					bannerService.insert(banner);
					this.setResult(true, "添加成功！");
				}
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateBanner() {
		try {
			if(!Utils.isEmpty(banner)){
				bannerService.update(banner);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteBanner() {
		try {
			if(!Utils.isEmpty(banner)){
				bannerService.delete(banner);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
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
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public List<Banner> getBannerList() {
		return bannerList;
	}
	public void setBannerList(List<Banner> bannerList) {
		this.bannerList = bannerList;
	}
	public Banner getBanner() {
		return banner;
	}
	public void setBanner(Banner banner) {
		this.banner = banner;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
