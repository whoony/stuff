package com.stuff.component.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;

public class DownloadResource implements ConnectorResource
{
	private interface InputStreamSource
	{
		InputStream getInputStream(String filePath);
	}
	
	
	private String fileName;
	private String mimeType;
	private String filePath;
	
	public DownloadResource(String filePath, String filename, String mimeType)
	{
		this.filePath = filePath;
		this.fileName = filename;
		this.mimeType = mimeType;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	@Override
	public String getMIMEType() {
		return mimeType;
	}

	@Override
	public DownloadStream getStream() 
	{
		InputStreamSource in = new InputStreamSource()
		{
			@Override
			public InputStream getInputStream(String filePath)
			{
				try
				{
					return new FileInputStream(new File(filePath));
				}
				catch (FileNotFoundException e)
				{
					DialogUtil.showErrorOnScreen("找不到文件");
					return null;
				}
			}
		};
		DownloadStream stream = new DownloadStream(in.getInputStream(filePath), mimeType, fileName);
		stream.setParameter("Content-Disposition", "attachment;filename=" + fileName);
		return stream;
	}

	@Override
	public String getFilename() {
		return fileName;
	}
	
}
