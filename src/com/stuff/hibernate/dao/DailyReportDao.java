package com.stuff.hibernate.dao;

import com.stuff.bean.DailyReport;
import com.stuff.hibernate.BaseHibernateDAO;

public class DailyReportDao extends BaseHibernateDAO<DailyReport>{

	private static DailyReportDao instance = new DailyReportDao();
	
	public static DailyReportDao getInstance()
	{
		return instance;
	}
	
	
	
	@Override
	protected Class<DailyReport> getEntityClazz() {
		return DailyReport.class;
	}

}
