package com.tsingma.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tsingma.core.util.Utils;


public class BaseDao<T, PK extends Serializable> {

	// 泛型反射类
	private Class<T> entityClass;

	// 通过反射获取子类确定的泛型类
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseDao() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	/**
	 * 注入sessionFactory
	 */
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 保存PO
	 */
	@SuppressWarnings("unchecked")
	public PK save(T entity) {
		return (PK) getSession().save(entity);
	}

	/**
	 * 保存或更新PO
	 */
	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	/**
	 * 更新PO
	 */
	public void update(T entity) {
		getSession().update(entity);
	}

	/**
	 * 合并PO
	 */
	public void merge(T entity) {
		getSession().merge(entity);
	}

	/**
	 * 根据id删除PO
	 */
	public void delete(PK id) {
		getSession().delete(this.get(id));
	}

	/**
	 * 
	 * 删除PO
	 */
	public void deleteObject(T entity) {
		getSession().delete(entity);
	}

	/**
	 * 
	 * 根据id判断PO是否存在
	 */
	public boolean exists(PK id) {
		return get(id) != null;
	}

	/**
	 * 
	 * 根据id获取PO
	 */
	public T get(PK id) {
		return (T) getSession().get(this.entityClass, id);
	}


	/**
	 * 
	 * 根据Criteria查询条件，获取PO总数
	 */
	public int loadCount(Criteria criteria) {
		return Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
	}
	
	/**
	 * 根据hql查询总数
	 * @param hqlString
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int loadCountHql(String hqlString, Object... values) {
		Query<T> query = this.getSession().createQuery(hqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return Integer.valueOf(Utils.isEmpty(query.uniqueResult())?"0":query.uniqueResult().toString());
	}
	
	@SuppressWarnings("unchecked")
	public String loadStringHql(String hqlString, Object... values) {
		Query<T> query = this.getSession().createQuery(hqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return Utils.isEmpty(query.uniqueResult())?"":query.uniqueResult().toString();
	}
	
	@SuppressWarnings("unchecked")
	public int loadCountSql(String sqlString, Object... values) {
		Query<T> query = this.getSession().createNativeQuery(sqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return Integer.valueOf(Utils.isEmpty(query.uniqueResult())?"0":query.uniqueResult().toString());
	}

	/**
	 * 
	 * 删除所有
	 */
	public void deleteAll(Collection<?> entities) {
		if (entities == null)
			return;
		for (Object entity : entities) {
			getSession().delete(entity);
		}
	}

	/**
	 * 
	 * 获取全部对象
	 */
	public List<T> list() {
		CriteriaQuery<T> query = getSession().getCriteriaBuilder().createQuery(entityClass);
	    query.select(query.from(entityClass));
	    Query<T> q = getSession().createQuery(query);
		return q.list();
	}

	/**
	 * 
	 * 获取对象列表根据Criteria
	 */
	@SuppressWarnings("unchecked")
	public List<T> list(Criteria criteria) {
		return criteria.list();
	}

	/**
	 * 
	 * 离线查询
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> List<T> list(DetachedCriteria criteria) {
		return (List<T>) list(criteria.getExecutableCriteria(getSession()));
	}



	/**
	 * <分页查询Criteria>
	 * @param criteria pageNo pageSize
	 * @return List<T>
	 */
	public List<T> listPage(Criteria criteria, int pageNo, int pageSize) {
		// 设置起始结果数
		criteria.setFirstResult(pageNo);
//		criteria.setFirstResult((pageNo - 1) * pageSize);
		// 返回的最大结果集
		criteria.setMaxResults(pageSize);
		return list(criteria);

	}
	
	public List<T> listPage(CriteriaQuery<T> query, int pageNo, int pageSize) {
		return getSession().createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
	}
	
	public Long listCount(CriteriaQuery<Long> query) {
		return getSession().createQuery(query).getSingleResult();
	}
	
	
	/**
	 * <根据HQL语句，得到对应的list>
	 * @param hqlString HQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> listByHql(String hqlString, Object... values) {
		Query<T> query = this.getSession().createQuery(hqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listByHql(String hqlString, Integer limit, Object... values) {
		Query<T> query = this.getSession().createQuery(hqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		query.setMaxResults(limit);
		return query.list();
	}

	/**
	 * <根据SQL语句，得到对应的list>
	 * @param sqlString HQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询多个实体的List集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> listBySql(String sqlString, Object... values ) {
		Query<T> query = this.getSession().createNativeQuery(sqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return query.list();
	}
	
	public List<T> listBySql2(String sqlString, Object... values) {
		Query<T> query = this.getSession().createNativeQuery(sqlString, entityClass);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return query.list();
	}


	/**
	 * 
	 * 按属性查找唯一对象，匹配方式为相等
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public T uniqueResult(String propertyName, Object value) {
		
		CriteriaBuilder crb = getSession().getCriteriaBuilder();  
        CriteriaQuery<T> crq=crb.createQuery(entityClass);  
        Root<T> root=crq.from(entityClass);  
        crq.select(root);
        crq.where(crb.equal(root.get(propertyName),value.toString()));
        Query<T> q = getSession().createQuery(crq);
		
		return q.uniqueResult();
	}


	/**
	 * 按Criteria查询唯一对象
	 * @param criterions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T uniqueResult(Criteria criteria) {
		return (T) criteria.uniqueResult();
	}
	
	/**
	 * <根据HQL语句查找唯一实体>
	 * @param hqlString HQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询实体
	 */
	@SuppressWarnings("unchecked")
	public T uniqueResultByHql(String hqlString, Object... values) {
		Query<T> query = this.getSession().createQuery(hqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i+"", values[i]);
            }
        }
		return (T) query.uniqueResult();
	}

	/**
	 * <根据SQL语句查找唯一实体>
	 * @param sqlString SQL语句
	 * @param values 不定参数的Object数组
	 * @return 查询实体
	 */
	@SuppressWarnings("unchecked")
	public T uniqueResultBySql(String sqlString, Object... values) {
		Query<T> query = this.getSession().createNativeQuery(sqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		return (T) query.uniqueResult();
	}
	

	/**
	 * <执行Hql语句>
	 * @param hqlString hql
	 * @param values 不定参数数组
	 */
	@SuppressWarnings("unchecked")
	public void excuteHql(String hqlString, Object... values) {
		Query<T> query = this.getSession().createQuery(hqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		query.executeUpdate();
	}
	
	/**
	 * <执行Sql语句>
	 * @param sqlString sql
	 * @param values 不定参数数组
	 */
	@SuppressWarnings("unchecked")
	public void excuteSql(String sqlString, Object... values) {
		Query<T> query = this.getSession().createNativeQuery(sqlString);
		if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
		query.executeUpdate();
	}

	
	
	/**
	 * 
	 * 强制清空session
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 
	 * 清空session
	 */
	public void clear() {
		getSession().clear();
	}
	
	/**
	 * 
	 * 清空session
	 */
	public void refresh(T entity) {
		getSession().refresh(entity);
	}

	/**
	 * 
	 * 创建Criteria实例
	 */
	public CriteriaQuery<T> createCriteria() {
		
		CriteriaQuery<T> query = getSession().getCriteriaBuilder().createQuery(entityClass);
	    query.select(query.from(entityClass));
	    Query<T> q = getSession().createQuery(query);
	    q.list();
	    
		return query;
	}

	
}
