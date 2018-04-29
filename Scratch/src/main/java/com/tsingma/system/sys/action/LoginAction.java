package com.tsingma.system.sys.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.common.action.BaseAction;
import com.tsingma.core.util.Utils;
import com.tsingma.system.sys.model.SysUser;
import com.tsingma.system.sys.service.SysUserService;


@Scope("prototype")
@Component("loginAction")
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 8855074463155483936L;
	private static Logger log = Logger.getLogger("SLog");

	@Autowired
	private SysUserService sysUserService;
	
	private SysUser sysUser;
	
	/**
	 * 跳转到登录页面
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 跳转到后台首页
	 * @return
	 * @throws Exception
	 */
	public String initIndex() throws Exception {
		SysUser user = (SysUser) this.getRequest().getSession().getAttribute("SESSION_USER");
		if(user == null)
			return LOGIN;
		else
			return SUCCESS;
	}
	
	/**
	 * 后台用户登录主方法
	 * @return
	 * @throws Exception
	 */
	public String userLogin() {
		
		try {
			SysUser loginUser = null;
			if(!Utils.isEmpty(sysUser) && !Utils.isEmpty(sysUser.getUserName()))
				loginUser = sysUserService.getByUserName(sysUser.getUserName());
			if(loginUser == null){
				this.setResult(false, "该用户不存在！");
				return SUCCESS;
			}
			if(!loginUser.getPassword().equals(sysUser.getPassword())){
				this.setResult(false, "密码错误！");
				return SUCCESS;
			}
			if(loginUser.getLocked()){
				this.setResult(false, "该用户已被锁定，请联系管理员！");
				return SUCCESS;
			}
//			if(Utils.isEmpty(loginUser.getRoleId())){
//				this.setResult(false, "该用户没有绑定任何角色！");
//				return SUCCESS;
//			}
//			//获取用户角色信息， 放入session 
//			SysRole sysRole = sysRoleService.getRoleById(loginUser.getRoleId());
//			if(Utils.isEmpty(sysRole)){
//				this.setResult(false, "用户角色不存在，请联系管理员！");
//				return SUCCESS;
//			} else if(!Utils.isEmpty(sysRole) && sysRole.getLocked()){
//				this.setResult(false, "角色已锁定！");
//				return SUCCESS;
//			}
//			SysDept sysDept = sysDeptService.getById(loginUser.getDeptId());
//			Setting setting = settingService.getById(sysDept.getDeptCode());
			loginUser.setLastLogin(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			sysUserService.update(loginUser);
//			if(!Utils.isEmpty(sysDept.getAreaId()) && !"root".equals(sysDept.getAreaId())) {
//				BaseArea baseArea = baseAreaService.getAreaById(sysDept.getAreaId());
//				session.put(SESSION_AREA, baseArea);
//			}
//			List<WechatAuthorizerParams> wechatParamsList = wechatAuthorizerParamsService.getByDeptCode(sysDept.getDeptCode());
//			if(!Utils.isEmpty(wechatParamsList))
//				session.put(SESSION_WECHATAPP, wechatParamsList.get(0));
			this.getRequest().getSession().setAttribute("SESSION_USER", loginUser);
//			session.put(SESSION_USER, loginUser);
//			session.put(SESSION_ROLE, sysRole);
//			session.put(SESSION_DEPT, sysDept);
//			session.put(SESSION_SETTING, setting);
			
			this.setResult(true, "");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
	
	/**
	 * 注销
	 * @return
	 * @throws Exception
	 */
	public String logout() throws Exception {
		this.getRequest().getSession().removeAttribute("SESSION_USER");
		return SUCCESS;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
	
}
