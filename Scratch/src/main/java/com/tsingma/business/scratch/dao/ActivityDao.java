package com.tsingma.business.scratch.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.scratch.model.Activity;
import com.tsingma.common.dao.BaseDao;

@Component("activityDao")
public class ActivityDao extends BaseDao<Activity, Serializable> {

	/**
	 * 根据appid查询所有活动
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Activity> loadListByAppid(String appid) throws Exception {
		return super.listByHql("from Activity s where s.appid = ?0", appid);
	}
	
	/**
	 * 查询商户唯一开启的活动
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public Activity loadEnableActivity(String appid) throws Exception {
		return super.uniqueResultByHql("from Activity a where a.appid = ?0 and a.enable = 1", appid);
	}
	
	/**
	 * 设置商户所有活动为停用
	 * 一个商户下只能有一个活动开启
	 * @param appid
	 * @throws Exception
	 */
	public void editDisabled(String appid) throws Exception {
		super.excuteHql("update Activity a set a.enable = 0 where a.appid = '" + appid + "'");
	}
	
	/**
	 * 增加活动虚拟参加人数和实际人数
	 * @param id
	 * @param number
	 * @throws Exception
	 */
	public void addNumber(String appid, Integer number) throws Exception {
		super.excuteHql("update Activity a set a.virNumber = a.virNumber + " + number + ", a.actNumber = a.actNumber + " + number + " where a.appid = '" + appid + "' and a.enable = 1");
	}
	
	/**
	 * 增加活动浏览参加人数
	 * @param id
	 * @param number
	 * @throws Exception
	 */
	public void addBroNumber(Integer id, Integer number) throws Exception {
		super.excuteHql("update Activity a set a.broNumber = a.broNumber + " + number + " where a.id = " + id);
	}
}
