package com.tsingma.common.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("DownloadAction")
public class DownloadAction extends ActionSupport {

	private static final long serialVersionUID = 4948613716249667557L;
	private static Logger log = Logger.getLogger("SLog");
	
	private InputStream fileInput;
    private String fileName;
    private String path;
    private String appid;
    
    /**
     * 图片下载
     */
    public String execute() {
    	try {
    		if(!Utils.isEmpty(fileName) && !Utils.isEmpty(path)){
    			File file = new File(ServletActionContext.getServletContext().getRealPath("/upload/" + path + (Utils.isEmpty(appid)?"":("/" + appid)) + "/" + fileName));
//    			System.out.println(ServletActionContext.getServletContext().getRealPath("/upload/" + path + "/" + appid + "/" + fileName));
    			if(file.exists())
    				fileInput = new FileInputStream(file);
    			else
    				log.error("file not found!");
    		} else
    			fileInput = new FileInputStream(new File(ServletActionContext.getServletContext().getRealPath("/resources/img/noImage.png")));
    	} catch(Exception e){
    		log.error(Utils.getErrorMessage(e));
    	}
        return SUCCESS;  
    }
    
    public InputStream getFileInput() {
		return fileInput;
	}
	public void setFileInput(InputStream fileInput) {
		this.fileInput = fileInput;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getFileName() {
        return fileName;  
    }  
    public void setFileName(String fileName) {
        this.fileName = fileName;  
    }  
    
}
