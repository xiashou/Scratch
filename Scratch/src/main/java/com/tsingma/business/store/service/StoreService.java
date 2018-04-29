package com.tsingma.business.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.store.dao.StoreDao;
import com.tsingma.business.store.model.Store;

@Service("storeService")
@Transactional
public class StoreService {

	@Autowired
	private StoreDao storeDao;
	
	/**
	 * 分页查询商户
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public List<Store> getListPage(Store store, int start, int limit) throws Exception {
		return storeDao.loadListPage(store, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long getListCount(Store store) throws Exception {
		return storeDao.loadListCount(store);
	}
	
	/**
	 * 根据appId查询所有启用门店
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Store> getListByAppid(String appid) throws Exception {
		List<Store> list = storeDao.loadListByAppid(appid);
		for(Store store : list){
			//查询已消费次数
			store.setTimes(1);
		}
		return list;
	}
	
	/**
	 * 查询所有商户
	 * @return
	 * @throws Exception
	 */
	public List<Store> getAll() throws Exception {
		return storeDao.list();
	}
	
	public Store getById(Integer id) throws Exception {
		return storeDao.get(id);
	}

	public void insert(Store store) throws Exception {
		storeDao.save(store);
	}
	
	public void update(Store store) throws Exception {
		storeDao.update(store);
	}
	
	public void merge(Store store) throws Exception {
		storeDao.merge(store);
	}
	
	public void delete(Store store) throws Exception {
		storeDao.deleteObject(store);
	}
}
