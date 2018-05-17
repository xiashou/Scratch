package com.tsingma.business.member.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.member.dao.MemScratchDao;
import com.tsingma.business.member.model.MemScratch;
import com.tsingma.core.util.Utils;

@Service("memScratchService")
@Transactional
public class MemScratchService {

	@Autowired
	private MemScratchDao memScratchDao;
	
	/**
	 * 根据openid查询会员最后刮奖结果
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public MemScratch getLastByOpenid(Integer actId, String openid) throws Exception {
		List<MemScratch> list = memScratchDao.loadLastByOpenid(actId, openid);
		return Utils.isEmpty(list)?null:list.get(0);
	}
	
	/**
	 * 根据openid查询会员刮奖次数
	 * @param actId
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public Integer getCountByOpenid(Integer actId, String openid) throws Exception {
		return memScratchDao.loadCountByOpenid(actId, openid);
	}
	
	/**
	 * 查询会员累计刮奖金额
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public Double getPriceByOpenid(String openid) throws Exception {
		return memScratchDao.loadPriceByOpenid(openid);
	}
	
	/**
	 * 设置为已刮奖
	 * @param id
	 * @throws Exception
	 */
	public void updateAlreadyScratch(Integer id) throws Exception {
		memScratchDao.editAlreadyScratch(id, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
	}
}
