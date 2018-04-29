package com.tsingma.business.scratch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.scratch.dao.ActivityDao;
import com.tsingma.business.scratch.model.Activity;

@Service("activityService")
@Transactional
public class ActivityService {

	@Autowired
	private ActivityDao activityDao;
	
	/**
	 * 根据appid查询所有活动
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Activity> getListByAppid(String appid) throws Exception {
		return activityDao.loadListByAppid(appid);
	}
	
	/**
	 * 查询唯一开启的活动详情
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public Activity getEnableActivity(String appid) throws Exception {
		return activityDao.loadEnableActivity(appid);
	}
	
	/**
	 * 增加活动虚拟参加人数
	 * @param id
	 * @param number
	 * @throws Exception
	 */
	public void addVirNumber(Integer id, Integer number) throws Exception {
		activityDao.addVirNumber(id, number);
	}
	
	public Activity getById(Integer id) throws Exception {
		return activityDao.get(id);
	}

	public void insert(Activity activity) throws Exception {
		if(activity.getEnable())
			activityDao.editDisabled(activity.getAppid());
		activityDao.save(activity);
	}
	
	public void update(Activity activity) throws Exception {
		if(activity.getEnable())
			activityDao.editDisabled(activity.getAppid());
		activityDao.update(activity);
	}
	
	public void merge(Activity activity) throws Exception {
		activityDao.merge(activity);
	}
	
	public void delete(Activity activity) throws Exception {
		activityDao.deleteObject(activity);
	}
}
