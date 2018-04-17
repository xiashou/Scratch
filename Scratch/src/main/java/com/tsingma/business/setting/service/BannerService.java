package com.tsingma.business.setting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.setting.dao.BannerDao;
import com.tsingma.business.setting.model.Banner;

@Service("bannerService")
@Transactional
public class BannerService {

	@Autowired
	private BannerDao bannerDao;
	
	/**
	 * 查询小程序banner列表
	 * @return
	 * @throws Exception
	 */
	public List<Banner> getListByAppid(String appid) throws Exception {
		return bannerDao.loadListByAppid(appid);
	}
	
	/**
	 * 添加
	 * @param banner
	 * @throws Exception
	 */
	public void insert(Banner banner) throws Exception {
		bannerDao.save(banner);
	}
	
	public void update(Banner banner) throws Exception {
		bannerDao.update(banner);
	}
	
	public void delete(Banner banner) throws Exception {
		bannerDao.deleteObject(banner);
	}
}
