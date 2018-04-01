package com.tsingma.system.wechat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tsingma.core.util.Utils;

import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;

@Service("wxMiniCustomService")
public class WxMiniCustomService extends WxMaServiceImpl {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
    public void init() {
		
		Properties props = new Properties(); 
        InputStream inputStream = null; 
        try {
        	System.out.println("Loading miniProgram properties...");
            inputStream = getClass().getResourceAsStream("/wechat.properties");
            props.load(inputStream);
            
            WxMaInMemoryConfig config = new WxMaInMemoryConfig();
            config.setAppid((String) props.get("miniapp.appid"));
            config.setSecret((String) props.get("miniapp.secret"));
            config.setToken((String) props.get("miniapp.token"));
            config.setAesKey((String) props.get("miniapp.aesKey"));
            config.setMsgDataFormat((String) props.get("miniapp.msgDataFormat"));
            
            this.setWxMaConfig(config);
            
        } catch (IOException ex) { 
        	logger.error(Utils.getErrorMessage(ex));
            ex.printStackTrace(); 
        }
		
    }
}
