package com.tsingma.business.store.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.tsingma.business.store.model.Store;
import com.tsingma.common.dao.BaseDao;

@Component("storeDao")
public class StoreDao extends BaseDao<Store, Serializable> {

	/**
	 * 根据appid查询所有启用门店
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Store> loadListByAppid(String appid) throws Exception {
		return super.listByHql("from Store s where s.appid = ?0 and s.enable = 1", appid);
	}
	
	/**
	 * 分页查询商户
	 * @param store
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<Store> loadListPage(Store store, int start, int limit) throws Exception {
		return super.listPage(createListQuery(store), start, limit);
	}
	
	/**
	 * 分页总数
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(Store store) throws Exception {
		return super.listCount(createCountQuery(store));
	}
	
	private CriteriaQuery<Store> createListQuery(Store store){
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Store> query = builder.createQuery(Store.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<Store> root = query.from(Store.class); 
        query.select(root);
        predicatesList.add(builder.equal(root.get("appid"), store.getAppid()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
        return query;
	}
	
	private CriteriaQuery<Long> createCountQuery(Store store){
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<Store> root = query.from(Store.class);  
		query.select(builder.count(root));
		predicatesList.add(builder.equal(root.get("appid"), store.getAppid()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return query;
	}
}
