package com.stuff.controller;

import java.util.Date;
import java.util.List;

import com.stuff.bean.DailyReport;
import com.stuff.bean.helper.DailyReportHelper;
import com.stuff.dashboard.DashboardUI;
import com.stuff.hibernate.QueryBuilder;
import com.stuff.hibernate.QueryBuilder.PropertyClause;
import com.stuff.hibernate.dao.DailyReportDao;
import com.stuff.util.DataUtil;

public class DailyReportUtil 
{
	public static DailyReportDao dao = DailyReportDao.getInstance();
	
	
	public static boolean saveReport(DailyReport report)
	{
		DailyReport saved = dao.saveEntity(report);
		return saved != null;
	}
	
	public static boolean updateReport(DailyReport report)
	{
		DailyReport updated = dao.mergeEntity(report);
		return updated != null;
	}
	
	
	public static DailyReport getTodayReport() 
	{
		QueryBuilder qb = dao.getQueryBuilder();
		qb.addPropertyClause(PropertyClause.valueOf("principalId", "=", DashboardUI.getCurrentUser().getId()));
		PropertyClause dateProp = PropertyClause.valueOf("DATE_FORMAT(createTime,'%Y-%m-%d')", "=", DataUtil.dateToStringForCompare(new Date()));
		dateProp.setNeedAlias(false);
		qb.addPropertyClause(dateProp);
		
		List<DailyReport> list = qb.getList();
		return list.isEmpty() ? null : list.get(0);
	}

	
	public List<DailyReport> getHistoryReport(Date from, Date to)
	{
		
		return null;
	}
	

	public List<DailyReportHelper> getStuffReport(Date from, Date to)
	{
		
		
		return null;
	}
	
}
