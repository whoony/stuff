package com.stuff.dashboard.view.report;

import java.text.SimpleDateFormat;

import com.stuff.bean.DailyReport;
import com.stuff.component.util.ConfirmListener;
import com.stuff.component.util.DialogUtil;
import com.stuff.component.util.DownloadUtil;
import com.stuff.controller.DailyReportUtil;
import com.stuff.util.Config;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class MyReportUI extends VerticalLayout
{
	private static final long serialVersionUID = -6064547651936847619L;
	private Table table = new Table();
	private String[] headers = new String[] { "文件名", "时间", "已锁定", "动作" };
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
	private String baseUrl = Config.getInstance().get("upload.dir");
	private Button add;
	
	public MyReportUI()
	{
		Label caption = new Label("<h2>今日日报</h2>", ContentMode.HTML);
		addComponent(caption);

		init();
	}

	private void init()
	{
		setSizeFull();

		VerticalLayout tableCon = new VerticalLayout();
		tableCon.setWidth("100%");
		tableCon.setMargin(true);
		addComponent(tableCon);
		tableCon.addComponent(table);

		add = new Button("上传");
		add.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = -8648292618652554233L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				final UploadDialog uploadDialog = new UploadDialog();
				uploadDialog.setCaption("上传日报");
				uploadDialog.setWidth("400px");
				uploadDialog.setHeight("300px");
				uploadDialog.setModal(true);

				uploadDialog.addCloseListener(new CloseListener()
				{
					private static final long serialVersionUID = -7875906775813074716L;

					@Override
					public void windowClose(CloseEvent paramCloseEvent)
					{
						if (uploadDialog.isUploaded())
						{
							refreshTable();
						}
					}
				});

				UI.getCurrent().addWindow(uploadDialog);
				uploadDialog.bringToFront();
			}
		});
		addComponent(add);
		setComponentAlignment(add, Alignment.BOTTOM_LEFT);
		Label info = new Label("<ul>" + "<li><h4>文件大小不能超过120k</h4></li>" + "<li><h4>上传成功后如需修改，请删除后重新上传</h4></li>"
				+ "<li><h4><font color='red'>如不需要修改，请点击锁定确认提交</font></h4></li>" + "<li><h4>文件锁定后，无法再次删除</h4></li>"
				+ "</ul>", ContentMode.HTML);
		addComponent(info);
		setComponentAlignment(info, Alignment.BOTTOM_LEFT);

		table.setSelectable(false);
		table.setImmediate(true);
		table.setNullSelectionAllowed(false);
		table.setPageLength(1);

		for (int i = 0; i < headers.length; i++)
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
		final DailyReport report = getDailyReport();
		if (report == null)
		{
			add.setVisible(true);
			return;
		}
		add.setVisible(false);

		Button delete = new Button("删除");
		delete.setData(report);
		Button submit = new Button("锁定");
		submit.setData(report);
		Button download = new Button("下载");
		download.setData(report);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		if (report.isLocked())
		{
			buttons.addComponent(download);
		}
		else
		{
			buttons.addComponents(submit, delete);
		}

		String filename = DownloadUtil.decodeFilename(report.getGeneratedName());
		Label nameLabel = new Label(filename);
		Label timeLabel = new Label(timeFormatter.format(report.getChooseTime()));
		Label lockedLabel = new Label(report.isLocked() ? "<font color='blue'>是</font>" : "<font color='red'>否</font>",
				ContentMode.HTML);

		table.addItem(new Object[] { nameLabel, timeLabel, lockedLabel, buttons }, report);

		submit.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				DailyReport data = (DailyReport) event.getButton().getData();
				if (data == null)
					return;

				data.setLocked(true);
				boolean updated = DailyReportUtil.updateReport(data);
				if (updated)
				{
					refreshTable();
				}
				else
				{
					DialogUtil.showErrorOnScreen("系统错误");
				}
			}

		});

		DownloadUtil.attach(download, baseUrl + report.getSavedPath(), report.getGeneratedName(), report.getMimeType());

		delete.addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				DialogUtil.deleteConfirm(new ConfirmListener()
				{
					@Override
					public void accept(boolean flag)
					{
						if (flag)
						{
							// don't need delete file
							// File file = new File(baseUrl +
							// report.getSavedPath());
							// if(!file.exists())
							// return;
							// if(file.delete())
							if (DailyReportUtil.deleteFile(report))
							{
								add.setVisible(true);
								refreshTable();
							}
							else
							{
								DialogUtil.showErrorOnScreen("删除失败");
							}
						}
					}
				});
			}
		});
	}

	private DailyReport getDailyReport()
	{
		return DailyReportUtil.getTodayReport();
	}

}
