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
	 * 根据appId查询门店
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Store> getListByAppid(String appid) throws Exception {
		return storeDao.loadListByAppid(appid);
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
	
	public static void main(String[] args) {
		System.out.println("asdfawefe.jp".contains("jpg"));
	}
}
