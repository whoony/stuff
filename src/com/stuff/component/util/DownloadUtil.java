package com.stuff.component.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.vaadin.server.FileDownloader;
import com.vaadin.ui.Button;

public class DownloadUtil
{
	
	public static void attach(Button button, String filePath, String name, String mimeType)
	{

		// 这种方式不能设置下载文件名
		// FileResource file = new FileResource(new
		// File(report.getSavedPath()));
		// FileDownloader fileDownloader = new FileDownloader(file);
		// fileDownloader.setFileDownloadResource(file);
		// fileDownloader.extend(download);

		DownloadResource resource = new DownloadResource(filePath, name, mimeType);
		FileDownloader fileDownloader = new FileDownloader(resource);
		fileDownloader.extend(button);
	}

	public static String encodeFilename(String filename)
	{
		try
		{
			String encodedFilename = URLEncoder.encode(filename, "utf-8");
			return encodedFilename.replace("+", "%20");
		}
		catch (UnsupportedEncodingException e)
		{
			return filename;
		}
	}

	public static String decodeFilename(String filename)
	{
		try
		{
			return URLDecoder.decode(filename, "utf-8");
		}
		catch (UnsupportedEncodingException e1)
		{
			return filename;
		}
	}
}
