package com.tsingma.system.sys.dao;

import java.io.Serializable;

import org.springframework.stereotype.Component;

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
}
