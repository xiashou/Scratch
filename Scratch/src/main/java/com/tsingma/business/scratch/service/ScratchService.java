package com.tsingma.business.scratch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.business.scratch.dao.ScratchDao;
import com.tsingma.business.scratch.dao.ScratchTextDao;
import com.tsingma.business.scratch.model.Scratch;
import com.tsingma.business.scratch.model.ScratchText;

@Service("scratchService")
@Transactional
public class ScratchService {

	@Autowired
	private ScratchDao scratchDao;
	@Autowired
	private ScratchTextDao scratchTextDao;
	
	/**
	 * 根据appid查询活动设置
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public List<Scratch> getListByAppid(String appid) throws Exception {
		return scratchDao.loadListByAppid(appid);
	}
	
	public Scratch getById(Integer id) throws Exception {
		return scratchDao.get(id);
	}
	
	public ScratchText getTextById(Integer id) throws Exception {
		return scratchTextDao.get(id);
	}
	
	public ScratchText getTextByActId(Integer actId) throws Exception {
		return scratchTextDao.loadByActId(actId);
	}
	
	public void insert(Scratch scratch) throws Exception {
		scratchDao.save(scratch);
	}
	
	public void update(Scratch scratch) throws Exception {
		scratchDao.update(scratch);
	}
	
	public void merge(Scratch scratch) throws Exception {
		scratchDao.merge(scratch);
	}
	
	public void delete(Scratch scratch) throws Exception {
		scratchDao.deleteObject(scratch);
	}
	
	public void insertText(ScratchText text) throws Exception {
		scratchTextDao.save(text);
	}
	
	public void updateText(ScratchText text) throws Exception {
		scratchTextDao.update(text);
	}
	
	public void mergeText(ScratchText text) throws Exception {
		scratchTextDao.merge(text);
	}
	
	public void deleteText(ScratchText text) throws Exception {
		scratchTextDao.deleteObject(text);
	}
}
