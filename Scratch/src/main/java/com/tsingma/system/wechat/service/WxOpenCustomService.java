package com.tsingma.system.wechat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tsingma.core.util.Utils;
import com.tsingma.system.wechat.storage.WxOpenCustomConfigStorage;

import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;

@Service("wxOpenCustomService")
public class WxOpenCustomService extends WxOpenServiceImpl {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private WxOpenMessageRouter wxOpenMessageRouter;
	
	@PostConstruct
    public void init() {
		
		Properties props = new Properties(); 
        InputStream inputStream = null; 
        try { 
        	System.out.println("Loading wechat properties...");
            inputStream = getClass().getResourceAsStream("/wechat.properties");
            props.load(inputStream); 
            WxOpenCustomConfigStorage inRedisConfigStorage = new WxOpenCustomConfigStorage();
            inRedisConfigStorage.setComponentAppId((String) props.get("wechat.componentAppId"));
            inRedisConfigStorage.setComponentAppSecret((String) props.get("wechat.componentSecret"));
            inRedisConfigStorage.setComponentToken((String) props.get("wechat.componentToken"));
            inRedisConfigStorage.setComponentAesKey((String) props.get("wechat.componentAesKey"));
            setWxOpenConfigStorage(inRedisConfigStorage);
            
        } catch (IOException ex) { 
        	logger.error(Utils.getErrorMessage(ex));
            ex.printStackTrace(); 
        }
		
        
        
//        wxOpenMessageRouter = new WxOpenMessageRouter(this);
//        wxOpenMessageRouter.rule().handler(new WxMpMessageHandler() {
//            @Override
//            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
//                logger.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
//                return null;
//            }
//        }).next();
    }
	
	public WxOpenMessageRouter getWxOpenMessageRouter(){
        return wxOpenMessageRouter;
    }
}
