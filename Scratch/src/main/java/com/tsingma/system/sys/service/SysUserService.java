package com.tsingma.system.sys.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsingma.common.exception.TsingmaException;
import com.tsingma.core.util.Utils;
import com.tsingma.system.sys.dao.SysUserDao;
import com.tsingma.system.sys.model.SysUser;

@Service("sysUserService")
@Transactional
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
	
	/**
	 * 根据商户id查询用户列表
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> getListByStore(Integer roleId) throws Exception {
		return sysUserDao.loadListByStore(roleId);
	}
	
	/**
	 * 分页查询系统用户
	 * @param sysUser
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> getListPage(SysUser sysUser, int start, int limit) throws Exception {
		return sysUserDao.loadListPage(sysUser, start, limit);
	}
	
	/**
	 * 分页总数
	 * @param sysUser
	 * @return
	 * @throws Exception
	 */
	public Long getListCount(SysUser sysUser) throws Exception {
		return sysUserDao.loadListCount(sysUser);
	}
	
	
	public void insert(SysUser sysUser) throws Exception {
		SysUser exist = sysUserDao.loadByUserName(sysUser.getUserName());
		if(Utils.isEmpty(exist)){
			sysUser.setCreatedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:dd").format(new Date()));
			sysUser.setPassword(sysUser.getPassword());
			sysUserDao.save(sysUser);
		} else
			throw new TsingmaException("用户已存在！");
			
	}
	public void update(SysUser sysUser) throws Exception {
		SysUser exist = sysUserDao.loadByUserName(sysUser.getUserName());
		if(!Utils.isEmpty(exist)){
			exist.setPassword(sysUser.getPassword());
			exist.setLocked(sysUser.getLocked());
			sysUserDao.merge(exist);
		} else
			throw new TsingmaException("用户不存在！");
	}
	
	public void delete(SysUser sysUser) throws Exception {
		sysUserDao.deleteObject(sysUser);
	}
}
