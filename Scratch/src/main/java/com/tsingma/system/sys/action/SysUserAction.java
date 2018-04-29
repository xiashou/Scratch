package com.tsingma.system.sys.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.common.action.BaseAction;
import com.tsingma.common.exception.TsingmaException;
import com.tsingma.core.util.Utils;
import com.tsingma.system.sys.model.SysUser;
import com.tsingma.system.sys.service.SysUserService;

@Scope("prototype")
@Component("SysUserAction")
public class SysUserAction extends BaseAction {

	private static final long serialVersionUID = -4898644525743857687L;
	private static Logger log = Logger.getLogger("SLog");
	
	@Autowired
	private SysUserService sysUserService;
	
	private List<SysUser> userList;
	private SysUser sysUser;
	
	private Integer roleId;
	
	/**
	 * 分页查询系统用户
	 * @return
	 */
	public String queryListByStore() {
		try {
			if(!Utils.isEmpty(roleId))
				userList = sysUserService.getListByStore(roleId);
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String insertSysUser() {
		try {
			if(!Utils.isEmpty(sysUser)){
				sysUserService.insert(sysUser);
				this.setResult(true, "添加成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(TsingmaException e) {
			this.setResult(false, e.getMessage());
		} catch(Exception e) {
			this.setResult(false, "发生错误!");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String updateSysUser() {
		try {
			if(!Utils.isEmpty(sysUser)){
				sysUserService.update(sysUser);
				this.setResult(true, "修改成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(TsingmaException e) {
			this.setResult(false, e.getMessage());
		} catch(Exception e) {
			this.setResult(false, "发生错误!");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	public String deleteSysUser() {
		try {
			if(!Utils.isEmpty(sysUser)){
				sysUserService.delete(sysUser);
				this.setResult(true, "删除成功！");
			} else
				this.setResult(false, "参数不能为空！");
		} catch(TsingmaException e) {
			this.setResult(false, e.getMessage());
		} catch(Exception e) {
			this.setResult(false, "发生错误!");
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}

	public List<SysUser> getUserList() {
		return userList;
	}
	public void setUserList(List<SysUser> userList) {
		this.userList = userList;
	}
	public SysUser getSysUser() {
		return sysUser;
	}
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	
}
