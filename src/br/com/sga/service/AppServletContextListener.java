package br.com.sga.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println();
		System.out.println("ServletContextListener started");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println();
		System.out.println("ServletContextListener destroyed");
	}
}