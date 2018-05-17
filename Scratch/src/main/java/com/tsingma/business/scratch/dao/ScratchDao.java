package com.tsingma.business.scratch.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tsingma.business.scratch.model.Scratch;
import com.tsingma.common.dao.BaseDao;

@Component("scratchDao")
public class ScratchDao extends BaseDao<Scratch, Serializable> {

	/**
	 * 查询活动所有设置
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public List<Scratch> loadListByAppid(String appid) throws Exception {
		return super.listByHql("from Scratch s where s.actId = (select a.id from Activity a where a.appid = ?0 and a.enable = 1)", appid);
	}
	
}
