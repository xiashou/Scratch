package com.tsingma.system.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsingma.system.sys.dao.SysUserDao;
import com.tsingma.system.sys.model.SysUser;

@Service("sysUserService")
public class SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	/**
	 * 根据用户名查找用户
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public SysUser getByUserName(String userName) throws Exception {
		return sysUserDao.loadByUserName(userName);
	}
	
	
	public void insert(SysUser sysUser) throws Exception {
		sysUserDao.save(sysUser);
	}
	public void update(SysUser sysUser) throws Exception {
		sysUserDao.update(sysUser);
	}
	public void delete(SysUser sysUser) throws Exception {
		sysUserDao.deleteObject(sysUser);
	}
}
