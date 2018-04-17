package com.tsingma.business.store.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.store.model.Store;
import com.tsingma.common.dao.BaseDao;

@Component("storeDao")
public class StoreDao extends BaseDao<Store, Serializable> {

	/**
	 * 根据appid查询门店
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Store> loadListByAppid(String appid) throws Exception {
		return super.listByHql("from Store s where s.appid = ?0", appid);
	}
}
