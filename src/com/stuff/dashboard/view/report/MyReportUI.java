package com.stuff.dashboard.view.report;

import com.stuff.bean.DailyReport;
import com.stuff.controller.DailyReportUtil;
import com.vaadin.ui.VerticalLayout;

public class MyReportUI extends VerticalLayout
{
	
	
	public MyReportUI()
	{
		DailyReport report = DailyReportUtil.getTodayReport();
	}
}
