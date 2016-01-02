package com.stuff.dashboard.view.report;

import com.stuff.bean.DailyReport;
import com.stuff.component.util.DialogUtil;
import com.stuff.controller.DailyReportUtil;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MyReportUI extends VerticalLayout
{
	private Table table = new Table();
	private String[] headers = new String[]{"文件名", "时间", "动作"};
	
	public MyReportUI()
	{
		Label caption = new Label("<h2>今日日报</h2>", ContentMode.HTML);
		addComponent(caption);
		
		init();
	}
	
	private void init()
	{
		setSizeFull();
		setMargin(true);
		
		VerticalLayout tableCon = new VerticalLayout();
		tableCon.setWidth("100%");
		tableCon.setMargin(true);
		addComponent(tableCon);
		tableCon.addComponent(table);
		
		Button add = new Button("上传");
		add.addClickListener(new ClickListener(){
			@Override
			public void buttonClick(ClickEvent event) {
				UploadDialog uploadDialog = new UploadDialog();
				uploadDialog.setCaption("上传日报");
				uploadDialog.setWidth("400px");
				uploadDialog.setHeight("300px");
				uploadDialog.setModal(true);
				
				UI.getCurrent().addWindow(uploadDialog);
				uploadDialog.bringToFront();
			}
		});
		addComponent(add);
		setComponentAlignment(add, Alignment.BOTTOM_LEFT);
		Label info = new Label("<ul><li><h4>上传成功后如需修改，请删除后重新上传</h4></li>"
				+ "<li><h4>如不需要修改，请点击锁定确认提交</h4></li>"
				+ "<li><h4>文件锁定后，无法再次删除</h4></li>"
				+ "</ul>", ContentMode.HTML);
		addComponent(info);
		setComponentAlignment(info, Alignment.BOTTOM_LEFT);
		
		table.setSelectable(true);
		table.setImmediate(true);
		table.setNullSelectionAllowed(false);
		table.setPageLength(1);
		
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
		DailyReport report = getDailyReport();
		if(report == null)
			return;
		
		Button delete = new Button("删除");
		Button submit = new Button("锁定");
		
	}
	
	
	private DailyReport getDailyReport()
	{
		
		
		return DailyReportUtil.getTodayReport();
	}
	
	
	
	
	
	
}
