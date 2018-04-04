package com.tsingma.business.setting.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.setting.model.Banner;
import com.tsingma.common.dao.BaseDao;

@Component("bannerDao")
public class BannerDao extends BaseDao<Banner, Serializable> {

	/**
	 * 查询小程序banner列表
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Banner> loadListByAppid(String appid) throws Exception {
		return super.listByHql("from Banner b where b.appid = ?0", appid);
	}
}
