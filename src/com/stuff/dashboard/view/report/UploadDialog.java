package com.stuff.dashboard.view.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

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
		String path = uploadBase + "/12/16";
		File dic = new File(path);
		if(!dic.exists())
		{
			dic.mkdirs();
		}
        file = new File("path" + filename);  
        
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
        report.setGeneratedName("generename");
        report.setSavedPath("/dd/dd");
        report.setPrincipalId(DashboardUI.getCurrentUser().getId());
        
        return fos; 
	}

}
