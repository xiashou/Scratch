package com.tsingma.common.action;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tsingma.core.util.Utils;

@Scope("prototype")
@Component("ErrorAction")
public class ErrorAction extends BaseAction {

	private static final long serialVersionUID = 4849760928685668337L;
	private static Logger log = Logger.getLogger("SLog");

	public String execute(){
		try {
			this.setResult(false, "error!");
		} catch(Exception e) {
			log.error(Utils.getErrorMessage(e));
		}
		return SUCCESS;
	}
}
