package com.tsingma.business.coupon.action;

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

import com.tsingma.business.coupon.model.Coupon;
import com.tsingma.business.coupon.service.CouponService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("CouponAction")
public class CouponAction extends BaseAction {

	private static final long serialVersionUID = 110881918135465869L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private CouponService couponService;
	
	private List<Coupon> couponList;
	private Coupon coupon;
	
	private File image;
	private String imageFileName;
	
	/**
	 * 分页查询优惠券
	 * @return
	 * @throws Exception
	 */
	public String queryListPage() {
		try {
			if(Utils.isEmpty(coupon))
				coupon = new Coupon();
			this.setTotalCount(couponService.getListCount(coupon));
			couponList = couponService.getListPage(coupon, this.getStart(), this.getLimit());
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertCoupon() {
		try {
			if(!Utils.isEmpty(coupon)){
				if (!Utils.isEmpty(image)) {
					String fileName = upload("coupon", image, imageFileName);
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						coupon.setImageUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				coupon.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				couponService.insert(coupon);
				this.setResult(true, "添加成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateCoupon() {
		try {
			if(!Utils.isEmpty(coupon) && !Utils.isEmpty(coupon.getId())){
				Coupon exist = couponService.getById(coupon.getId());
				exist.setDescrib(coupon.getDescrib());
				exist.setEndDate(coupon.getEndDate());
				exist.setName(coupon.getName());
				exist.setNumber(coupon.getNumber());
				if (!Utils.isEmpty(image)) {
					String fileName = upload("coupon", image, imageFileName);
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						exist.setImageUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				couponService.merge(exist);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteCoupon() {
		try {
			if(!Utils.isEmpty(coupon)){
				couponService.delete(coupon);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			this.setResult(false, "发生错误！");
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

	public List<Coupon> getCouponList() {
		return couponList;
	}
	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
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

}
