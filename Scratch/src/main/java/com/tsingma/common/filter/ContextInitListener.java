package com.tsingma.common.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("...");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ContextInitListener Destroyed!");
	}

}
