package com.tsingma.business.store.action;

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

import com.tsingma.business.store.model.Store;
import com.tsingma.business.store.service.StoreService;
import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("StoreAction")
public class StoreAction extends BaseAction {

	private static final long serialVersionUID = 4588740724635168769L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private StoreService storeService;
	
	private List<Store> storeList;
	private Store store;
	private String tcode;
	private Integer id;
	
	private File head;
	private File image;
	private String headFileName;
	private String imageFileName;
	
	/**
	 * 分页查询商户
	 * @return
	 * @throws Exception
	 */
	public String queryListPage() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
				if(Utils.isEmpty(store))
					store = new Store();
				store.setAppid(super.getWxMaConfig().getAppid());
				this.setTotalCount(storeService.getListCount(store));
	            storeList = storeService.getListPage(store, this.getStart(), this.getLimit());
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询可用商户
	 * （后台端）
	 * @return
	 * @throws Exception
	 */
	public String queryListByConfig() {
		try {
			if (!Utils.isEmpty(super.getWxMaConfig())) {
	            storeList = storeService.getListByAppid(super.getWxMaConfig().getAppid());
	        }
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询可用商户
	 * （小程序端）
	 * @return
	 * @throws Exception
	 */
	public String queryListByAppid() {
		try {
			if (!Utils.isEmpty(tcode)) {
	            storeList = storeService.getListByAppid(tcode);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 查询商户详细信息
	 * （小程序端）
	 * @return
	 * @throws Exception
	 */
	public String queryById() {
		try {
			if (!Utils.isEmpty(id)) {
				store = storeService.getById(id);
	        } 
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertStore() {
		try {
			if(!Utils.isEmpty(store)){
				store.setAppid(super.getWxMaConfig().getAppid());
				if (!Utils.isEmpty(head)) {
					String fileName = upload("store/" + store.getAppid(), head, headFileName, "small");
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						store.setHeadUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				if (!Utils.isEmpty(image)) {
					String fileName = upload("store/" + store.getAppid(), image, imageFileName, "large");
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						store.setImageUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				store.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				storeService.insert(store);
				this.setResult(true, "添加成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateStore() {
		try {
			if(!Utils.isEmpty(store) && !Utils.isEmpty(store.getId())){
				Store exist = storeService.getById(store.getId());
				exist.setAppid(super.getWxMaConfig().getAppid());
				exist.setAddress(store.getAddress());
				exist.setEnable(store.getEnable());
				exist.setIntroduction(store.getIntroduction());
				exist.setLocationx(store.getLocationx());
				exist.setLocationy(store.getLocationy());
				exist.setName(store.getName());
				exist.setPhone(store.getPhone());
				if (!Utils.isEmpty(head)) {
					String fileName = upload("store/" + store.getAppid(), head, headFileName, "small");
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						exist.setHeadUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				if (!Utils.isEmpty(image)) {
					String fileName = upload("store/" + store.getAppid(), image, imageFileName, "large");
					if(fileName.contains("jpeg") || fileName.contains("jpg") || fileName.contains("png") || fileName.contains("gif")){
						exist.setImageUrl(fileName);
					} else 
						this.setResult(false, "请上传正确的图片格式！");
		        }
				storeService.merge(exist);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteStore() {
		try {
			if(!Utils.isEmpty(store)){
				storeService.delete(store);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	private String upload(String path, File file, String fileName, String suff) throws Exception {
		String realpath = ServletActionContext.getServletContext().getRealPath("/upload/" + path);
    	String suffix;
    	if(fileName != null && !"".equals(fileName) && fileName.indexOf(".") > 0)
			suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		else
			suffix = "";
		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + suff + "." + suffix;
		File savefile = new File(new File(realpath), fileName);
		if (!savefile.getParentFile().exists())
			savefile.getParentFile().mkdirs();
		FileUtils.copyFile(file, savefile);
    	return fileName;
	}

	public List<Store> getStoreList() {
		return storeList;
	}
	public void setStoreList(List<Store> storeList) {
		this.storeList = storeList;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public File getHead() {
		return head;
	}
	public void setHead(File head) {
		this.head = head;
	}
	public File getImage() {
		return image;
	}
	public void setImage(File image) {
		this.image = image;
	}
	public String getHeadFileName() {
		return headFileName;
	}
	public void setHeadFileName(String headFileName) {
		this.headFileName = headFileName;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	

}
