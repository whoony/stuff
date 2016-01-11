package com.stuff.dashboard.view.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.stuff.bean.DailyReport;
import com.stuff.component.util.DownloadUtil;
import com.stuff.controller.DailyReportUtil;
import com.stuff.util.Config;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

public class HistoryReportUI extends VerticalLayout
{

	private static final long serialVersionUID = -4763821605645832294L;
	private Table table = new Table();
	private String[] headers = new String[]{"文件名", "时间", "操作"};
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
	private String baseUrl = Config.getInstance().get("upload.dir");
	private Date viewedDate;
	
	public HistoryReportUI()
	{
		Label caption = new Label("<h2>历史日报</h2>", ContentMode.HTML);
		addComponent(caption);
		
		init();
	}
	
	private void init()
	{
		setSizeFull();
		
		viewedDate = new Date();
		DateField dateField = new DateField();
		dateField.setResolution(Resolution.MONTH);
		dateField.setLocale(Locale.CHINA);
		dateField.setValue(viewedDate);
		dateField.setRangeEnd(viewedDate);
		dateField.addValueChangeListener(new ValueChangeListener(){
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				Date value = (Date) event.getProperty().getValue();
				if(value == null)
				{
					table.removeAllItems();
				}
				else
				{
					viewedDate = value;
					refreshTable();
				}
			}
		});
		
		VerticalLayout tableCon = new VerticalLayout();
		tableCon.setWidth("100%");
		tableCon.setMargin(true);
		addComponent(tableCon);
		
		HorizontalLayout dateFieldCon = new HorizontalLayout();
		dateFieldCon.setSpacing(true);
		Label dateLabel = new Label("选择月份");
		dateFieldCon.addComponent(dateLabel);
		dateFieldCon.setComponentAlignment(dateLabel, Alignment.MIDDLE_LEFT);
		dateFieldCon.addComponent(dateField);
		tableCon.addComponent(dateFieldCon);
		tableCon.addComponent(table);
		
		table.setSelectable(false);
		table.setImmediate(true);
		table.setNullSelectionAllowed(false);
		table.setPageLength(10);
		
		for(int i = 0; i < headers.length; i++)
		{
			table.addContainerProperty(headers[i], Component.class, null, headers[i], null, Align.CENTER);
			table.setColumnWidth(headers[i], 200);
		}
		table.setColumnWidth("文件名", 300);
		refreshTable();
	}
	
	private void refreshTable()
	{
		table.removeAllItems();
		List<DailyReport> reports = getDailyReport();
		if(reports.isEmpty())
			return;
		
		for(DailyReport report : reports)
		{
			Button download = new Button("下载");
			download.setData(report);
			
			HorizontalLayout buttons = new HorizontalLayout(download);
			buttons.setSpacing(true);
			
			String filename = DownloadUtil.decodeFilename(report.getGeneratedName());
			Label nameLabel = new Label(filename);
			Label timeLabel = new Label(timeFormatter.format(report.getChooseTime()));
			
			table.addItem(new Object[]{nameLabel, timeLabel, buttons}, report);
			
			DownloadUtil.attach(download, baseUrl + report.getSavedPath(), report.getGeneratedName(), report.getMimeType());
		}
		
	}
	
	
	private List<DailyReport> getDailyReport()
	{
		return DailyReportUtil.getMonthReport(viewedDate);
	}
	
}
