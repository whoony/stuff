package com.stuff.schedule;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ScheduledTasksListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		new Thread(new InitialDataTask()).start();
	}

}
