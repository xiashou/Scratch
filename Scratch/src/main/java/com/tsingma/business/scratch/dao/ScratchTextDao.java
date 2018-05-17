package com.tsingma.business.scratch.dao;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.tsingma.business.scratch.model.ScratchText;
import com.tsingma.common.dao.BaseDao;

@Component("scratchTextDao")
public class ScratchTextDao extends BaseDao<ScratchText, Serializable> {

	public ScratchText loadByActId(Integer actId) throws Exception {
		return super.uniqueResult("actId", actId);
	}
	
}
