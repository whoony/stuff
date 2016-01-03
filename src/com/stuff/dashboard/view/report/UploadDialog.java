package com.stuff.dashboard.view.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.stuff.bean.DailyReport;
import com.stuff.component.util.DialogUtil;
import com.stuff.dashboard.DashboardUI;
import com.stuff.util.Config;
import com.stuff.util.DataUtil;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UploadDialog extends Window implements Receiver, SucceededListener
{
	private static final SimpleDateFormat pathFormatter = new SimpleDateFormat("/yyyy/MM/");
	private static final SimpleDateFormat nameFormatter = new SimpleDateFormat("yyyy-MM-dd HH-mm");
	
	private VerticalLayout root = new VerticalLayout();
	private File file;
	private DailyReport report;
	private Upload upload;
	
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
		upload = new Upload();
		root.addComponent(upload);

//		upload.setCaption("。。。。。。请选择文件。。。。。。");
		upload.setButtonCaption("点击上传"); //$NON-NLS-1$

		upload.setReceiver(this);
		upload.addSucceededListener(this);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) 
	{
		//TODO: save dailyreport
		close();
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) 
	{
		boolean flag = true;
		if(DataUtil.isEmptyString(filename))
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
		
		FileOutputStream fos = null;
		String uploadBase = Config.getInstance().get("upload.dir");
		String dateString = pathFormatter.format(new Date());
		String path = uploadBase + dateString + DashboardUI.getCurrentUser().getUsername();
		String ext = filename.substring(filename.lastIndexOf("."));
		String filenameUUID = UUID.randomUUID().toString() + ext;
		String filePath = path + "/" + filenameUUID;
		File dic = new File(path);
		if(!dic.exists())
		{
			dic.mkdirs();
		}
        file = new File(filePath);  
        
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
        report.setChooseTime(new Date());
        
        String nameFormat = nameFormatter.format(report.getChooseTime());
		report.setGeneratedName(DashboardUI.getCurrentUser().getName() + " " + nameFormat + ext);
        report.setSavedPath(filePath);
        report.setPrincipalId(DashboardUI.getCurrentUser().getId());
        
        return fos; 
	}

}
