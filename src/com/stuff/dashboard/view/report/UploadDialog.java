package com.stuff.dashboard.view.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.stuff.bean.DailyReport;
import com.stuff.component.util.DialogUtil;
import com.stuff.component.util.DownloadUtil;
import com.stuff.controller.DailyReportUtil;
import com.stuff.dashboard.DashboardUI;
import com.stuff.util.Config;
import com.stuff.util.DataUtil;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UploadDialog extends Window implements Receiver, SucceededListener, StartedListener, FailedListener
{
	private static final long serialVersionUID = -2356504267723663885L;
	private static final SimpleDateFormat pathFormatter = new SimpleDateFormat("/yyyy/MM/");
	private static final SimpleDateFormat nameFormatter = new SimpleDateFormat("yyyy-MM-dd HH-mm");
	
	private VerticalLayout root = new VerticalLayout();
	private File file;
	private DailyReport report;
	private Upload upload;
	private DateField date;
	private boolean uploaded;
	private FileOutputStream fos;
	
	public UploadDialog()
	{
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);
		
		setContent(root);
		init();
	}
	
	private void init()
	{
		date = new DateField("上传时间");
		date.setLocale(Locale.UK);
		date.setDateFormat("HH:mm");
		date.setResolution(Resolution.MINUTE);
		Date today = new Date();
		date.setRangeStart(today);
		date.setRangeEnd(today);
		root.addComponent(date);
		
		upload = new Upload();
		root.addComponent(upload);

//		upload.setCaption("。。。。。。请选择文件。。。。。。");
		upload.setButtonCaption("点击上传"); //$NON-NLS-1$

		upload.setReceiver(this);
		upload.addSucceededListener(this);
		upload.addStartedListener(this);
		upload.addFailedListener(this);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) 
	{
		boolean saved = DailyReportUtil.saveReport(report);
		if(saved)
		{
			uploaded = true;
			close();
		}
		else
		{
			DialogUtil.showErrorOnScreen("系统错误");
			return;
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) 
	{
		boolean flag = true;
		Date uploadTime = date.getValue();
		if(uploadTime == null)
		{
			flag = false;
			DialogUtil.showErrorOnScreen("请选择上传时间！");
		}
		else if(DataUtil.isEmptyString(filename))
		{
			flag = false;
			DialogUtil.showErrorOnScreen("请选择文件！");
		}
		else if(!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))
		{
			flag = false;
			DialogUtil.showErrorOnScreen("请上传excel文件！");
		}
		
		if(!flag)
		{
			upload.interruptUpload();
			return new ByteArrayOutputStream();
		}
		
		String uploadBase = Config.getInstance().get("upload.dir");
		String path = pathFormatter.format(new Date()) + DashboardUI.getCurrentUser().getUsername();
		File dic = new File(uploadBase + path);
		if(!dic.exists())
		{
			dic.mkdirs();
		}
		
		String filenameUUID = UUID.randomUUID().toString();
		String filePath = path + "/" + filenameUUID;
        file = new File(uploadBase + filePath);  
        
        try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			DialogUtil.showErrorOnScreen("找不到文件。。。");
			upload.interruptUpload();
			return new ByteArrayOutputStream();
		}  
        
        report = new DailyReport();
        report.setUploadedName(filename);
        report.setMimeType(mimeType);
        report.setCreateTime(new Date());
        report.setChooseTime(uploadTime);
        
        String ext = filename.substring(filename.lastIndexOf("."));
        String nameFormat = nameFormatter.format(report.getChooseTime());
		report.setGeneratedName(DashboardUI.getCurrentUser().getName() + " " + nameFormat + ext);
        report.setSavedPath(filePath);
        report.setPrincipalId(DashboardUI.getCurrentUser().getId());
        
        report.setUploadedName(DownloadUtil.encodeFilename(report.getUploadedName()));
        report.setGeneratedName(DownloadUtil.encodeFilename(report.getGeneratedName()));
        
        return fos; 
	}

	@Override
	public void uploadStarted(StartedEvent paramStartedEvent) 
	{
		long length = paramStartedEvent.getContentLength();
		if(length > 120 * 1024)
		{
			DialogUtil.showErrorOnScreen("文件长度不能超过120k");
			upload.interruptUpload();
		}
	}

	
	public boolean isUploaded()
	{
		return uploaded;
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		DialogUtil.showErrorOnScreen("上传失败");
		upload.interruptUpload();
	}
}
