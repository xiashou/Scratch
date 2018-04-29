package com.tsingma.system.sys.dao;

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
import com.tsingma.system.sys.model.SysUser;

@Component("sysUserDao")
public class SysUserDao extends BaseDao<SysUser, Serializable> {

	/**
	 * 根据用户名查找唯一用户
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public SysUser loadByUserName(String userName) throws Exception {
		return super.uniqueResult("userName", userName);
	}
	
	/**
	 * 根据商户id查询用户列表
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> loadListByStore(Integer roleId) throws Exception {
		return super.listByHql("from SysUser s where s.roleId = " + roleId);
	}
	
	/**
	 * 分页查询用户
	 * @param sysUser
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> loadListPage(SysUser sysUser, int start, int limit) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<SysUser> query = builder.createQuery(SysUser.class);
        List<Predicate> predicatesList = new ArrayList<Predicate>();
        Root<SysUser> root = query.from(SysUser.class); 
        query.select(root);
        predicatesList.add(builder.equal(root.get("roleId"), sysUser.getRoleId()));
        query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listPage(query, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param sysUser
	 * @return
	 * @throws Exception
	 */
	public Long loadListCount(SysUser sysUser) throws Exception {
		CriteriaBuilder builder = super.getSession().getCriteriaBuilder();  
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Root<Store> root = query.from(Store.class);  
		query.select(builder.count(root));
		predicatesList.add(builder.equal(root.get("roleId"), sysUser.getRoleId()));
		query.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
		return super.listCount(query);
	}
	
}
