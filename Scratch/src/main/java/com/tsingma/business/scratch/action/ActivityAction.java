package com.tsingma.business.scratch.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.tsingma.business.scratch.model.Activity;
import com.tsingma.business.scratch.service.ActivityService;
import com.tsingma.business.scratch.service.PaymentService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;
import com.tsingma.system.wechat.service.WxPayCustomService;

@Scope("prototype")
@Component("ActivityAction")
public class ActivityAction extends BaseAction {

	private static final long serialVersionUID = -8719205598234547814L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private WxPayCustomService wxPayCustomService;
	
	private List<Activity> actList;
	private Activity activity;
	private String tcode;
	private String body;
	private String fee;
	private String attach;
	private String openId;
	private File image;
	private String imageFileName;
	private WxPayMpOrderResult result;
	
	
	public String createOrder() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
				request.setBody(body);
//				request.setTradeType("JSAPI");
				request.setOutTradeNo(new SimpleDateFormat("yyyyddMMssmmHHSSS").format(new Date()));
				request.setTotalFee(Integer.parseInt(fee));
				request.setSpbillCreateIp(this.getRequest().getRemoteAddr());
				request.setAttach(attach);
//				request.setNotifyUrl("http://tsingma.imwork.net/pay/wechatPayNotify");
				request.setOpenid(openId);
				
				paymentService.insertByOrderRequest(request);
				result = wxPayCustomService.createOrder(request);
	        } 
		} catch(Exception e) {
			e.printStackTrace();
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询所有活动
	 * @return
	 * @throws Exception
	 */
	public String queryListByAppid() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				actList = activityService.getListByAppid(super.getWxMaConfig().getAppid());
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询商户开启的活动
	 * 小程序端
	 * @return
	 */
	public String queryEnableActivity() {
		try {
			if (!Utils.isEmpty(tcode)) {
				activity = activityService.getEnableActivity(tcode);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertActivity() {
		try {
			if(!Utils.isEmpty(activity)){
				activity.setAppid(super.getWxMaConfig().getAppid());
				if (!Utils.isEmpty(image)) {
					String fileName = upload("activity/" + activity.getAppid(), image, imageFileName);
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						activity.setImageUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				activity.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				activityService.insert(activity);
				this.setResult(true, "添加成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateActivity() {
		try {
			if(!Utils.isEmpty(activity) && !Utils.isEmpty(activity.getId())){
				Activity exist = activityService.getById(activity.getId());
				exist.setAppid(super.getWxMaConfig().getAppid());
				exist.setEnable(activity.getEnable());
				exist.setName(activity.getName());
				exist.setPrice(activity.getPrice());
				exist.setVirNumber(activity.getVirNumber());
				if (!Utils.isEmpty(image)) {
					String fileName = upload("activity/" + activity.getAppid(), image, imageFileName);
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						exist.setImageUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				activityService.merge(exist);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteActivity() {
		try {
			if(!Utils.isEmpty(activity)){
				activityService.delete(activity);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	private String upload(String path, File file, String fileName) throws Exception {
		String realpath = ServletActionContext.getServletContext().getRealPath("/upload/" + path);
    	String suffix;
    	if(fileName != null && !"".equals(fileName) && fileName.indexOf(".") > 0)
			suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		else
			suffix = "";
		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "." + suffix;
		File savefile = new File(new File(realpath), fileName);
		if (!savefile.getParentFile().exists())
			savefile.getParentFile().mkdirs();
		FileUtils.copyFile(file, savefile);
    	return fileName;
	}
	
	
	
	
	public List<Activity> getActList() {
		return actList;
	}
	public void setActList(List<Activity> actList) {
		this.actList = actList;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public File getImage() {
		return image;
	}
	public void setImage(File image) {
		this.image = image;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getTcode() {
		return tcode;
	}
	public void setTcode(String tcode) {
		this.tcode = tcode;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public WxPayMpOrderResult getResult() {
		return result;
	}
	public void setResult(WxPayMpOrderResult result) {
		this.result = result;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}

}
